package com.dicoding.aplikasigithub.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.aplikasigithub.ui.ThemePreference
import kotlinx.coroutines.launch


class SettingViewModel(private val pref : ThemePreference) : ViewModel(){

    fun getThemeSettings() : LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSettings(isDarkModeActive : Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}