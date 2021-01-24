package com.example.appsample.business.data.cache.implementation

import com.example.appsample.business.data.cache.abstraction.PhotoCacheDataSource
import com.example.appsample.business.data.models.PhotoEntity
import com.example.appsample.framework.datasource.cache.abstraction.PhotoDaoService
import javax.inject.Inject


class PhotoCacheDataSourceImpl
@Inject
constructor(
    private val photoDaoService: PhotoDaoService
) : PhotoCacheDataSource {

    override suspend fun insertPhoto(photo: PhotoEntity): Long {
        return photoDaoService.insertPhoto(photo)
    }

    override suspend fun insertPhotoList(photos: List<PhotoEntity>): LongArray {
        return photoDaoService.insertPhotoList(photos)
    }

    override suspend fun deletePhoto(id: Int): Int {
        return photoDaoService.deletePhoto(id)
    }

    override suspend fun deletePhotos(photos: List<PhotoEntity>): Int {
        return photoDaoService.deletePhotos(photos)
    }

    override suspend fun updatePhoto(photoEntity: PhotoEntity, timestamp: String?): Int {
        return photoDaoService.updatePhoto(
            photoEntity.id ?: 0,
            photoEntity.albumId ?: 0,
            photoEntity.title ?: "",
            photoEntity.url ?: "",
            photoEntity.thumbnailUrl ?: "",
            timestamp
        )
    }

    override suspend fun searchPhotos(query: String, page: Int): List<PhotoEntity> {
        return photoDaoService.searchPhotos(query, page) ?: emptyList()
    }

    override suspend fun getAllPhotos(albumId: Int): List<PhotoEntity> {
        return photoDaoService.getAllPhotos(albumId)
    }

    override suspend fun searchPhotoById(id: Int): PhotoEntity? {
        return photoDaoService.searchPhotoById(id)
    }

    override suspend fun getNumPhotos(albumId: Int): Int {
        return photoDaoService.getNumPhotos(albumId)
    }
}






