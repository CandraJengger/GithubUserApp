package com.jengger.githubuserapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jengger.githubuserapp.Adapter.UserAdapter
import com.jengger.githubuserapp.Model.UserItems
import com.jengger.githubuserapp.ViewModel.MainViewModel
import com.jengger.githubuserapp.databinding.ActivityMainBinding
import com.jengger.githubuserapp.fragments.SettingFragment

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> {
//                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//                startActivity(mIntent)
                val settingsFragment = SettingFragment()

                val mFragmentManager = supportFragmentManager
                settingsFragment.show(mFragmentManager, SettingFragment::class.java.simpleName)
                return true
            }
            R.id.favorite_page -> {
                val mIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(mIntent)
                return true
            }
            else -> return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showRecyclerView(false)
                showBanner(false)
                showLoading(true)
                mainViewModel.setSearchUser(binding.root, query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("onQueryTextChange", newText)
                return false
            }

        })

        mainViewModel.getSearchUsers().observe(this, { userItems ->
            if (userItems != null) {
                Log.d("User", userItems[0].name)
                adapter.setData(userItems)
                showRecyclerView(true)
                showLoading(false)
                showBanner(false)
            } else {
                showRecyclerView(false)
                showLoading(false)
                showBanner(true)
            }
        })

        val moveWithObjectUser = Intent(this@MainActivity, UserActivity::class.java)
        adapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItems) {
                moveWithObjectUser.putExtra(UserActivity.EXTRA_USER, data)
                startActivity(moveWithObjectUser)
            }
        })

    }



    private fun showRecyclerView(state: Boolean) {
        if (state) {
            binding.recyclerView.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.GONE
        }
    }

    private fun showBanner(state: Boolean) {
        if (state) {
            binding.blankBanner.visibility = View.VISIBLE
        } else {
            binding.blankBanner.visibility = View.GONE
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}