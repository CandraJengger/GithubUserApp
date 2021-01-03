package com.jengger.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.jengger.githubuserapp.Adapter.SectionsPagesAdapter
import com.jengger.githubuserapp.Model.UserItems
import com.jengger.githubuserapp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    companion object {
        val EXTRA_USER = "extra_user"
    }
    private lateinit var binding: ActivityUserBinding


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
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


        val user = intent.getParcelableExtra<UserItems>(EXTRA_USER) as UserItems
        binding.tvName.text = user.name
        binding.tvCompany.text = user.company
        binding.tvUsername.text = user.username
        binding.tvLocation.text = user.location
        binding.tvRepo.text = user.repository
        Glide.with(this)
                .load(user.photo)
                .into(binding.imgPhotoProfile)

        val sectionsPagesAdapter = SectionsPagesAdapter(this, supportFragmentManager)
        sectionsPagesAdapter.username = user.username
        binding.viewPager.adapter = sectionsPagesAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        supportActionBar?.elevation = 0f


    }
}