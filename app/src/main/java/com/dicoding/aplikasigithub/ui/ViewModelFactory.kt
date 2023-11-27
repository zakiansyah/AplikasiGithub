package com.dicoding.aplikasigithub.ui
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.aplikasigithub.data.FavoriteRepository
import com.dicoding.aplikasigithub.data.Injection
import com.dicoding.aplikasigithub.model.MainViewModel
import com.dicoding.aplikasigithub.model.SettingViewModel

class ViewModelFactory private constructor(private val userRepository: FavoriteRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}