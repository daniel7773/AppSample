package com.example.appsample.business.data.cache.abstraction

import com.example.appsample.business.domain.model.Photo


interface PhotoCacheSource {

    suspend fun insertPhoto(photo: Photo): Long

    suspend fun insertPhotoList(photos: List<Photo>): LongArray

    suspend fun deletePhoto(id: Int): Int

    suspend fun deletePhotos(photos: List<Photo>): Int

    suspend fun updatePhoto(
        photoData: Photo,
        timestamp: String?
    ): Int

    suspend fun searchPhotos(
        query: String,
        page: Int
    ): List<Photo>

    suspend fun getAllPhotos(albumId: Int): List<Photo>

    suspend fun searchPhotoById(id: Int): Photo?

    suspend fun getNumPhotos(albumId: Int): Int
}






