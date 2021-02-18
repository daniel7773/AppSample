package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.PhotoCacheSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PhotoDataToPhotoMapper
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Named

class PhotoRepositoryImpl @Inject constructor(
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val photoCacheSource: PhotoCacheSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource
) : PhotoRepository {

    private val TAG = "PhotoRepositoryImpl"

    override suspend fun getAlbumPhotoList(albumId: Int): Flow<DataState<List<Photo>?>> = flow {
        val cache: List<Photo>? = getPhotoListCacheData(albumId)

        if (!cache.isNullOrEmpty()) {
            emit(DataState.Loading<List<Photo>?>(cache, "Data from cache"))
        } else {
            Log.d(TAG, "Value from cache is empty")
        }

        val networkData = getPhotoListNetworkData(cache, albumId)
        if (!networkData.data.isNullOrEmpty()) {
            photoCacheSource.insertPhotoList(networkData.data!!)
        }
        emit(networkData)
    }.flowOn(ioDispatcher)

    private suspend fun getPhotoListNetworkData(cacheValue: List<Photo>?, albumId: Int): DataState<List<Photo>?> {
        try {
            val photoDataList = withTimeout(15000L) {
                jsonPlaceholderApiSource.getAlbumPhotosAsync(albumId).await()
            }
            return if (!photoDataList.isNullOrEmpty()) {
                val photoList = PhotoDataToPhotoMapper.mapPhotoList(photoDataList)
                DataState.Success(photoList)
            } else {
                DataState.Error(null, "NULL came from network", null)
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting photoList from network")
            }
            e.printStackTrace()
            return DataState.Error(cacheValue, "Can't get value from network", e)
        }
    }

    private suspend fun getPhotoListCacheData(albumId: Int): List<Photo>? {
        var photoList: List<Photo>?
        try {
            withTimeout(3000L) {
                photoList = photoCacheSource.getAllPhotos(albumId)
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting photoList from cache")
            }
            e.printStackTrace()
            photoList = null
        }
        return photoList
    }

    override suspend fun getPhotoById(albumId: Int, photoId: Int): Flow<DataState<Photo?>> {
        /** Since it's sample app and we are working not with a real data
         *  and we have no option change server logic or to get photo by photoId without some hacking
         *  I believe it's ok to make albumId and photoId to photoId transformation
         *  because we are sure what data is on server:
         *  https://jsonplaceholder.typicode.com/photos
         */
        return flowOf() // TODO: rework when this method will be needed
    }

    override suspend fun getPhotoByIdSuspend(albumId: Int, photoId: Int): Photo? {
        /** Since it's sample app and we are working not with a real data
         *  and we have no option change server logic or to get photo by photoId without some hacking
         *  I believe it's ok to make albumId and photoId to photoId transformation
         *  because we are sure what data is on server:
         *  https://jsonplaceholder.typicode.com/photos
         */
        val hackPhotoId = (albumId - 1) * 50 + photoId // USE THIS INSTEAD OF photoId for correct work, read comment above
        try {
            return getPhotoNetworkData(null, hackPhotoId).data
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Error while getting photo from network.")
            e.printStackTrace()
            val photo = getPhotoCacheData(hackPhotoId)
            if (photo == null) {
                Log.e(TAG, "Error while getting photo from cache, or empty cache.")
            }
            return photo
        }
    }

    private suspend fun getPhotoNetworkData(cacheValue: Photo?, albumId: Int): DataState<Photo?> {
        try {
            val photoData = withTimeout(15000L) {
                jsonPlaceholderApiSource.getPhotoByIdAsync(albumId).await()
            }
            return if (photoData != null) {
                val photo = PhotoDataToPhotoMapper.mapPhoto(photoData)
                DataState.Success(photo)
            } else {
                DataState.Error(null, "NULL came from network", null)
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting photo from network")
            }
            e.printStackTrace()
            return DataState.Error(cacheValue, "Can't get value from network", e)
        }
    }

    private suspend fun getPhotoCacheData(hackPhotoId: Int): Photo? {
        var photo: Photo?
        try {
            withTimeout(3000L) {
                photo = photoCacheSource.searchPhotoById(hackPhotoId)
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting photo from cache")
            }
            e.printStackTrace()
            photo = null
        }
        return photo
    }
}