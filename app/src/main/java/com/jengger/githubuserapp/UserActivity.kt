package com.jengger.githubuserapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jengger.githubuserapp.Adapter.SectionsPagesAdapter
import com.jengger.githubuserapp.Model.UserItems
import com.jengger.githubuserapp.databinding.ActivityUserBinding
import com.jengger.githubuserapp.db.DatabaseContract
import com.jengger.githubuserapp.db.UserHelper
import com.jengger.githubuserapp.helper.MappingHelper

class UserActivity : AppCompatActivity(), View.OnClickListener {
    private var user: UserItems? = null
    private var username: String? = null
    private lateinit var userHelper: UserHelper

    companion object {
        const val EXTRA_USER = "extra_user"
    }
    private lateinit var binding: ActivityUserBinding


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        user = intent.getParcelableExtra(EXTRA_USER)

        user?.let {
            binding.tvName.text = it.name
            binding.tvCompany.text = it.company
            binding.tvUsername.text = it.username
            binding.tvLocation.text = it.location
            binding.tvRepo.text = it.repository
            Glide.with(this)
                    .load(it.photo)
                    .into(binding.imgPhotoProfile)

            username = it.username
        }

        val sectionsPagesAdapter = SectionsPagesAdapter(this, supportFragmentManager)
        sectionsPagesAdapter.username = username
        binding.viewPager.adapter = sectionsPagesAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        supportActionBar?.elevation = 0f

        binding.fabFavorite.setOnClickListener(this)

        if (userHelper.queryByUsername(user?.username.toString()).count > 0) {
            setFavoriteButton(true)
        } else {
            setFavoriteButton(false)
        }
    }

    private fun setFavoriteButton(state: Boolean) {
        if (state) {
            binding.fabFavorite.contentDescription = getString(R.string.favorite)
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.fabFavorite.contentDescription = getString(R.string.cancel_favorite)
            binding.fabFavorite.setImageResource(R.drawable.ic_unfavorite)
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.fab_favorite) {
            val username = binding.tvUsername.text.toString()
            val name = binding.tvName.text.toString()
            val location = binding.tvLocation.text.toString()
            val repository = binding.tvRepo.text.toString()
            val company = binding.tvCompany.text.toString()
            val imgUser = user?.photo
            val following = user?.following
            val followers = user?.followers
            val urlFollowing = user?.urlFollowing
            val urlFollowers = user?.urlFollowers


            val values = ContentValues()
            values.put(DatabaseContract.UserColumns.USERNAME, username)
            values.put(DatabaseContract.UserColumns.NAME, name)
            values.put(DatabaseContract.UserColumns.COMPANY, company)
            values.put(DatabaseContract.UserColumns.LOCATION, location)
            values.put(DatabaseContract.UserColumns.REPO, repository)
            values.put(DatabaseContract.UserColumns.IMAGE_USER, imgUser)
            values.put(DatabaseContract.UserColumns.FOLLOWERS, followers)
            values.put(DatabaseContract.UserColumns.FOLLOWING, following)
            values.put(DatabaseContract.UserColumns.URL_FOLLOWING, urlFollowing)
            values.put(DatabaseContract.UserColumns.URL_FOLLOWERS, urlFollowers)

            if (userHelper.queryByUsername(username).count > 0) {
                val cursor = userHelper.queryByUsername(username)
                val cursorArray = MappingHelper.mapCursorToArrayList(cursor)

                val result = userHelper.deleteByUsername(cursorArray[0].username).toLong()
                if (result > 0) {

                    showSnackBarMessage(getString(R.string.remove_favorite))
                }

                setFavoriteButton(false)
            } else {
                val result = userHelper.insert(values)
                showSnackBarMessage(getString(R.string.added_favorite))

                if (result > 0) {
                    setFavoriteButton(true)
                } else {
                    Toast.makeText(this@UserActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show()
                    setFavoriteButton(false)
                }

            }
        }
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}