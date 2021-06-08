package com.example.appsample.business.data.cache.abstraction

import com.example.appsample.business.domain.model.Album

interface AlbumCacheSource {

    suspend fun insertAlbum(album: Album): Long

    suspend fun insertAlbumList(albums: List<Album>): LongArray

    suspend fun deleteAlbum(id: Int): Int

    suspend fun deleteAlbums(albums: List<Album>): Int

    suspend fun updateAlbum(
        album: Album,
        timestamp: String?
    ): Int

    suspend fun searchAlbums(
        query: String,
        page: Int
    ): List<Album>

    suspend fun getAllAlbums(userId: Int): List<Album>

    suspend fun searchAlbumById(id: Int): Album?

    suspend fun getNumAlbums(userId: Int): Int
}






