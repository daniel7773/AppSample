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
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val photoCacheDataSource: PhotoCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
) : PhotoRepository {

    override suspend fun getAlbumPhotoList(albumId: Int): Flow<Resource<List<Photo>?>> {

        return object : NetworkBoundResource<List<PhotoEntity>, List<Photo>>(
            mainDispatcher,
            { photoCacheDataSource.getAllPhotos(albumId) },
            { jsonPlaceholderApiSource.getAlbumPhotosAsync(albumId).await() },
        ) {
            override suspend fun updateCache(entity: List<PhotoEntity>) {
                Log.d("Asdasddwc", "updateCache called for photoList with size: ${entity.size}")
                photoCacheDataSource.insertPhotoList(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here
            override suspend fun shouldFetch(entity: List<Photo>?) = true
            override suspend fun map(entity: List<PhotoEntity>) =
                PhotoEntityToPhotoMapper.mapPhotoList(entity)
        }.result
    }

    override suspend fun getPhotoById(albumId: Int, photoId: Int): Resource<Photo?> {
        /** Since it's sample app and we are working not with a real data
         *  and we have no option change server logic or to get photo by photoId without some hacking
         *  I believe it's ok to make albumId and photoId to photoId transformation
         *  because we are sure what data is on server:
         *  https://jsonplaceholder.typicode.com/photos
         */

        val hackPhotoId = (albumId - 1) * 50 + photoId

        return object : NetworkBoundResource<PhotoEntity, Photo>(
            mainDispatcher,
            { photoCacheDataSource.searchPhotoById(hackPhotoId) },
            { jsonPlaceholderApiSource.getPhotoByIdAsync(hackPhotoId).await() },
        ) {
            override suspend fun updateCache(entity: PhotoEntity) {
                Log.d("Asdasddwc", "updateCache called for photo with id: ${entity.id}")
                photoCacheDataSource.insertPhoto(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here
            override suspend fun shouldFetch(entity: Photo?) = true

            override suspend fun map(entity: PhotoEntity) =
                PhotoEntityToPhotoMapper.mapPhoto(entity)
        }.resultSuspend()
    }
}