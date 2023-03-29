package com.aprass.githubuser

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aprass.githubuser.databinding.ActivityMainBinding
import com.aprass.githubuser.utils.UIState.hideBottomNav
import com.aprass.githubuser.utils.UIState.observeTheme
import com.aprass.githubuser.utils.UIState.showBottomNav
import com.google.android.material.bottomnavigation.BottomNavigationView

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeTheme(dataStore, this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingBottomNav()
    }

    private fun settingBottomNav() {
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navController.addOnDestinationChangedListener {_, destination, _ ->
            when(destination.id) {
                R.id.detailFragment -> hideBottomNav(binding)
                else -> {
                    showBottomNav(binding)
                }
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_favorite, R.id.navigation_home, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean
    {
        val navController = this.findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp()
    }
}