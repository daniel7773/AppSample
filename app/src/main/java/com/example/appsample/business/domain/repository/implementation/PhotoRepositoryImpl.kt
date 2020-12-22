package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.models.PhotoEntity
import com.example.appsample.business.data.network.abstraction.GET_ALBUMS_TIMEOUT
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PhotoEntityToPhotoMapper
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
) : PhotoRepository {

    override suspend fun getAlbumPhotoList(albumId: Int): Resource<List<Photo>?> {
        val photoEntityList: List<PhotoEntity>?

        try {
            photoEntityList = withTimeout(GET_ALBUMS_TIMEOUT) {
                return@withTimeout jsonPlaceholderApiSource.getAlbumPhotosAsync(albumId).await()
            }
        } catch (e: Exception) {
            Log.d("PhotoRepositoryImpl", "catch (e: Exception)")
            e.printStackTrace()
            return Resource.Error(null, "catch error while calling getPhotoById", e)
        }

        photoEntityList?.forEach {
            Log.d("PhotoRepositoryImpl", "photo url: ${it.url}")
        }

        if (photoEntityList == null) {
            Log.d("PhotoRepositoryImpl", "photoEntityList == null")
            return Resource.Error(null, "Data from repository is null", NullPointerException())
        }

        val photoList: List<Photo> = PhotoEntityToPhotoMapper.mapPhotoList(photoEntityList)

        photoList.forEach {
            Log.d("PhotoRepositoryImpl", "photo url: ${it.url}")
        }
        return Resource.Success(photoList, "Success")
    }

    override suspend fun getPhotoById(albumId: Int, photoId: Int): Resource<Photo?> {
        /** Since it's sample app and we are working not with a real data
         *  and we have no option to get photo by photoId without some hacking
         *  I believe it's ok to make albumId and photoId to photoId transformation
         *  because we are sure what data is on server:
         *  https://jsonplaceholder.typicode.com/photos
         */

        val hackPhotoId = (albumId - 1) * 50 + photoId
        val photoEntity: PhotoEntity?

        try {
            photoEntity = withTimeout(GET_ALBUMS_TIMEOUT) {
                return@withTimeout jsonPlaceholderApiSource.getPhotoByIdAsync(hackPhotoId).await()
            }
        } catch (e: Exception) {
            return Resource.Error(null, "catch error while calling getPhotoById", e)
        }

        if (photoEntity == null) {
            return Resource.Error(null, "Data from repository is null", NullPointerException())
        }

        val photo: Photo = PhotoEntityToPhotoMapper.mapPhoto(photoEntity)

        return Resource.Success(photo, "Success")
    }
}