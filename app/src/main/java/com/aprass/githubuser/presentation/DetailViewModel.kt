package com.aprass.githubuser.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aprass.githubuser.source.networking.ApiConfig
import com.aprass.githubuser.source.networking.model.User
import com.aprass.githubuser.source.networking.model.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _profile = MutableLiveData<User>()
    val profile: LiveData<User> = _profile

    private val _listFollowers = MutableLiveData<List<UserItem>>()
    val listFollowers: LiveData<List<UserItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<UserItem>>()
    val listFollowing: LiveData<List<UserItem>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getProfile(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserProfile(username)
        client.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _profile.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure getProfile: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure getProfile: ${t.message}")
            }
        })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserConnectionResponse(username, "followers")
        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowers.value = responseBody!!
                        val updatedList = mutableListOf<UserItem>()
                        for (userItem in _listFollowers.value.orEmpty()) {
                            getName(userItem.username) { name ->
                                val updatedUserItem = if (name != null) {
                                    userItem.copy(name = name)
                                } else {
                                    userItem.copy(name = "Unknown")
                                }
                                updatedList.add(updatedUserItem)
                                if (updatedList.size == _listFollowers.value?.size) {
                                    _listFollowers.value = updatedList
                                }
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure getFollowers: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure getFollowers: ${t.message}")
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserConnectionResponse(username, "following")
        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowing.value = responseBody!!
                        val updatedList = mutableListOf<UserItem>()
                        for (userItem in _listFollowing.value.orEmpty()) {
                            getName(userItem.username) { name ->
                                val updatedUserItem = if (name != null) {
                                    userItem.copy(name = name)
                                } else {
                                    userItem.copy(name = "Unknown")
                                }
                                updatedList.add(updatedUserItem)
                                if (updatedList.size == _listFollowing.value?.size) {
                                    _listFollowing.value = updatedList
                                }
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure getFollowing: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure getFollowing: ${t.message}")
            }
        })
    }

    fun getName(username: String, callback: (String?) -> Unit) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserProfile(username)
        client.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback(responseBody.name)
                    }
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}