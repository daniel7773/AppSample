package com.example.appsample.business.domain.mappers

import com.example.appsample.business.data.models.PhotoEntity
import com.example.appsample.business.domain.model.Photo

object PhotoEntityToPhotoMapper {

    fun mapPhoto(photoEntity: PhotoEntity) = createPhoto(photoEntity)

    fun mapPhotoList(photoEntityList: List<PhotoEntity>) = photoEntityList.map { createPhoto(it) }

    private fun createPhoto(photoEntity: PhotoEntity) = Photo(
        albumId = photoEntity.albumId,
        id = photoEntity.id,
        title = photoEntity.title,
        url = photoEntity.url,
        thumbnailUrl = photoEntity.thumbnailUrl,
    )
}