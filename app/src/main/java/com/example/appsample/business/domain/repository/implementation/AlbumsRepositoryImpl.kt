package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.AlbumCacheSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.AlbumDataToAlbumMapper
import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Named

class AlbumsRepositoryImpl @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val albumCacheSource: AlbumCacheSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
    private val photoRepository: PhotoRepository
) : AlbumsRepository {

    private val TAG = AlbumsRepositoryImpl::class.java.simpleName

    override suspend fun getAlbumList(userId: Int): Flow<DataState<List<Album>?>> = flow {
        val cache: List<Album>? = getCacheData(userId)

        if (!cache.isNullOrEmpty()) {
            emit(DataState.Loading<List<Album>?>(cache, "Data from cache"))
        } else {
            Log.d(TAG, "Value from cache is empty")
        }

        val networkData = getNetworkData(cache, userId)
        if (!networkData.data.isNullOrEmpty()) {
            albumCacheSource.insertAlbumList(networkData.data!!)
        }
        emit(networkData)
    }.flowOn(ioDispatcher)

    private suspend fun getNetworkData(cacheValue: List<Album>?, userId: Int): DataState<List<Album>?> {
        try {
            val albumList = withTimeout(15000L) {
                jsonPlaceholderApiSource.getAlbumsFromUserAsync(userId).await()
            }
            return if (!albumList.isNullOrEmpty()) {
                val albumList = AlbumDataToAlbumMapper.mapList(albumList)

                DataState.Success(addFirstPhotos(albumList))
            } else {
                DataState.Error(null, "NULL came from network", NullPointerException())
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting albumList from network")
            }
            e.printStackTrace()
            return DataState.Error(addFirstPhotos(cacheValue), "Can't get value from network", e)
        }
    }

    private suspend fun getCacheData(userId: Int): List<Album>? {
        var albumList: List<Album>?
        try {
            withTimeout(3000L) {
                albumList = albumCacheSource.getAllAlbums(userId)
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting albumList from cache")
            }
            e.printStackTrace()
            albumList = null
        }
        return albumList
    }

    private fun addFirstPhotos(albumList: List<Album>?) = runBlocking(ioDispatcher) {
        try {
            albumList?.map { album ->
                async {
                    album.firstPhoto = photoRepository.getPhotoByIdSuspend(album.id ?: 0, 1)
                }
            }?.map {
                it.await()
            }
            joinAll()
            albumList
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Get error while adding first photos")
            e.printStackTrace()
            albumList
        }
    }

}