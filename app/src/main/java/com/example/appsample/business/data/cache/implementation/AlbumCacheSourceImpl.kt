package com.example.appsample.business.data.cache.implementation

import com.example.appsample.business.data.cache.abstraction.AlbumCacheSource
import com.example.appsample.business.domain.model.Album
import com.example.appsample.framework.datasource.cache.abstraction.AlbumDaoService
import javax.inject.Inject


class AlbumCacheSourceImpl
@Inject
constructor(
    private val albumDaoService: AlbumDaoService
) : AlbumCacheSource {

    override suspend fun insertAlbum(album: Album): Long {
        return albumDaoService.insertAlbum(album)
    }

    override suspend fun insertAlbumList(albums: List<Album>): LongArray {
        return albumDaoService.insertAlbumList(albums)
    }

    override suspend fun deleteAlbum(id: Int): Int {
        return albumDaoService.deleteAlbum(id)
    }

    override suspend fun deleteAlbums(albums: List<Album>): Int {
        return albumDaoService.deleteAlbums(albums)
    }

    override suspend fun updateAlbum(album: Album, timestamp: String?): Int {
        return albumDaoService.updateAlbum(
            album.id ?: 0,
            album.userId ?: 0,
            album.title ?: "",
            timestamp
        )
    }

    override suspend fun searchAlbums(query: String, page: Int): List<Album> {
        return albumDaoService.searchAlbums(query, page) ?: emptyList()
    }

    override suspend fun getAllAlbums(userId: Int): List<Album> {
        return albumDaoService.getAllAlbums(userId)
    }

    override suspend fun searchAlbumById(id: Int): Album? {
        return albumDaoService.searchAlbumById(id)
    }

    override suspend fun getNumAlbums(userId: Int): Int {
        return albumDaoService.getNumAlbums(userId)
    }
}






