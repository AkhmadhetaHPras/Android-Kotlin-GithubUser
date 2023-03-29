package com.aprass.githubuser.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprass.githubuser.R
import com.aprass.githubuser.databinding.FragmentHomeBinding
import com.aprass.githubuser.utils.UIState

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: HomeViewModel by viewModels()

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

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvGithubUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvGithubUser.addItemDecoration(itemDecoration)

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            UIState.showLoading(it, view.findViewById(R.id.progressBar))
        }

        mainViewModel.listGithubUser.observe(viewLifecycleOwner) { githubUser ->
            if (githubUser.isEmpty()) {
                binding.apply {
                    errorResult.text = getString(R.string.no_data)
                    rvGithubUser.visibility = View.GONE
                    errorResult.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    errorResult.visibility = View.GONE
                    rvGithubUser.visibility = View.VISIBLE
                }
                UIState.setUserData(githubUser, binding.rvGithubUser, this)
            }
        }

        binding.apply {
            svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        mainViewModel.searchGithubUser(query)
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
    }
}