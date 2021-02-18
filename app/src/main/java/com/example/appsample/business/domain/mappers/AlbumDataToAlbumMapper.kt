package com.example.appsample.business.domain.mappers

import com.example.appsample.business.data.models.AlbumData
import com.example.appsample.business.domain.model.Album

object AlbumDataToAlbumMapper {

    fun mapList(albumDataList: List<AlbumData>) = albumDataList.map { createAlbum(it) }

    private fun createAlbum(albumData: AlbumData) = Album(
        userId = albumData.userId,
        id = albumData.id,
        title = albumData.title
    )
}