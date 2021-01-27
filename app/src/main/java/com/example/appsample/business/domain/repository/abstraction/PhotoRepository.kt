package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {

    suspend fun getAlbumPhotoList(albumId: Int): Flow<List<Photo>?>

    suspend fun getPhotoById(albumId: Int, photoId: Int): Flow<Photo?>

    suspend fun getPhotoByIdSuspend(albumId: Int, photoId: Int): Photo?
}