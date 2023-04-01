package com.aprass.githubuser.presentation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aprass.githubuser.presentation.favorite.FavoriteViewModel
import com.aprass.githubuser.presentation.home.HomeViewModel
import com.aprass.githubuser.source.local.preferences.SettingPreferences
import com.aprass.githubuser.presentation.settings.SettingViewModel
import com.aprass.githubuser.presentation.splashscreen.SplashScreenViewModel
import com.aprass.githubuser.source.UserRepository
import com.aprass.githubuser.source.di.Injection

class ViewModelFactory(private val pref: SettingPreferences, private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            return SplashScreenViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE : ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context : Context, userSettings : DataStore<Preferences>) : ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = ViewModelFactory(Injection.provideUserSettings(userSettings),Injection.provideRepository(context))
                INSTANCE = instance
                instance
            }
        }

    }
}