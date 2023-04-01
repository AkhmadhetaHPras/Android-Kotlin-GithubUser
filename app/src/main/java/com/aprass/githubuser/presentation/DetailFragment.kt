package com.aprass.githubuser.presentation

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.aprass.githubuser.R
import com.aprass.githubuser.adapter.SectionsPagerAdapter
import com.aprass.githubuser.dataStore
import com.aprass.githubuser.databinding.FragmentDetailBinding
import com.aprass.githubuser.presentation.favorite.FavoriteViewModel
import com.aprass.githubuser.source.Result
import com.aprass.githubuser.source.local.entity.UserEntity
import com.aprass.githubuser.source.network.model.User
import com.aprass.githubuser.utils.UIState
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

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
        val user: UserEntity? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(USER, UserEntity::class.java)
        } else {
            arguments?.getParcelable(USER)
        }
        sectionsPagerAdapter.username = user?.username ?: getString(R.string.default_username)

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity(), requireActivity().dataStore)
        val viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        val favViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        viewModel.getProfile(user?.username ?: getString(R.string.default_username))
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            UIState.showLoading(true, requireView().findViewById(R.id.progressBar))
                        }
                        is Result.Success -> {
                            UIState.showLoading(false, requireView().findViewById(R.id.progressBar))
                            setProfile(result.data)
                        }
                        is Result.Error -> {
                            UIState.showLoading(false, requireView().findViewById(R.id.progressBar))
                            Toast.makeText(
                                context,
                                "Terjadi kesalahan" + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

        viewModel.isFavorite(user?.username ?: getString(R.string.default_username))
            .observe(viewLifecycleOwner) { result ->
                if (result.favorite) {
                    view.findViewById<FloatingActionButton>(R.id.fav_detail).apply {
                        imageTintList= ColorStateList.valueOf(Color.parseColor("#D26161"))
                        setOnClickListener{
                            favViewModel.updateFavorite(result, false)
                        }
                    }
                } else {
                    view.findViewById<FloatingActionButton>(R.id.fav_detail).apply {
                        imageTintList= ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                        setOnClickListener{
                            favViewModel.updateFavorite(result, true)
                        }
                    }
                }
            }

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
        const val USER = "user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }
}