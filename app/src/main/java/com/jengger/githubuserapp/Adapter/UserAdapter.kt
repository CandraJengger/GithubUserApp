package com.jengger.githubuserapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jengger.githubuserapp.Model.UserItems
import com.jengger.githubuserapp.R
import com.jengger.githubuserapp.databinding.UserItemsBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val mData = ArrayList<UserItems>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class UserViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val binding = UserItemsBinding.bind(view)
        fun bind(user: UserItems) {
            with(itemView) {
                binding.tvUsername.text = user.username
                binding.tvLocation.text = user.location
                binding.tvRepo.text = user.repository
                Glide.with(this)
                        .load(user.photo)
                        .into(binding.imgPhoto)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.user_items, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    interface OnItemClickCallback {
        fun onItemClicked(data: UserItems)
    }
}