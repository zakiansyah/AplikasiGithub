package com.dicoding.aplikasigithub.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aplikasigithub.R
import com.dicoding.aplikasigithub.response.ItemsItem
import com.dicoding.aplikasigithub.ui.DetailActivity

class UserAdapter(private val listUser: List<ItemsItem>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_user, viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Users = listUser[position]
        holder.tvName.text = Users.login
        Glide.with(holder.itemView.context)
            .load(Users.avatarUrl)
            .circleCrop()
            .into(holder.imgPhoto)

        holder.itemView.setOnClickListener{
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EXTRA_USER, Users.login)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount() = listUser.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvName : TextView = view.findViewById(R.id.tvUsername)
        val imgPhoto : ImageView = view.findViewById(R.id.imgUser)
    }
}