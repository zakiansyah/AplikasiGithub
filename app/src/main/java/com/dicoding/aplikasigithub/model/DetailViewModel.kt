package com.dicoding.aplikasigithub.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aplikasigithub.response.DetailUserResponse
import com.dicoding.aplikasigithub.response.ItemsItem
import com.dicoding.aplikasigithub.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser : LiveData<DetailUserResponse> = _detailUser

    private val _isLoadingg = MutableLiveData<Boolean>()
    val isLoadingg : LiveData<Boolean> = _isLoadingg

    private val _isLoadingFollow = MutableLiveData<Boolean>()
    val isLoadingFollow : LiveData<Boolean> = _isLoadingFollow

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers : LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following : LiveData<List<ItemsItem>> = _following

    companion object {
        const val TAG = "DetailUserViewModel"
    }

    init {
        getUserDetail()
    }

    fun getUserDetail(query: String = "") {
        _isLoadingg.value = true
        val client = ApiConfig.getApiService().getDetailUser(query)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                _isLoadingg.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoadingg.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowers(query: String = "") {
        _isLoadingFollow.value = true
        val client = ApiConfig.getApiService().getFollowers(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoadingFollow.value = true
                if (response.isSuccessful) {
                    _followers.value = response.body()
                }
                else
                {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollow.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(query: String = "") {
        _isLoadingFollow.value = true
        val client = ApiConfig.getApiService().getFollowing(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoadingFollow.value = false
                if (response.isSuccessful) {
                    _following.value = response.body()
                }
                else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollow.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}