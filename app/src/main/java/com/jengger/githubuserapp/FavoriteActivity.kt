package com.jengger.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.jengger.githubuserapp.Adapter.FavoriteAdapter
import com.jengger.githubuserapp.Adapter.UserAdapter
import com.jengger.githubuserapp.Model.UserItems
import com.jengger.githubuserapp.databinding.ActivityFavoriteBinding
import com.jengger.githubuserapp.db.UserHelper
import com.jengger.githubuserapp.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter

    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorites"

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showRecyclerView()
    }

    override fun onResume() {
        showRecyclerView()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            android.R.id.home -> {
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)

            }
            val users = deferredNotes.await()
            if (users.size > 0) {
                adapter.listUsers = users
            } else {
                adapter.listUsers = ArrayList()
            }
        }
    }

    fun showRecyclerView() {
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.rvFavorites.adapter = adapter


        val moveWithObjectUser = Intent(this@FavoriteActivity, UserActivity::class.java)
        adapter.setOnItemClickCallback(object: FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItems) {
                moveWithObjectUser.putExtra(UserActivity.EXTRA_USER, data)
                startActivity(moveWithObjectUser)
            }
        })

        // proses ambil data
        loadNotesAsync()
    }
}