package com.aprass.githubuser.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.aprass.githubuser.source.local.entity.UserEntity
import com.aprass.githubuser.source.local.room.UserDao
import com.aprass.githubuser.source.network.ApiService
import com.aprass.githubuser.source.network.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
) {

    fun searchUserGithub(username: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.searchGithubUser(username)
            val users = response.items

            val userList = users.map { user ->
                val favorite = userDao.isFavoriteUser(user.username)
                val responseProfile = apiService.getUserProfile(user.username)
                val name = responseProfile.name
                UserEntity(
                    user.username,
                    user.avatar,
                    name,
                    favorite
                )
            }

            userDao.deleteNoFavorite()
            userDao.insertUsers(userList)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataReturned: LiveData<Result<List<UserEntity>>> =
            userDao.getSearch(username).map { Result.Success(it) }
        emitSource(dataReturned)
    }

    fun getProfile(username: String): LiveData<Result<User>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserProfile(username)

            val returnedUser: LiveData<User> = MutableLiveData(response)
            withContext(Dispatchers.Main) {
                emitSource(returnedUser.map { Result.Success(it) })
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFollowers(username: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        val listName = mutableListOf<String>()
        try {
            val response = apiService.getUserConnectionResponse(username, "followers")

            val userEntities: List<UserEntity> = response.map { user ->
                val favorite = userDao.isFavoriteUser(user.username)
                val responseProfile = apiService.getUserProfile(user.username)
                val name = responseProfile.name
                listName.add(user.username)
                UserEntity(
                    user.username,
                    user.avatar,
                    name,
                    favorite,
                )
            }
            userDao.insertUsers(userEntities)
        } catch (e: java.lang.Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataReturned: LiveData<Result<List<UserEntity>>> =
            userDao.getUsersByName(listName).map { Result.Success(it) }
        emitSource(dataReturned)
    }

    fun getFollowing(username: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        val listName = mutableListOf<String>()
        try {
            val response = apiService.getUserConnectionResponse(username, "following")

            val userEntities: List<UserEntity> =
                response.map { user ->
                    val favorite = userDao.isFavoriteUser(user.username)
                    val responseProfile = apiService.getUserProfile(user.username)
                    val name = responseProfile.name
                    listName.add(user.username)
                    UserEntity(
                        user.username,
                        user.avatar,
                        name,
                        favorite
                    )
                }
            userDao.insertUsers(userEntities)
        } catch (e: java.lang.Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataReturned: LiveData<Result<List<UserEntity>>> =
            userDao.getUsersByName(listName).map { Result.Success(it) }
        emitSource(dataReturned)
    }

    fun getFavoriteUsers(): LiveData<List<UserEntity>> {
        return userDao.getFavoriteUsers()
    }

    suspend fun setFavorites(user: UserEntity, isFavorite: Boolean) {
        user.favorite = isFavorite
        userDao.updateFavorite(user)
    }

    fun getFavoriteDataUserByUsername(username: String): LiveData<UserEntity> {
        return userDao.getFavoriteDataUserByUsername(username)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao)
            }.also { instance = it }
    }
}