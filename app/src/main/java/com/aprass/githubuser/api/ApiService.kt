package com.aprass.githubuser.api

import com.aprass.githubuser.model.GithubUserResponse
import com.aprass.githubuser.model.User
import com.aprass.githubuser.model.UserItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

//    dont
//    @GET("search/users/") karena akan digenerate backend retrofit
    @GET("search/users")
    fun searchGithubUser(
        @Query("q") username: String
    ): Call<GithubUserResponse>

    @GET("users/{$USERNAME_PARAM}")
    fun getUserProfile(
        @Path(USERNAME_PARAM) username: String
    ): Call<User>

    @GET("users/{$USERNAME_PARAM}/{endpoint}")
    fun getUserConnectionResponse(
        @Path(USERNAME_PARAM) username: String,
        @Path("endpoint") endpoint: String
    ): Call<List<UserItem>>

    companion object {
        const val USERNAME_PARAM = "username"
    }
}
