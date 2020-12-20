package com.example.appsample.framework.presentation.profile.mappers

import com.example.appsample.business.domain.model.Album
import com.example.appsample.framework.presentation.profile.models.AlbumModel

object AlbumToAlbumModelMapper {

    fun map(albumList: List<Album>) = albumList.map { createAlbum(it) }

    private fun createAlbum(album: Album) = AlbumModel(
        userId = album.userId,
        id = album.id,
        title = album.title,
        firstPhoto = PhotoToPhotoModelMapper.mapPhoto(album.firstPhoto)
    )
}