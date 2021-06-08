package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumListUseCase @Inject constructor(private val albumsRepository: AlbumsRepository) {

    suspend fun getAlbumList(userId: Int): Flow<DataState<List<Album>?>> {
        return albumsRepository.getAlbumList(userId)
    }
}