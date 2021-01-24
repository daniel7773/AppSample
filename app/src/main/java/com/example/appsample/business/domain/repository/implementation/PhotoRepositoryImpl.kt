package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.PhotoCacheDataSource
import com.example.appsample.business.data.models.PhotoEntity
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PhotoEntityToPhotoMapper
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.repository.NetworkBoundResource
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class PhotoRepositoryImpl @Inject constructor(
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val photoCacheDataSource: PhotoCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource
) : PhotoRepository {

    private val TAG = "PhotoRepositoryImpl"

    override suspend fun getAlbumPhotoList(albumId: Int): Flow<Resource<List<Photo>?>> {

        return object : NetworkBoundResource<List<PhotoEntity>, List<Photo>>(
            { photoCacheDataSource.getAllPhotos(albumId) },
            { jsonPlaceholderApiSource.getAlbumPhotosAsync(albumId).await() },
        ) {
            override suspend fun updateCache(entity: List<PhotoEntity>) {
                Log.d(TAG, "updateCache called for photoList with size: ${entity.size}")
                photoCacheDataSource.insertPhotoList(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here, now it is fake condition here since it's sample,
            //      we know it should be 50 photos in each album
            override suspend fun shouldFetch(entity: List<Photo>?) = entity?.size != 50
            override suspend fun map(entity: List<PhotoEntity>) =
                PhotoEntityToPhotoMapper.mapPhotoList(entity)
        }.result
    }

    override suspend fun getPhotoById(albumId: Int, photoId: Int): Flow<Resource<Photo?>> {
        /** Since it's sample app and we are working not with a real data
         *  and we have no option change server logic or to get photo by photoId without some hacking
         *  I believe it's ok to make albumId and photoId to photoId transformation
         *  because we are sure what data is on server:
         *  https://jsonplaceholder.typicode.com/photos
         */
        val hackPhotoId = (albumId - 1) * 50 + photoId

        return object : NetworkBoundResource<PhotoEntity, Photo>(
            { withContext(ioDispatcher) { photoCacheDataSource.searchPhotoById(hackPhotoId) } },
            {
                withContext(ioDispatcher) {
                    jsonPlaceholderApiSource.getPhotoByIdAsync(hackPhotoId).await()
                }
            },
        ) {
            override suspend fun updateCache(entity: PhotoEntity) {
                Log.d(TAG, "updateCache called for photo with id: ${entity.id}")
                photoCacheDataSource.insertPhoto(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here, now it is fake condition here since it's sample,
            //         we know photos are not updating on server
            override suspend fun shouldFetch(entity: Photo?) = entity == null

            override suspend fun map(entity: PhotoEntity) =
                PhotoEntityToPhotoMapper.mapPhoto(entity)
        }.result
    }
}