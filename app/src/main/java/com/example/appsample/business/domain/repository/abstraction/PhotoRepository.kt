package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {

    suspend fun getAlbumPhotoList(albumId: Int): Flow<DataState<List<Photo>?>>

    suspend fun getPhotoById(albumId: Int, photoId: Int): Flow<DataState<Photo?>>

    suspend fun getPhotoByIdSuspend(albumId: Int, photoId: Int): Photo?
}