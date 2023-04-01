package com.aprass.githubuser.presentation.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aprass.githubuser.MainActivity
import com.aprass.githubuser.dataStore
import com.aprass.githubuser.databinding.ActivitySplashScreenBinding
import com.aprass.githubuser.presentation.ViewModelFactory
import com.aprass.githubuser.presentation.settings.SettingViewModel
import com.aprass.githubuser.utils.UIState.observeTheme

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create view binding
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(this, dataStore)
        val settingViewModel = ViewModelProvider(this, factory)[SettingViewModel::class.java]

        observeTheme(settingViewModel, this)


        val ssViewModel = ViewModelProvider(this, factory)[SplashScreenViewModel::class.java]

        ssViewModel.getIsFirstLaunch().observe(this) { isFirstLaunch: Boolean ->
            if (isFirstLaunch) {
                Handler(Looper.getMainLooper()).postDelayed({
                    ssViewModel.saveFirstLaunch(false)
                    val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    finish()
                }, SPLASH_SCREEN_DELAY)
            } else {
                val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                finish()
            }
        }
    }

    companion object {
        const val SPLASH_SCREEN_DELAY = 4000L // 4 seconds
    }
}