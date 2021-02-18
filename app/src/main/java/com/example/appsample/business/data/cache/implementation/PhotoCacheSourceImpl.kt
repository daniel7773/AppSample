package com.example.appsample.business.data.cache.implementation

import com.example.appsample.business.data.cache.abstraction.PhotoCacheSource
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.framework.datasource.cache.abstraction.PhotoDaoService
import javax.inject.Inject


class PhotoCacheSourceImpl
@Inject
constructor(
    private val photoDaoService: PhotoDaoService
) : PhotoCacheSource {

    override suspend fun insertPhoto(photo: Photo): Long {
        return photoDaoService.insertPhoto(photo)
    }

    override suspend fun insertPhotoList(photos: List<Photo>): LongArray {
        return photoDaoService.insertPhotoList(photos)
    }

    override suspend fun deletePhoto(id: Int): Int {
        return photoDaoService.deletePhoto(id)
    }

    override suspend fun deletePhotos(photos: List<Photo>): Int {
        return photoDaoService.deletePhotos(photos)
    }

    override suspend fun updatePhoto(photo: Photo, timestamp: String?): Int {
        return photoDaoService.updatePhoto(
            photo.id ?: 0,
            photo.albumId ?: 0,
            photo.title ?: "",
            photo.url ?: "",
            photo.thumbnailUrl ?: "",
            timestamp
        )
    }

    override suspend fun searchPhotos(query: String, page: Int): List<Photo> {
        return photoDaoService.searchPhotos(query, page) ?: emptyList()
    }

    override suspend fun getAllPhotos(albumId: Int): List<Photo> {
        return photoDaoService.getAllPhotos(albumId)
    }

    override suspend fun searchPhotoById(id: Int): Photo? {
        return photoDaoService.searchPhotoById(id)
    }

    override suspend fun getNumPhotos(albumId: Int): Int {
        return photoDaoService.getNumPhotos(albumId)
    }
}






