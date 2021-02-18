package com.example.appsample.framework.datasource.cache.abstraction

import com.example.appsample.business.domain.model.Photo


interface PhotoDaoService {

    suspend fun insertPhoto(photoData: Photo): Long

    suspend fun insertPhotoList(photoList: List<Photo>): LongArray

    suspend fun searchPhotoById(id: Int): Photo?

    suspend fun searchPhotos(query: String, page: Int): List<Photo>?

    suspend fun updatePhoto(
        id: Int,
        album_id: Int,
        title: String,
        url: String,
        thumbnail_url: String,
        timestamp: String?
    ): Int

    suspend fun deletePhoto(id: Int): Int

    suspend fun deletePhotos(photos: List<Photo>): Int

    suspend fun getAllPhotos(albumId: Int): List<Photo>

    suspend fun getNumPhotos(albumId: Int): Int
}












