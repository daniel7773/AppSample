package com.example.appsample.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appsample.framework.datasource.cache.model.UserCacheEntity

@Database(entities = [UserCacheEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        val DATABASE_NAME: String = "user_db"
    }
}