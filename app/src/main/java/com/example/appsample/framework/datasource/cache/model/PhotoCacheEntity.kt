package com.example.appsample.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int? = 0,

    @ColumnInfo(name = "album_id")
    var album_id: Int? = 0,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "url")
    var url: String? = null,

    @ColumnInfo(name = "thumbnail_url")
    var thumbnail_url: String? = null,

    @ColumnInfo(name = "updated_at")
    var updated_at: String,

    @ColumnInfo(name = "created_at")
    var created_at: String
) {


    companion object {

        fun nullTitleError(): String {
            return "You must enter a name."
        }

        fun nullIdError(): String {
            return "PhotoCacheEntity object has a null id. This should not be possible. Check local database."
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PhotoCacheEntity

        if (id != other.id) return false
        if (album_id != other.album_id) return false
        if (title != other.title) return false
        if (url != other.url) return false
        if (thumbnail_url != other.thumbnail_url) return false
//        if (updated_at != other.updated_at) return false // ignore this
        if (created_at != other.created_at) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + album_id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + thumbnail_url.hashCode()
        result = 31 * result + updated_at.hashCode()
        result = 31 * result + created_at.hashCode()
        return result
    }
}