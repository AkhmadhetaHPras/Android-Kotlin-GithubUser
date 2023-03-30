package com.aprass.githubuser.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.aprass.githubuser.R
import com.aprass.githubuser.adapter.SectionsPagerAdapter
import com.aprass.githubuser.databinding.FragmentDetailBinding
import com.aprass.githubuser.source.data.networking.model.User
import com.aprass.githubuser.utils.UIState
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val username: String = arguments?.getString(USERNAME) ?: ""
        sectionsPagerAdapter.username = username

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            UIState.showLoading(it, view.findViewById(R.id.progressBar))
        }
        detailViewModel.profile.observe(viewLifecycleOwner) { profile ->
            setProfile(profile)
        }

        detailViewModel.getProfile(username)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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