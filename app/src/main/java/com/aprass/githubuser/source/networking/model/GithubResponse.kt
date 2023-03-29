package com.aprass.githubuser.source.networking.model

import com.google.gson.annotations.SerializedName

data class GithubUserResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<UserItem>
)

data class UserItem(

	@field:SerializedName("login")
	val username: String,

	@field:SerializedName("avatar_url")
	val avatar: String,

	@field:SerializedName("url")
	val url: String,

	var name: String,
)

data class User(
	@field:SerializedName("login")
	val username: String,

	@field:SerializedName("avatar_url")
	val avatar: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("following")
	val following: Int,
)
