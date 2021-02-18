package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotoListUseCase @Inject constructor(private val photoRepository: PhotoRepository) {

    suspend fun getPhotoList(albumId: Int): Flow<DataState<List<Photo>?>> {
        return photoRepository.getAlbumPhotoList(albumId)
    }
}