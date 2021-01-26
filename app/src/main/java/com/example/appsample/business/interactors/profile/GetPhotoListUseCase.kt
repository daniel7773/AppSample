package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotoListUseCase @Inject constructor(private val photoRepository: PhotoRepository) {

    suspend fun getPhotoList(albumId: Int): Flow<List<Photo>?> {
        return photoRepository.getAlbumPhotoList(albumId)
    }
}