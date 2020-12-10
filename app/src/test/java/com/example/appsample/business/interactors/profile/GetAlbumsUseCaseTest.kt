package com.example.appsample.business.interactors.profile

import com.example.appsample.business.data.network.FakeJsonPlaceHolderApiSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.AlbumEntityToAlbumMapper
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.implementation.AlbumsRepositoryImpl
import junit.framework.Assert
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

@InternalCoroutinesApi
class GetAlbumsUseCaseTest {

    private val albumsRepository: AlbumsRepository
    private val networkApi: JsonPlaceholderApiSource

    init {
        networkApi = FakeJsonPlaceHolderApiSource()
        albumsRepository = AlbumsRepositoryImpl(networkApi)
    }

    @Test
    fun getAlbums_success() = runBlocking {

        val userId = 1
        val networkValue = networkApi.getAlbumsFromUser(userId).await()
        val repositoryValue = albumsRepository.getAlbums(userId)

        Assert.assertEquals(AlbumEntityToAlbumMapper.map(networkValue!!), repositoryValue.data)
    }

}