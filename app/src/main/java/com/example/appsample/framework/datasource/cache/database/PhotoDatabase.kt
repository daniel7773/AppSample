package com.example.appsample.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appsample.framework.datasource.cache.model.PhotoCacheEntity

@Database(entities = [PhotoCacheEntity::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {
        val DATABASE_NAME: String = "photo_db"
    }
}