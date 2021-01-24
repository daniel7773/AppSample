package com.example.appsample.framework.datasource.cache.abstraction

import com.example.appsample.business.data.models.AlbumEntity

interface AlbumDaoService {

    suspend fun insertAlbum(albumEntity: AlbumEntity): Long

    suspend fun insertAlbumList(albumList: List<AlbumEntity>): LongArray

    suspend fun searchAlbumById(id: Int): AlbumEntity?

    suspend fun searchAlbums(query: String, page: Int): List<AlbumEntity>?

    suspend fun updateAlbum(
        id: Int,
        user_id: Int,
        title: String,
        timestamp: String?
    ): Int

    suspend fun deleteAlbum(id: Int): Int

    suspend fun deleteAlbums(albums: List<AlbumEntity>): Int

    suspend fun getAllAlbums(userId: Int): List<AlbumEntity>

    suspend fun getNumAlbums(userId: Int): Int
}












