package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val albumsRepository: AlbumsRepository
) {

    suspend fun getAlbums(userId: Int?) = albumsRepository.getAlbums(userId)
}