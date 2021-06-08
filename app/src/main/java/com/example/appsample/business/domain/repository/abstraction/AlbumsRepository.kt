package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow

interface AlbumsRepository {

    suspend fun getAlbumList(userId: Int): Flow<DataState<List<Album>?>>
}