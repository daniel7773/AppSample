package com.example.appsample.framework.datasource.cache.implementation

import com.example.appsample.business.data.models.PhotoEntity
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
    override suspend fun insertPhoto(photoEntity: PhotoEntity): Long {
        return photoDao.insertPhoto(photoCacheMapper.mapToCacheEntity(photoEntity))
    }

    override suspend fun insertPhotoList(photoList: List<PhotoEntity>): LongArray {
        return photoDao.insertPhotos(photoCacheMapper.entityListToCacheEntityList(photoList))
    }

    override suspend fun searchPhotoById(id: Int) = photoDao.searchPhotoById(id)?.run {
        photoCacheMapper.mapFromCacheEntity(this)
    }

    override suspend fun searchPhotos(query: String, page: Int) =
        photoCacheMapper.cacheEntityListToEntityList(photoDao.searchPhotos(query, page))

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

    override suspend fun deletePhotos(photos: List<PhotoEntity>): Int {
        val ids = photos.mapIndexed { index, value -> value.id ?: 0 }
        return photoDao.deletePhotos(ids)
    }

    override suspend fun getAllPhotos(userId: Int): List<PhotoEntity> {
        return photoCacheMapper.cacheEntityListToEntityList(photoDao.getAllPhotos(userId))
    }

    override suspend fun getNumPhotos(userId: Int): Int {
        return photoDao.getNumPhotos(userId)
    }
}