package com.example.appsample.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appsample.framework.datasource.cache.model.AlbumCacheEntity

@Database(entities = [AlbumCacheEntity::class], version = 1)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao

    companion object {
        val DATABASE_NAME: String = "album_db"
    }
}