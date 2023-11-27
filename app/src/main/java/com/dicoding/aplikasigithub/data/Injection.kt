package com.dicoding.aplikasigithub.data

import android.content.Context
import com.dicoding.aplikasigithub.database.FavoriteDatabase
import com.dicoding.aplikasigithub.retrofit.ApiConfig
import com.dicoding.aplikasigithub.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.favoriteDao()
        val appExecutors = AppExecutors()
        return FavoriteRepository.getInstance(apiService, dao, appExecutors)
    }
}