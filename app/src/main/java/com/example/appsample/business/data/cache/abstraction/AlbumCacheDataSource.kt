package com.example.appsample.business.data.cache.abstraction

import com.example.appsample.business.data.models.AlbumEntity

interface AlbumCacheDataSource {

    suspend fun insertAlbum(album: AlbumEntity): Long

    suspend fun insertAlbumList(albums: List<AlbumEntity>): LongArray

    suspend fun deleteAlbum(id: Int): Int

    suspend fun deleteAlbums(albums: List<AlbumEntity>): Int

    suspend fun updateAlbum(
        albumEntity: AlbumEntity,
        timestamp: String?
    ): Int

    suspend fun searchAlbums(
        query: String,
        page: Int
    ): List<AlbumEntity>

    suspend fun getAllAlbums(userId: Int): List<AlbumEntity>

    suspend fun searchAlbumById(id: Int): AlbumEntity?

    suspend fun getNumAlbums(userId: Int): Int
}






