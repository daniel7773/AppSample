package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import javax.inject.Inject

class GetPhotoListUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend fun getPhotoList(albumId: Int) = photoRepository.getAlbumPhotoList(albumId)
}