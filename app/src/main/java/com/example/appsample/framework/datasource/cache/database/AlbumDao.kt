package com.example.appsample.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appsample.framework.datasource.cache.model.AlbumCacheEntity

const val ALBUM_PAGINATION_PAGE_SIZE = 10

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: AlbumCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbums(albums: List<AlbumCacheEntity>): LongArray

    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun searchAlbumById(id: Int): AlbumCacheEntity?

    @Query(
        """
        SELECT * FROM albums 
        WHERE title LIKE '%' || :query || '%'
        ORDER BY updated_at DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchAlbums(
        query: String,
        page: Int,
        pageSize: Int = ALBUM_PAGINATION_PAGE_SIZE
    ): List<AlbumCacheEntity>

    @Query("DELETE FROM albums WHERE id IN (:ids)")
    suspend fun deleteAlbums(ids: List<Int>): Int

    @Query("DELETE FROM albums")
    suspend fun deleteAllAlbums()

    @Query("SELECT * FROM albums WHERE user_id = :user_id")
    suspend fun getAllAlbums(user_id: Int): List<AlbumCacheEntity>

    @Query(
        """
        UPDATE albums 
        SET 
        user_id = :user_id, 
        title = :title,
        updated_at = :updated_at
        WHERE id = :id
        """
    )
    suspend fun updateAlbum(
        id: Int,
        user_id: Int,
        title: String,
        updated_at: String
    ): Int

    @Query("DELETE FROM albums WHERE id = :id")
    suspend fun deleteAlbum(id: Int): Int

    @Query("SELECT COUNT(*) FROM albums WHERE id = :album_id")
    suspend fun getNumAlbums(album_id: Int): Int
}












