package com.aprass.githubuser.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprass.githubuser.R
import com.aprass.githubuser.adapter.RecyclerViewAdapter
import com.aprass.githubuser.dataStore
import com.aprass.githubuser.databinding.FragmentHomeBinding
import com.aprass.githubuser.presentation.ViewModelFactory
import com.aprass.githubuser.presentation.favorite.FavoriteViewModel
import com.aprass.githubuser.utils.UIState
import com.aprass.githubuser.source.Result


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity(), requireActivity().dataStore)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        val favViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        val userAdapter = RecyclerViewAdapter(this) { user ->
            if (user.favorite) {
                favViewModel.updateFavorite(user, false)
            } else {
                favViewModel.updateFavorite(user, true)
            }
        }

        binding.apply {
            svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        searchUser(query, userAdapter)
                        svUser.clearFocus()
                        return true
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        binding.rvGithubUser.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(requireActivity(), LinearLayoutManager(context).orientation))
            adapter = userAdapter
        }
    }

    fun searchUser(username: String, adapter: RecyclerViewAdapter) {
        viewModel.searchGithubUser(username).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        UIState.showLoading(true, requireView().findViewById(R.id.progressBar))
                    }
                    is Result.Success -> {
                        UIState.showLoading(false, requireView().findViewById(R.id.progressBar))
                        if (result.data.isEmpty()) {
                            binding.apply {
                                textInformation.text = getString(R.string.no_data)
                                rvGithubUser.visibility = View.GONE
                                img.visibility = View.VISIBLE
                                textInformation.visibility = View.VISIBLE
                            }
                        } else {
                            binding.apply {
                                textInformation.visibility = View.GONE
                                img.visibility = View.GONE
                                rvGithubUser.visibility = View.VISIBLE
                            }
                            adapter.submitList(result.data)
                        }
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
    }
}