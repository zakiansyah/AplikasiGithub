package com.dicoding.aplikasigithub.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.aplikasigithub.database.FavoriteDao
import com.dicoding.aplikasigithub.database.FavoriteUser
import com.dicoding.aplikasigithub.response.GithubUserResponse
import com.dicoding.aplikasigithub.retrofit.ApiService
import com.dicoding.aplikasigithub.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteRepository private constructor(
    private val apiService: ApiService,
    private val favoriteDao : FavoriteDao,
    private val appExecutors: AppExecutors
) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val result = MediatorLiveData<Result<List<FavoriteUser>>>()


    fun getAllUser(q : String ): LiveData<Result<List<FavoriteUser>>>{
        _isLoading.value = true
        result.value = Result.Loading
        val client = apiService.getGithubUser(q)
        client.enqueue(object : Callback<GithubUserResponse> {
            override fun onResponse(call: Call<GithubUserResponse>, response: Response<GithubUserResponse>) {
                _isLoading.value = false
                result.value = Result.Loading
                if (response.isSuccessful) {
                    val items = response.body()?.items
                    val favoriteList = ArrayList<FavoriteUser>()
                    appExecutors.diskIO.execute {
                        items?.forEach { itemsItem ->
                            val isFavorite = favoriteDao.isUserFavorite(itemsItem.login)
                            val favorite = FavoriteUser(
                                itemsItem.login,
                                itemsItem.avatarUrl,
                                isFavorite
                            )
                            favoriteList.add(favorite)
                        }
                        favoriteDao.delete()
                        favoriteDao.insert(favoriteList)
                    }
                }
            }
            override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val locaData = favoriteDao.getUser()
        result.addSource(locaData){newData :List<FavoriteUser> ->
            result.value = Result.Success(newData)
        }
        return result
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> {
        return favoriteDao.getFavoriteUser()
    }
    fun setFavoriteUser(favorite: FavoriteUser, favoriteState: Boolean) {
        appExecutors.diskIO.execute {
            favorite.isFavorite = favoriteState
            favoriteDao.updateFavorite(favorite)
        }
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteDao: FavoriteDao,
            appExecutors: AppExecutors
        ): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(apiService,favoriteDao, appExecutors)
            }.also { instance = it }
    }


}