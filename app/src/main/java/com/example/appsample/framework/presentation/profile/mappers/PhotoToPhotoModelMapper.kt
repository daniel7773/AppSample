package com.example.appsample.framework.presentation.profile.mappers

import com.example.appsample.business.domain.model.Photo
import com.example.appsample.framework.presentation.profile.models.PhotoModel

object PhotoToPhotoModelMapper {

    fun mapPhoto(photo: Photo?) = photo?.run { createPhoto(photo) }

    fun mapPhotoList(photoList: List<Photo>) = photoList.map { createPhoto(it) }

    private fun createPhoto(photo: Photo) = PhotoModel(
        albumId = photo.albumId,
        id = photo.id,
        title = photo.title,
        url = photo.url,
        thumbnailUrl = photo.thumbnailUrl,
    )
}