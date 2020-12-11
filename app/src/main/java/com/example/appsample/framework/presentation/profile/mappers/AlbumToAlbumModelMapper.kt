package com.example.appsample.framework.presentation.profile.mappers

import com.example.appsample.business.domain.model.Album
import com.example.appsample.framework.presentation.profile.models.AlbumModel

object AlbumToAlbumModelMapper {

    fun map(postEntity: List<Album>) = postEntity.map { createAlbum(it) }

    private fun createAlbum(album: Album) = AlbumModel(
        userId = album.userId,
        id = album.id,
        title = album.title
    )
}