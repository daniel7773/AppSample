package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumListUseCase @Inject constructor(private val albumsRepository: AlbumsRepository) {

    suspend fun getAlbumList(userId: Int): Flow<Resource<List<Album>?>> {
        return albumsRepository.getAlbumList(userId)
    }
}