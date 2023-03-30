package com.aprass.githubuser.utils

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintSet
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aprass.githubuser.R
import com.aprass.githubuser.adapter.RecyclerViewAdapter
import com.aprass.githubuser.databinding.ActivityMainBinding
import com.aprass.githubuser.preferences.SettingPreferences
import com.aprass.githubuser.source.data.networking.model.UserItem
import com.aprass.githubuser.presentation.DetailFragment
import com.aprass.githubuser.presentation.UserListFragment
import com.aprass.githubuser.presentation.home.HomeFragment
import com.aprass.githubuser.presentation.settings.SettingViewModel
import com.aprass.githubuser.presentation.ViewModelFactory

object UIState {
    fun showLoading(isLoading: Boolean, progressBar: ProgressBar) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    fun setUserData(githubUser: List<UserItem>, recyclerView: RecyclerView, view: Fragment) {
        val adapter = RecyclerViewAdapter(githubUser)
        recyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object : RecyclerViewAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                val mBundle = Bundle()
                mBundle.putString(DetailFragment.USERNAME, username)
                when (view) {
                    is HomeFragment -> view.findNavController()
                        .navigate(R.id.action_homeFragment_to_detailFragment, mBundle)
                    is UserListFragment -> view.findNavController()
                        .navigate(R.id.action_userListFragment_to_detailFragment, mBundle)
                }
            }
        })
    }

    fun updateBottomNav(binding: ActivityMainBinding, isShown: Boolean) {
        binding.navView.visibility = if (isShown) View.VISIBLE else View.INVISIBLE
        val constraintLayout = binding.parentLayout
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.nav_host_fragment_activity_main,
            ConstraintSet.BOTTOM, if (isShown) R.id.nav_view else R.id.parentLayout,
            if (isShown) ConstraintSet.TOP else ConstraintSet.BOTTOM, 0
        )
        constraintSet.applyTo(constraintLayout)
    }

    fun observeTheme(dataStore: DataStore<Preferences>, owner: AppCompatActivity) {
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(owner, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(owner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}