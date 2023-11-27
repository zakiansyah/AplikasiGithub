package com.dicoding.aplikasigithub.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aplikasigithub.R
import com.dicoding.aplikasigithub.adapter.ListProfileAdapter
import com.dicoding.aplikasigithub.databinding.ActivityMainBinding
import com.dicoding.aplikasigithub.model.MainViewModel
import com.dicoding.aplikasigithub.data.Result
import com.dicoding.aplikasigithub.model.SettingViewModel
import com.dicoding.aplikasigithub.model.ThemeViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

        mainViewModel.Loading.observe(this) {
            showLoading(it)
        }

        val userAdapter = ListProfileAdapter{user ->
            if (user.isFavorite){
                mainViewModel.deleteFavorite(user)
                Toast.makeText(this, "Berhasil Menghapus ${user.username} dari favorite", Toast.LENGTH_SHORT).show()
            }else{
                mainViewModel.saveFavorite(user)
                Toast.makeText(this, "Berhasil Menambah ${user.username} ke favorite", Toast.LENGTH_SHORT).show()
            }
        }

        mainViewModel.findGithubUser("Zaki").observe(this) { listUser ->
            if (listUser != null) {
                when (listUser) {
                    is Result.Loading-> {
                        mainViewModel.Loading.observe(this){
                            showLoading(it)
                        }
                    }

                    is Result.Success -> {
                        mainViewModel.Loading.observe(this){
                            showLoading(it)
                        }
                        val userData = listUser.data
                        userAdapter.submitList(userData)

                    }

                    is Result.Error -> {
                        mainViewModel.Loading.observe(this){
                            showLoading(it)
                        }
                        Toast.makeText(
                            this,
                            "terjadi kesalahan" + listUser.error, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvListUser.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
        }

        binding.searchBar.inflateMenu(R.menu.option_menu)
        binding.searchBar.setOnMenuItemClickListener{menuItem ->
            when(menuItem.itemId){
                R.id.menu1 -> {
                    val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu2 -> {
                    val intent = Intent(this@MainActivity, ThemeActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.setOnEditorActionListener { textView, _, _ ->
            binding.searchBar.text = binding.searchView.text
            mainViewModel.findGithubUser(binding.searchView.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(textView.windowToken, 0)
            binding.searchView.hide()
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        }
        else {
            binding.progressBar.visibility = View.GONE
        }
    }

}