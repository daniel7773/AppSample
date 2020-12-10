package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Album

interface AlbumsRepository {

    suspend fun getAlbums(userId: Int?): Resource<List<Album>>
}