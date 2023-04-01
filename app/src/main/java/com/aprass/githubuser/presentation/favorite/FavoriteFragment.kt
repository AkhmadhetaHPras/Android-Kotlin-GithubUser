package com.aprass.githubuser.presentation.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprass.githubuser.R
import com.aprass.githubuser.adapter.RecyclerViewAdapter
import com.aprass.githubuser.dataStore
import com.aprass.githubuser.databinding.FragmentFavoriteBinding
import com.aprass.githubuser.presentation.ViewModelFactory
import com.aprass.githubuser.utils.UIState

class FavoriteFragment : Fragment() {
    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity(), requireActivity().dataStore)
        val viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        val favViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        val userAdapter = RecyclerViewAdapter(this) { user ->
            if (user.favorite) {
                favViewModel.updateFavorite(user, false)
            } else {
                favViewModel.updateFavorite(user, true)
            }
        }

        viewModel.getFavoriteUser().observe(viewLifecycleOwner) { favUsers ->
            UIState.showLoading(false, requireView().findViewById(R.id.progressBar))
            if (favUsers.isEmpty()) {
                binding.apply {
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
                userAdapter.submitList(favUsers)
            }
        }

        binding.rvGithubUser.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(requireActivity(), LinearLayoutManager(context).orientation))
            adapter = userAdapter
        }
    }

}