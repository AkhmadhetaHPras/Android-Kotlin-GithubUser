package com.aprass.githubuser.source.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.aprass.githubuser.source.UserRepository
import com.aprass.githubuser.source.local.preferences.SettingPreferences
import com.aprass.githubuser.source.network.ApiConfig
import com.dicoding.newsapp.data.local.room.UserDatabase

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val udao = database.userDao()
        return UserRepository.getInstance(apiService, udao)
    }

    fun provideUserSettings(datastore : DataStore<Preferences>) : SettingPreferences {
        return SettingPreferences.getInstance(datastore)
    }
}