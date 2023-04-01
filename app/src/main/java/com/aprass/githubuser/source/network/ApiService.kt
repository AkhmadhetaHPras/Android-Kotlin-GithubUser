package com.aprass.githubuser.source.network

import com.aprass.githubuser.source.network.model.GithubUserResponse
import com.aprass.githubuser.source.network.model.User
import com.aprass.githubuser.source.network.model.UserItem
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    suspend fun searchGithubUser(
        @Query("q") username: String
    ): GithubUserResponse

    @GET("users/{$USERNAME_PARAM}")
    suspend fun getUserProfile(
        @Path(USERNAME_PARAM) username: String
    ): User

    @GET("users/{$USERNAME_PARAM}/{endpoint}")
    suspend fun getUserConnectionResponse(
        @Path(USERNAME_PARAM) username: String,
        @Path("endpoint") endpoint: String
    ): List<UserItem>

    companion object {
        const val USERNAME_PARAM = "username"
    }
}
