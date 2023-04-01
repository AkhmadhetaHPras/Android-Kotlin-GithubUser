package com.aprass.githubuser.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aprass.githubuser.source.UserRepository
import com.aprass.githubuser.source.local.entity.UserEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getFavoriteUser() = userRepository.getFavoriteUsers()

    fun updateFavorite(user: UserEntity, isFavorite: Boolean) {
        viewModelScope.launch {
            userRepository.setFavorites(user, isFavorite)
        }
    }

}