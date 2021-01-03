package com.jengger.githubuserapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jengger.githubuserapp.Adapter.UserAdapter
import com.jengger.githubuserapp.R
import com.jengger.githubuserapp.ViewModel.FollowingViewModel
import com.jengger.githubuserapp.databinding.FragmentFollowingBinding

class FollowingFragment() : Fragment() {

    companion object {
        private const val ARG_USERNAME = "arg_username"

        @JvmStatic
        fun newInstance(username: String?) = FollowingFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_USERNAME, username)
            }
        }
    }

    private lateinit var adapter: UserAdapter
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowing.layoutManager = LinearLayoutManager(context)
        binding.rvFollowing.adapter = adapter


        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        followingViewModel.setUsername(view, username.toString())

        showLoading(true)
        followingViewModel.getFollowing().observe(viewLifecycleOwner, { followingItem ->
            if (followingItem != null) {
                adapter.setData(followingItem)
                showRecyclerView(true)
                showTextNull(false)
                showLoading(false)
            } else {
                showRecyclerView(false)
                showTextNull(true)
                showLoading(false)
            }
        })
    }

    private fun showRecyclerView(state: Boolean) {
        if (state) {
            binding.rvFollowing.visibility = View.VISIBLE
        } else {
            binding.rvFollowing.visibility = View.GONE
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showTextNull(state: Boolean) {
        if (state) {
            binding.hasNoFollowing.visibility = View.VISIBLE
        } else {
            binding.hasNoFollowing.visibility = View.GONE
        }
    }
}