package com.dicoding.aplikasigithub.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.aplikasigithub.R
import com.dicoding.aplikasigithub.adapter.PagerAdapter
import com.dicoding.aplikasigithub.databinding.ActivityDetailBinding
import com.dicoding.aplikasigithub.model.DetailViewModel
import com.dicoding.aplikasigithub.model.SettingViewModel
import com.dicoding.aplikasigithub.model.ThemeViewModelFactory
import com.dicoding.aplikasigithub.response.DetailUserResponse
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    companion object {
        const val EXTRA_USER = "extra_user"

        private val TAB_CODES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
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

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)

        val userLogin = intent.getStringExtra(EXTRA_USER)
        binding.tvName.text = userLogin

        val pagerAdapter = PagerAdapter(this)
        pagerAdapter.username = userLogin.toString()

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = pagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_CODES[position])
        }.attach()

        if (userLogin != null) {
            showLoading(true)
            viewModel.getUserDetail(userLogin)
            showLoading(false)
        }
        viewModel.detailUser.observe(this, {detailUser ->
            setDetailUser(detailUser)
        })
        viewModel.isLoadingg.observe(this) {
            showLoading(it)
        }
    }

    private fun setDetailUser(User: DetailUserResponse?) {
        Glide.with(this@DetailActivity)
            .load(User?.avatarUrl)
            .into(binding.avatarPhoto)
        binding.tvUsername.text = User?.login
        binding.tvName.text = User?.name
        binding.tvFollowerCount.text = User?.followers.toString()
        binding.tvFollowingCount.text = User?.following.toString()
    }

    private fun showLoading(isLoadingg: Boolean) {
        if (isLoadingg) {
            binding.progressBar.visibility = View.VISIBLE
        }
        else {
            binding.progressBar.visibility = View.GONE
        }
    }
}