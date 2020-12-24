package com.example.appsample.business.data.cache.implementation

import com.example.appsample.business.data.cache.abstraction.AlbumCacheDataSource
import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.framework.datasource.cache.abstraction.AlbumDaoService
import javax.inject.Inject


class AlbumCacheDataSourceImpl
@Inject
constructor(
    private val albumDaoService: AlbumDaoService
) : AlbumCacheDataSource {

    override suspend fun insertAlbum(album: AlbumEntity): Long {
        return albumDaoService.insertAlbum(album)
    }

    override suspend fun insertAlbumList(albums: List<AlbumEntity>): LongArray {
        return albumDaoService.insertAlbumList(albums)
    }

    override suspend fun deleteAlbum(id: Int): Int {
        return albumDaoService.deleteAlbum(id)
    }

    override suspend fun deleteAlbums(albums: List<AlbumEntity>): Int {
        return albumDaoService.deleteAlbums(albums)
    }

    override suspend fun updateAlbum(albumEntity: AlbumEntity, timestamp: String?): Int {
        return albumDaoService.updateAlbum(
            albumEntity.id ?: 0,
            albumEntity.userId ?: 0,
            albumEntity.title ?: "",
            timestamp
        )
    }

    override suspend fun searchAlbums(query: String, page: Int): List<AlbumEntity> {
        return albumDaoService.searchAlbums(query, page) ?: emptyList()
    }

    override suspend fun getAllAlbums(userId: Int): List<AlbumEntity> {
        return albumDaoService.getAllAlbums(userId)
    }

    override suspend fun searchAlbumById(id: Int): AlbumEntity? {
        return albumDaoService.searchAlbumById(id)
    }

    override suspend fun getNumAlbums(userId: Int): Int {
        return albumDaoService.getNumAlbums(userId)
    }
}






