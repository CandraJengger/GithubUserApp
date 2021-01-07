package com.jengger.githubuserapp.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jengger.githubuserapp.CustomOnItemClickListener
import com.jengger.githubuserapp.Model.UserItems
import com.jengger.githubuserapp.R
import com.jengger.githubuserapp.databinding.UserItemsBinding

class FavoriteAdapter (private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>() {
    var listNotes = ArrayList<UserItems>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listNotes.clear()
            }
            this.listNotes.addAll(listNotes)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_items, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }
    override fun getItemCount(): Int = this.listNotes.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemsBinding.bind(itemView)
        fun bind (user: UserItems) {
            binding.tvUsername.text = user.username
            binding.tvLocation.text = user.location
            binding.tvRepo.text = user.repository
            Glide.with(itemView)
                    .load(user.photo)
                    .into(binding.imgPhoto)


            itemView.setOnClickListener (CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                override fun onItemClicked(view: View, position: Int) {
                    Snackbar.make(view, "Kenek", Snackbar.LENGTH_LONG).show()

                }
            }))
        }
    }

    fun addItem(note: UserItems) {
        this.listNotes.add(note)
        notifyItemInserted(this.listNotes.size - 1)
    }
    fun updateItem(position: Int, note: UserItems) {
        this.listNotes[position] = note
        notifyItemChanged(position, note)
    }
    fun removeItem(position: Int) {
        this.listNotes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNotes.size)
    }
}