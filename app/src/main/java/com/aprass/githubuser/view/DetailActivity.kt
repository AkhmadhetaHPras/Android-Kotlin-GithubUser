package com.aprass.githubuser.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.aprass.githubuser.R
import com.aprass.githubuser.adapter.SectionsPagerAdapter
import com.aprass.githubuser.databinding.ActivityDetailBinding
import com.aprass.githubuser.model.User
import com.aprass.githubuser.utils.Utils
import com.aprass.githubuser.viewmodel.DetailViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val username: String = intent.getStringExtra(USERNAME) ?: ""
        sectionsPagerAdapter.username = username

        detailViewModel.isLoading.observe(this) {
            Utils.showLoading(it, findViewById(R.id.progressBar))
        }
        detailViewModel.profile.observe(this) { profile ->
            setProfile(profile)
        }

        detailViewModel.getProfile(username)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun setProfile(profile: User) {
        val (username, avatar, name, location, followersCount, followingCount) = profile

        binding.apply {
            tvName.text = name
            tvUsername.text = username
            tvLocation.text = location
            followers.text = followersCount.toString()
            following.text = followingCount.toString()
        }

        Glide.with(this)
            .load(avatar)
            .into(binding.imgItemImage)
    }

    companion object {
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }

}