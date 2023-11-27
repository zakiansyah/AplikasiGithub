package com.dicoding.aplikasigithub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MyFavorite")
data class FavoriteUser (
    @field:PrimaryKey
    @field:ColumnInfo(name = "username")
    var username: String = "",
    @field:ColumnInfo(name = "avatarUrl" )
    var avatarUrl: String? = null,
    @field:ColumnInfo(name = "favorite" )
    var isFavorite: Boolean
)