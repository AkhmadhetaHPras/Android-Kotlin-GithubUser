package com.aprass.githubuser.presentation.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aprass.githubuser.preferences.SettingPreferences
import kotlinx.coroutines.launch

class SplashScreenViewModel (private val pref: SettingPreferences) : ViewModel() {
    fun getIsFirstLaunch(): LiveData<Boolean> {
        return pref.getIsFirstLaunch().asLiveData()
    }

    fun saveFirstLaunch(firstLaunch: Boolean) {
        viewModelScope.launch {
            pref.saveFirstLaunch(firstLaunch)
        }
    }
}