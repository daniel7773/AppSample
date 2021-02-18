package com.example.appsample.framework.datasource.cache.implementation

import com.example.appsample.business.domain.model.Album
import com.example.appsample.framework.datasource.cache.abstraction.AlbumDaoService
import com.example.appsample.framework.datasource.cache.database.AlbumDao
import com.example.appsample.framework.datasource.cache.mappers.AlbumCacheMapper
import javax.inject.Inject

class AlbumDaoServiceImpl
@Inject
constructor(
    private val albumDao: AlbumDao,
    private val albumCacheMapper: AlbumCacheMapper
) : AlbumDaoService {
    override suspend fun insertAlbum(albumData: Album): Long {
        return albumDao.insertAlbum(albumCacheMapper.mapToCacheEntity(albumData))
    }

    override suspend fun insertAlbumList(albumList: List<Album>): LongArray {
        return albumDao.insertAlbums(albumCacheMapper.entityListToCacheEntityList(albumList))
    }

    override suspend fun searchAlbumById(id: Int) = albumDao.searchAlbumById(id)?.run {
        albumCacheMapper.mapFromCacheEntity(this)
    }

    override suspend fun searchAlbums(query: String, page: Int) =
        albumCacheMapper.cacheEntityListToEntityList(albumDao.searchAlbums(query, page))

    override suspend fun updateAlbum(
        id: Int,
        user_id: Int,
        title: String,
        timestamp: String?
    ): Int {
        return albumDao.updateAlbum(id, user_id, title, "NOW")
    }

    override suspend fun deleteAlbum(id: Int): Int {
        return albumDao.deleteAlbum(id)
    }

    override suspend fun deleteAlbums(albums: List<Album>): Int {
        val ids = albums.mapIndexed { index, value -> value.id ?: 0 }
        return albumDao.deleteAlbums(ids)
    }

    override suspend fun getAllAlbums(userId: Int): List<Album> {
        return albumCacheMapper.cacheEntityListToEntityList(albumDao.getAllAlbums(userId))
    }

    override suspend fun getNumAlbums(userId: Int): Int {
        return albumDao.getNumAlbums(userId)
    }
}