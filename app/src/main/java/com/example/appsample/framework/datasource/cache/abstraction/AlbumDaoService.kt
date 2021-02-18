package com.example.appsample.framework.datasource.cache.abstraction

import com.example.appsample.business.domain.model.Album


interface AlbumDaoService {

    suspend fun insertAlbum(albumData: Album): Long

    suspend fun insertAlbumList(albumList: List<Album>): LongArray

    suspend fun searchAlbumById(id: Int): Album?

    suspend fun searchAlbums(query: String, page: Int): List<Album>?

    suspend fun updateAlbum(
        id: Int,
        user_id: Int,
        title: String,
        timestamp: String?
    ): Int

    suspend fun deleteAlbum(id: Int): Int

    suspend fun deleteAlbums(albums: List<Album>): Int

    suspend fun getAllAlbums(userId: Int): List<Album>

    suspend fun getNumAlbums(userId: Int): Int
}












