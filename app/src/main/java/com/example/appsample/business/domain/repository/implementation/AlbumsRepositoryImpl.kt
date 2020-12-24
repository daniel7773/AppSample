package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.AlbumCacheDataSource
import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.AlbumEntityToAlbumMapper
import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.repository.NetworkBoundResource
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AlbumsRepositoryImpl @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val albumCacheDataSource: AlbumCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
    private val photoRepository: PhotoRepository
) : AlbumsRepository {

    override suspend fun getAlbumList(userId: Int): Resource<List<Album>?> {
        val resourceList = object : NetworkBoundResource<List<AlbumEntity>, List<Album>>(
            mainDispatcher,
            { albumCacheDataSource.getAllAlbums(userId) },
            { jsonPlaceholderApiSource.getAlbumsFromUserAsync(userId).await() },
        ) {
            override suspend fun updateCache(entity: List<AlbumEntity>) {
                Log.d("Asdasddwc", "updateCache called for albumList with size: ${entity.size}")
                albumCacheDataSource.insertAlbumList(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here
            override suspend fun shouldFetch(entity: List<Album>?) = true
            override suspend fun map(entity: List<AlbumEntity>) =
                AlbumEntityToAlbumMapper.mapList(entity)
        }.resultSuspend()

        if (resourceList is Resource.Success) {
            resourceList.data?.forEach { album ->
                album.id?.run {  // explanation why we sending hardcoded 1 in photoRepository
                    val commentsPostResponse = photoRepository.getPhotoById(this, 1)

                    if (commentsPostResponse is Resource.Success) {
                        album.firstPhoto = commentsPostResponse.data
                    }
                }
            }
        }
        return resourceList
    }

}