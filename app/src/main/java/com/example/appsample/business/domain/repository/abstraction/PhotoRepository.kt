package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.repository.Resource
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {

    suspend fun getAlbumPhotoList(albumId: Int): Flow<Resource<List<Photo>?>>

    suspend fun getPhotoById(albumId: Int, photoId: Int): Flow<Resource<Photo?>>
}