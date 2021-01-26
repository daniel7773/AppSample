package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.AlbumCacheDataSource
import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.AlbumEntityToAlbumMapper
import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.repository.NetworkBoundResource
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import java.util.concurrent.*
import javax.inject.Inject
import javax.inject.Named

class AlbumsRepositoryImpl @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val albumCacheDataSource: AlbumCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
    private val photoRepository: PhotoRepository
) : AlbumsRepository {

    private val TAG = "AlbumsRepositoryImpl"

    override suspend fun getAlbumList(userId: Int): Flow<List<Album>?> {
        return object : NetworkBoundResource<List<AlbumEntity>, List<Album>>(
            { albumCacheDataSource.getAllAlbums(userId) },
            { jsonPlaceholderApiSource.getAlbumsFromUserAsync(userId).await() },
        ) {
            override suspend fun updateCache(entity: List<AlbumEntity>) {
                Log.d(TAG, "updateCache called for albumList with size: ${entity.size}")
                albumCacheDataSource.insertAlbumList(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here, now it is fake condition here since it's sample,
            //  we know each user got 10 albums on server
            override suspend fun shouldFetch(entity: List<Album>?) = entity?.size != 10
            override suspend fun map(entity: List<AlbumEntity>) =
                AlbumEntityToAlbumMapper.mapList(entity)
        }.result.transform { albumList ->

            albumList?.forEach { album ->
                album.id?.run {
                    val photoFlow = withContext(ioDispatcher) {
                        photoRepository.getPhotoById(this@run, 1)
                    }
                    /**
                     * @see PhotoRepositoryImpl.getPhotoById()
                     * explanation why we sending hardcoded 1
                     */

                    photoFlow.collect { photo ->
                        album.firstPhoto = photo
                    }
                }
            }

            emit(albumList)
        }
    }

}