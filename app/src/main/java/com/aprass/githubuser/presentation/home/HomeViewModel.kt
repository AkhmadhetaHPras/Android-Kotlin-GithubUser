package com.aprass.githubuser.presentation.home

import androidx.lifecycle.ViewModel
import com.aprass.githubuser.source.UserRepository

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun searchGithubUser(username: String) = userRepository.searchUserGithub(username)

}