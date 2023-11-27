package com.dicoding.aplikasigithub.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM Myfavorite ORDER BY username ASC")
    fun getUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM Myfavorite where favorite = 1")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @PrimaryKey(autoGenerate = false)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser : List<FavoriteUser>)

    @Query("DELETE FROM MyFavorite WHERE favorite = 0")
    fun delete()

    @Update
    fun updateFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM MyFavorite ORDER BY username ASC")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>>

    @Query("SELECT EXISTS(SELECT * FROM MyFavorite WHERE username = :title AND favorite = 1)")
    fun isUserFavorite(title: String): Boolean
}

