package com.aprass.githubuser.view

import android.os.Bundle
import android.text.style.TtsSpan.ARG_USERNAME
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprass.githubuser.R
import com.aprass.githubuser.databinding.FragmentUserListBinding
import com.aprass.githubuser.utils.Utils
import com.aprass.githubuser.viewmodel.DetailViewModel

class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME, getString(R.string.default_username))

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        val layoutManager = LinearLayoutManager(activity)
        binding.rvConnection.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvConnection.addItemDecoration(itemDecoration)

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            Utils.showLoading(it, binding.progressBar)
        }

        detailViewModel.listFollowers.observe(viewLifecycleOwner) { githubUser ->
            if (githubUser.isEmpty()) {
                binding.errorResult.text = "No Followers"
                binding.rvConnection.visibility = View.GONE
                binding.errorResult.visibility = View.VISIBLE
            } else {
                binding.errorResult.visibility = View.GONE
                binding.rvConnection.visibility = View.VISIBLE
                Utils.setUserData(githubUser, binding.rvConnection, requireActivity())
            }
        }

        detailViewModel.listFollowing.observe(viewLifecycleOwner) { githubUser ->
            if (githubUser.isEmpty()) {
                binding.errorResult.text = "No Following"
                binding.rvConnection.visibility = View.GONE
                binding.errorResult.visibility = View.VISIBLE
            } else {
                binding.errorResult.visibility = View.GONE
                binding.rvConnection.visibility = View.VISIBLE
                Utils.setUserData(githubUser, binding.rvConnection, requireActivity())
            }
        }

        when (index) {
            1 -> detailViewModel.getFollowers(username?:"")
            2 -> detailViewModel.getFollowing(username?:"")
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }

}