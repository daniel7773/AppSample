package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.repository.Resource

interface AlbumsRepository {

    suspend fun getAlbumList(userId: Int): Resource<List<Album>?>
}