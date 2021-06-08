package com.example.appsample.business.domain.mappers

import com.example.appsample.business.data.models.PhotoData
import com.example.appsample.business.domain.model.Photo

object PhotoDataToPhotoMapper {

    fun mapPhoto(photoData: PhotoData) = createPhoto(photoData)

    fun mapPhotoList(photoDataList: List<PhotoData>) = photoDataList.map { createPhoto(it) }

    private fun createPhoto(photoData: PhotoData) = Photo(
        albumId = photoData.albumId,
        id = photoData.id,
        title = photoData.title,
        url = photoData.url,
        thumbnailUrl = photoData.thumbnailUrl,
    )
}