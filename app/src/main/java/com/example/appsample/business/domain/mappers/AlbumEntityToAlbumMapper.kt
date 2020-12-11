package com.example.appsample.business.domain.mappers

import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.domain.model.Album

object AlbumEntityToAlbumMapper {

    fun map(albumEntity: List<AlbumEntity>) = albumEntity.map { createAlbum(it) }

    private fun createAlbum(albumEntity: AlbumEntity) = Album(
        userId = albumEntity.userId,
        id = albumEntity.id,
        title = albumEntity.title
    )
}