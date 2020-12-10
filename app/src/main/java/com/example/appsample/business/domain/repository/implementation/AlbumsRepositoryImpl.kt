package com.example.appsample.business.domain.repository.implementation

import com.example.appsample.business.data.network.abstraction.GET_USER_TIMEOUT
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.AlbumEntityToAlbumMapper
import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.abstraction.Resource
import com.example.appsample.business.domain.repository.abstraction.Resource.Error
import com.example.appsample.business.domain.repository.abstraction.Resource.Success
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class AlbumsRepositoryImpl @Inject constructor(
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
) : AlbumsRepository {
    override suspend fun getAlbums(userId: Int?): Resource<List<Album>> {
        val albumEntityList = withTimeoutOrNull(GET_USER_TIMEOUT) {
            return@withTimeoutOrNull jsonPlaceholderApiSource.getAlbumsFromUser(userId ?: 0).await()
        }
        if (albumEntityList == null) {
            return Error(null, "DATA IS NULL", NullPointerException())
        }

        return Success(AlbumEntityToAlbumMapper.map(albumEntityList), null)
    }

}