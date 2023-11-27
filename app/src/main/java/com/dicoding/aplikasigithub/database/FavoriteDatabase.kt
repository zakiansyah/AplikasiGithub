package com.dicoding.aplikasigithub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteUser::class], version = 1)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

        companion object {
            @Volatile
            private var INSTANCE: FavoriteDatabase? = null

            @JvmStatic
            fun getInstance(context: Context): FavoriteDatabase {
                if (INSTANCE == null) {
                    synchronized(FavoriteDatabase::class.java) {
                        INSTANCE =
                            Room.databaseBuilder(
                                context.applicationContext,
                                FavoriteDatabase::class.java, "favorite_user_database"
                            )
                                .build()
                    }
                }
                return INSTANCE as FavoriteDatabase
            }
        }
    }
