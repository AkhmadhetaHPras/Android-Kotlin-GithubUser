package com.aprass.githubuser.presentation

import androidx.lifecycle.ViewModel
import com.aprass.githubuser.source.UserRepository

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getProfile(username: String) = userRepository.getProfile(username)

    fun getFollowers(username: String) = userRepository.getFollowers(username)

    fun getFollowing(username: String) = userRepository.getFollowing(username)

    fun isFavorite(username: String) = userRepository.getFavoriteDataUserByUsername(username)
}