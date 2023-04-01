package com.aprass.githubuser.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aprass.githubuser.R
import com.aprass.githubuser.databinding.ItemUserBinding
import com.aprass.githubuser.presentation.DetailFragment
import com.aprass.githubuser.presentation.UserListFragment
import com.aprass.githubuser.presentation.favorite.FavoriteFragment
import com.aprass.githubuser.presentation.home.HomeFragment
import com.aprass.githubuser.source.local.entity.UserEntity
import com.bumptech.glide.Glide

class RecyclerViewAdapter(private val parentFragment: Fragment, private val onFavClick: (UserEntity) -> Unit) :
    ListAdapter<UserEntity, RecyclerViewAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity, parentF: Fragment) {
            binding.tvUsername.text = user.username
            binding.tvName.text = user.name
            Glide.with(itemView.context)
                .load(user.avatar_url)
                .into(binding.imgItemImage)
            itemView.setOnClickListener {
                val mBundle = Bundle()
                mBundle.putParcelable(DetailFragment.USER, user)
                when (parentF) {
                    is HomeFragment -> parentF.findNavController()
                        .navigate(R.id.action_homeFragment_to_detailFragment, mBundle)
                    is UserListFragment -> parentF.findNavController()
                        .navigate(R.id.action_userListFragment_to_detailFragment, mBundle)
                    is FavoriteFragment -> parentF.findNavController()
                        .navigate(R.id.action_favFragment_to_detailFragment, mBundle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user: UserEntity = getItem(position)
        holder.bind(user, parentFragment)

        val fav = holder.binding.favorite
        if (user.favorite) {
            fav.setImageDrawable(
                ContextCompat.getDrawable(
                    fav.context,
                    R.drawable.ic_favorite_red_24
                )
            )
        } else {
            fav.setImageDrawable(
                ContextCompat.getDrawable(
                    fav.context,
                    R.drawable.ic_favorite_border_24
                )
            )
        }
        fav.setOnClickListener { onFavClick(user) }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserEntity> =
            object : DiffUtil.ItemCallback<UserEntity>() {
                override fun areItemsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser.username == newUser.username
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}