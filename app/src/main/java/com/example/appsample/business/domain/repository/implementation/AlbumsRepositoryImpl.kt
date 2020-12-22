package com.example.appsample.business.domain.repository.implementation

import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.data.network.abstraction.GET_ALBUMS_TIMEOUT
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.AlbumEntityToAlbumMapper
import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.Resource.Error
import com.example.appsample.business.domain.repository.Resource.Success
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class AlbumsRepositoryImpl @Inject constructor(
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
) : AlbumsRepository {

    override suspend fun getAlbumList(userId: Int): Resource<List<Album>?> {

        var albumEntityList: List<AlbumEntity>? = null

        try {
            albumEntityList = withTimeout(GET_ALBUMS_TIMEOUT) {
                return@withTimeout jsonPlaceholderApiSource.getAlbumsFromUserAsync(userId).await()
            }
        } catch (e: Exception) {
            return Error(null, "Catch error while calling getAlbums", e)
        }

        if (albumEntityList == null) {
            return Error(null, "Data from repository is null", NullPointerException())
        }

        val albumList = AlbumEntityToAlbumMapper.map(albumEntityList).filter { it.id != null }

        return Success(albumList, "Success")
    }

}