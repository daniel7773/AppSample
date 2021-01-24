package com.example.appsample.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appsample.framework.datasource.cache.model.PhotoCacheEntity

const val PHOTO_PAGINATION_PAGE_SIZE = 100

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: PhotoCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhotos(photos: List<PhotoCacheEntity>): LongArray

    @Query("SELECT * FROM photos WHERE id = :id")
    suspend fun searchPhotoById(id: Int): PhotoCacheEntity?

    @Query(
        """
        SELECT * FROM photos 
        WHERE title LIKE '%' || :query || '%'
        ORDER BY updated_at DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchPhotos(
        query: String,
        page: Int,
        pageSize: Int = ALBUM_PAGINATION_PAGE_SIZE
    ): List<PhotoCacheEntity>

    @Query("DELETE FROM photos WHERE id IN (:ids)")
    suspend fun deletePhotos(ids: List<Int>): Int

    @Query("DELETE FROM photos")
    suspend fun deleteAllPhotos()

    @Query("SELECT * FROM photos WHERE album_id = :album_id")
    suspend fun getAllPhotos(album_id: Int): List<PhotoCacheEntity>

    @Query(
        """
        UPDATE photos 
        SET 
        album_id = :album_id, 
        title = :title,
        url = :url,
        thumbnail_url = :thumbnail_url,
        updated_at = :updated_at
        WHERE id = :id
        """
    )
    suspend fun updatePhoto(
        id: Int,
        album_id: Int,
        title: String,
        url: String,
        thumbnail_url: String,
        updated_at: String
    ): Int

    @Query("DELETE FROM photos WHERE id = :id")
    suspend fun deletePhoto(id: Int): Int

    @Query("SELECT COUNT(*) FROM photos WHERE id = :photo_id")
    suspend fun getNumPhotos(photo_id: Int): Int
}












