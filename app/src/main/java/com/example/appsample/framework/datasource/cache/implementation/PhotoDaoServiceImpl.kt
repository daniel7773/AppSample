package com.example.appsample.framework.datasource.cache.implementation

import com.example.appsample.business.domain.model.Photo
import com.example.appsample.framework.datasource.cache.abstraction.PhotoDaoService
import com.example.appsample.framework.datasource.cache.database.PhotoDao
import com.example.appsample.framework.datasource.cache.mappers.PhotoCacheMapper
import javax.inject.Inject

class PhotoDaoServiceImpl
@Inject
constructor(
    private val photoDao: PhotoDao,
    private val photoCacheMapper: PhotoCacheMapper
) : PhotoDaoService {
    override suspend fun insertPhoto(photoData: Photo): Long {
        return photoDao.insertPhoto(photoCacheMapper.mapToCacheEntity(photoData))
    }

    override suspend fun insertPhotoList(photoList: List<Photo>): LongArray {
        return photoDao.insertPhotos(photoCacheMapper.listToCacheEntityList(photoList))
    }

    override suspend fun searchPhotoById(id: Int) = photoDao.searchPhotoById(id)?.run {
        photoCacheMapper.mapFromCacheEntity(this)
    }

    override suspend fun searchPhotos(query: String, page: Int) =
        photoCacheMapper.cacheEntityListToList(photoDao.searchPhotos(query, page))

    override suspend fun updatePhoto(
        id: Int,
        album_id: Int,
        title: String,
        url: String,
        thumbnail_url: String,
        timestamp: String?
    ): Int {
        return photoDao.updatePhoto(id, album_id, title, url, thumbnail_url, "NOW")
    }

    override suspend fun deletePhoto(id: Int): Int {
        return photoDao.deletePhoto(id)
    }

    override suspend fun deletePhotos(photos: List<Photo>): Int {
        val ids = photos.mapIndexed { index, value -> value.id ?: 0 }
        return photoDao.deletePhotos(ids)
    }

    override suspend fun getAllPhotos(albumId: Int): List<Photo> {
        return photoCacheMapper.cacheEntityListToList(photoDao.getAllPhotos(albumId))
    }

    override suspend fun getNumPhotos(albumId: Int): Int {
        return photoDao.getNumPhotos(albumId)
    }
}