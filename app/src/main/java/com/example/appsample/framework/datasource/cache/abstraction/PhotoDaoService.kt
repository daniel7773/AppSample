package com.example.appsample.framework.datasource.cache.abstraction

import com.example.appsample.business.data.models.PhotoEntity

interface PhotoDaoService {

    suspend fun insertPhoto(photoEntity: PhotoEntity): Long

    suspend fun insertPhotoList(photoList: List<PhotoEntity>): LongArray

    suspend fun searchPhotoById(id: Int): PhotoEntity?

    suspend fun searchPhotos(query: String, page: Int): List<PhotoEntity>?

    suspend fun updatePhoto(
        id: Int,
        album_id: Int,
        title: String,
        url: String,
        thumbnail_url: String,
        timestamp: String?
    ): Int

    suspend fun deletePhoto(id: Int): Int

    suspend fun deletePhotos(photos: List<PhotoEntity>): Int

    suspend fun getAllPhotos(albumId: Int): List<PhotoEntity>

    suspend fun getNumPhotos(albumId: Int): Int
}












