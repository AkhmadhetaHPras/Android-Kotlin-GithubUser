package com.aprass.githubuser.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprass.githubuser.R
import com.aprass.githubuser.adapter.RecyclerViewAdapter
import com.aprass.githubuser.dataStore
import com.aprass.githubuser.databinding.FragmentUserListBinding
import com.aprass.githubuser.presentation.favorite.FavoriteViewModel
import com.aprass.githubuser.source.Result
import com.aprass.githubuser.utils.UIState

class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private lateinit var viewModel: DetailViewModel

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

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity(), requireActivity().dataStore)
        viewModel = ViewModelProvider(requireActivity(), factory)[DetailViewModel::class.java]

        val favViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        val userAdapter = RecyclerViewAdapter(this) { user ->
            if (user.favorite) {
                favViewModel.updateFavorite(user, false)
            } else {
                favViewModel.updateFavorite(user, true)
            }
        }

        when (index) {
            1 -> {
                viewModel.getFollowers(username ?: "").observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                UIState.showLoading(true, binding.progressBar)
                            }
                            is Result.Success -> {
                                UIState.showLoading(false, binding.progressBar)
                                if (result.data.isEmpty()) {
                                    binding.textInformation.text = getString(R.string.no_followers)
                                    binding.rvConnection.visibility = View.GONE
                                    binding.textInformation.visibility = View.VISIBLE
                                } else {
                                    binding.textInformation.visibility = View.GONE
                                    binding.rvConnection.visibility = View.VISIBLE

                                    Log.d("listname", "${result.data}")
                                    userAdapter.submitList(result.data)
                                }
                            }
                            is Result.Error -> {
                                UIState.showLoading(false, binding.progressBar)
                                Toast.makeText(
                                    context,
                                    "Terjadi kesalahan" + result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
            2 -> {
                viewModel.getFollowing(username ?: "").observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                UIState.showLoading(true, binding.progressBar)
                            }
                            is Result.Success -> {
                                UIState.showLoading(false, binding.progressBar)
                                if (result.data.isEmpty()) {
                                    binding.textInformation.text = getString(R.string.no_following)
                                    binding.rvConnection.visibility = View.GONE
                                    binding.textInformation.visibility = View.VISIBLE
                                } else {
                                    binding.textInformation.visibility = View.GONE
                                    binding.rvConnection.visibility = View.VISIBLE
                                    userAdapter.submitList(result.data)
                                }
                            }
                            is Result.Error -> {
                                UIState.showLoading(false, binding.progressBar)
                                Toast.makeText(
                                    context,
                                    "Terjadi kesalahan" + result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        binding.rvConnection.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    LinearLayoutManager(context).orientation
                )
            )
            adapter = userAdapter
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }

}