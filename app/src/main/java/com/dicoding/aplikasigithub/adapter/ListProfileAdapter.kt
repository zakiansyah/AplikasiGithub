package com.dicoding.aplikasigithub.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aplikasigithub.R
import com.dicoding.aplikasigithub.database.FavoriteUser
import com.dicoding.aplikasigithub.databinding.ItemUserBinding
import com.dicoding.aplikasigithub.ui.DetailActivity


class ListProfileAdapter(private val onFavoriteClick : (FavoriteUser)->Unit) : ListAdapter<FavoriteUser, ListProfileAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        val ivAdd = holder.binding.ivAdd
        if (user.isFavorite){
            ivAdd.setImageDrawable(ContextCompat.getDrawable(ivAdd.context, R.drawable.ic_heart_red))
        }else{
            ivAdd.setImageDrawable(ContextCompat.getDrawable(ivAdd.context, R.drawable.ic_heart_grey))
        }
        ivAdd.setOnClickListener{
            notifyItemChanged(position)
            onFavoriteClick(user)
        }
    }

    class MyViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listUser: FavoriteUser) {
            binding.tvUser.text = listUser.username
            Glide.with(itemView)
                .load(listUser.avatarUrl)
                .into(binding.imgUser)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, listUser.username)
                itemView.context.startActivity(intent)
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUser>() {
            override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }
        }
    }

}