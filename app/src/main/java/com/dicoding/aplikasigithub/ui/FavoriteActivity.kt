package com.dicoding.aplikasigithub.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aplikasigithub.R
import com.dicoding.aplikasigithub.adapter.ListProfileAdapter
import com.dicoding.aplikasigithub.databinding.ActivityFavoriteBinding
import com.dicoding.aplikasigithub.databinding.FragmentFollowBinding
import com.dicoding.aplikasigithub.model.MainViewModel
import com.dicoding.aplikasigithub.model.SettingViewModel
import com.dicoding.aplikasigithub.model.ThemeViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = ThemePreference.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val mainViewModel: MainViewModel by viewModels {
            factory
        }
        val userAdapter = ListProfileAdapter { user ->
            if (user.isFavorite) {
                mainViewModel.deleteFavorite(user)
                Toast.makeText(this, "Berhasil Menghapus", Toast.LENGTH_SHORT).show()
            } else {
                mainViewModel.saveFavorite(user)
                Toast.makeText(this, "Berhasil Menambah", Toast.LENGTH_SHORT).show()
            }
        }


        mainViewModel.getFavoriteUser().observe(this) { favoritedList ->
            userAdapter.submitList(favoritedList)
        }
        binding.rvListFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
        }

        binding.btnBack.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}
