package com.aprass.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aprass.githubuser.databinding.ItemUserBinding
import com.aprass.githubuser.source.data.networking.model.UserItem
import com.bumptech.glide.Glide

class RecyclerViewAdapter(private val listUser: List<UserItem>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(username: String)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.itemView.context)
            .load(listUser[position].avatar)
            .into(holder.binding.imgItemImage)
        holder.binding.tvUsername.text = listUser[position].username
        holder.binding.tvName.text = listUser[position].name
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition].username) }
    }
}