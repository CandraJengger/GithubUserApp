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
import com.jengger.githubuserapp.ViewModel.FollowersViewModel
import com.jengger.githubuserapp.databinding.FragmentFollowerBinding

class FollowerFragment : Fragment() {

    companion object {
        private const val ARG_USERNAME = "arg_username"

        @JvmStatic
        fun newInstance(username: String?) = FollowerFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_USERNAME, username)
            }
        }
    }

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var adapter: UserAdapter
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollower.layoutManager = LinearLayoutManager(context)
        binding.rvFollower.adapter = adapter

        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)
        followersViewModel.setUsername(view, username.toString())

        showLoading(true)
        followersViewModel.getFollowers().observe(viewLifecycleOwner, { followerItem ->
            if (followerItem != null) {
                adapter.setData(followerItem)
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
            binding.rvFollower.visibility = View.VISIBLE
        } else {
            binding.rvFollower.visibility = View.GONE
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
            binding.hasNoFollowers.visibility = View.VISIBLE
        } else {
            binding.hasNoFollowers.visibility = View.GONE
        }
    }
}