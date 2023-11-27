package com.dicoding.aplikasigithub.model

import androidx.lifecycle.ViewModel
import com.dicoding.aplikasigithub.data.FavoriteRepository
import com.dicoding.aplikasigithub.database.FavoriteUser

class MainViewModel(private val favoriteRepository: FavoriteRepository): ViewModel() {

    val Loading = favoriteRepository.isLoading

    fun findGithubUser(q: String = "") = favoriteRepository.getAllUser(q)

    fun getFavoriteUser() = favoriteRepository.getAllFavoriteUser()

    fun saveFavorite(favoriteUser: FavoriteUser) {
        favoriteRepository.setFavoriteUser(favoriteUser, true)
    }

    fun deleteFavorite(favoriteUser: FavoriteUser) {
        favoriteRepository.setFavoriteUser(favoriteUser, false)
    }

}