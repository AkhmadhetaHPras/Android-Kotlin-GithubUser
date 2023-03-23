package com.aprass.githubuser.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.aprass.githubuser.adapter.RecyclerViewAdapter
import com.aprass.githubuser.model.UserItem
import com.aprass.githubuser.view.DetailActivity

object Utils {
    fun showLoading(isLoading: Boolean, progressBar: ProgressBar) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun setUserData(githubUser: List<UserItem>, recyclerView: RecyclerView, context: Context) {
        val adapter = RecyclerViewAdapter(githubUser)
        recyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object : RecyclerViewAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                val intentToDetail = Intent(context, DetailActivity::class.java)
                intentToDetail.putExtra(DetailActivity.USERNAME, username.lowercase())
                context.startActivity(intentToDetail)
            }
        })
    }

}