package com.jengger.githubuserapp.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jengger.githubuserapp.Model.UserItems
import com.jengger.githubuserapp.R
import com.jengger.githubuserapp.UserActivity
import com.jengger.githubuserapp.databinding.UserItemsBinding

class FavoriteAdapter (private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    var listUsers = ArrayList<UserItems>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listUsers.clear()
            }
            this.listUsers.addAll(listNotes)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_items, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }
    override fun getItemCount(): Int = this.listUsers.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemsBinding.bind(itemView)
        fun bind (user: UserItems) {
            binding.tvUsername.text = user.username
            binding.tvLocation.text = user.location
            binding.tvRepo.text = user.repository
            Glide.with(itemView)
                    .load(user.photo)
                    .into(binding.imgPhoto)


            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserItems)
    }
}