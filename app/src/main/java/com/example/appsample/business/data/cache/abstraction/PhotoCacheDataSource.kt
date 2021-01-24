package com.example.appsample.business.data.cache.abstraction

import com.example.appsample.business.data.models.PhotoEntity

interface PhotoCacheDataSource {

    suspend fun insertPhoto(photo: PhotoEntity): Long

    suspend fun insertPhotoList(photos: List<PhotoEntity>): LongArray

    suspend fun deletePhoto(id: Int): Int

    suspend fun deletePhotos(photos: List<PhotoEntity>): Int

    suspend fun updatePhoto(
        photoEntity: PhotoEntity,
        timestamp: String?
    ): Int

    suspend fun searchPhotos(
        query: String,
        page: Int
    ): List<PhotoEntity>

    suspend fun getAllPhotos(albumId: Int): List<PhotoEntity>

    suspend fun searchPhotoById(id: Int): PhotoEntity?

    suspend fun getNumPhotos(albumId: Int): Int
}






