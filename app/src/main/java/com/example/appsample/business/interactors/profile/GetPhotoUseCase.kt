package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import javax.inject.Inject

class GetPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend fun getPhoto(albumId: Int, photoId: Int) =
        photoRepository.getPhotoById(albumId, photoId)
}