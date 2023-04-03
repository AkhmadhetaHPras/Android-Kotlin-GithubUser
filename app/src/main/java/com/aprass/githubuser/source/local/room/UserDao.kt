package com.aprass.githubuser.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aprass.githubuser.source.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users order by login")
    fun getUser(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users where login LIKE '%'|| :keyword || '%'")
    fun getSearch(keyword:String ) : LiveData<List<UserEntity>>

    @Query("SELECT * FROM users WHERE login = :username")
    fun getUserProfile(username : String) : UserEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateFavorite(user: UserEntity)

    @Query("DELETE FROM users WHERE favorite = 0")
    suspend fun deleteNoFavorite()

    @Query("SELECT * FROM users where favorite = 1")
    fun getFavoriteUsers(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM users WHERE login = :username AND favorite = 1)")
    suspend fun isFavoriteUser(username: String): Boolean

    @Query("SELECT * FROM users WHERE login IN (:names) order by login")
    fun getUsersByName(names: List<String>): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users WHERE login = :username")
    fun getFavoriteDataUserByUsername(username: String) : LiveData<UserEntity>
}