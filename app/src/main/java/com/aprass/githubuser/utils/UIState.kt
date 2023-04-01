package com.aprass.githubuser.utils

import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintSet
import com.aprass.githubuser.R
import com.aprass.githubuser.databinding.ActivityMainBinding
import com.aprass.githubuser.presentation.settings.SettingViewModel

object UIState {
    fun showLoading(isLoading: Boolean, progressBar: ProgressBar) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    fun observeTheme(viewModel: SettingViewModel, owner: AppCompatActivity) {
        viewModel.getThemeSettings().observe(owner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}