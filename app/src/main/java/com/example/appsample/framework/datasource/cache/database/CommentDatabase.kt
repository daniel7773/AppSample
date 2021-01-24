package com.example.appsample.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appsample.framework.datasource.cache.model.CommentCacheEntity

@Database(entities = [CommentCacheEntity::class], version = 1)
abstract class CommentDatabase : RoomDatabase() {

    abstract fun commentDao(): CommentDao

    companion object {
        val DATABASE_NAME: String = "comment_db"
    }
}