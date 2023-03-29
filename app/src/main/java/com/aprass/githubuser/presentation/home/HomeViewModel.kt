package com.aprass.githubuser.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aprass.githubuser.source.networking.ApiConfig
import com.aprass.githubuser.source.networking.model.GithubUserResponse
import com.aprass.githubuser.source.networking.model.User
import com.aprass.githubuser.source.networking.model.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _listGithubUser = MutableLiveData<List<UserItem>>()
    val listGithubUser: LiveData<List<UserItem>> = _listGithubUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        searchGithubUser(USERNAME)
    }

    fun searchGithubUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchGithubUser(username)
        client.enqueue(object : Callback<GithubUserResponse> {
            override fun onResponse(
                call: Call<GithubUserResponse>,
                response: Response<GithubUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listGithubUser.value = responseBody.items

                        val updatedList = mutableListOf<UserItem>()
                        for (userItem in _listGithubUser.value.orEmpty()) {
                            getName(userItem.username) { name ->
                                val updatedUserItem = if (name != null) {
                                    userItem.copy(name = name)
                                } else {
                                    userItem.copy(name = "Unknown")
                                }
                                updatedList.add(updatedUserItem)
                                if (updatedList.size == _listGithubUser.value?.size) {
                                    _listGithubUser.value = updatedList
                                }
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
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
        private const val TAG = "MainViewModel"
        private const val USERNAME = "akhmad"
    }

}