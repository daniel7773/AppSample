package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.network.FORCE_GET_EXCEPTION
import com.example.appsample.business.data.network.FORCE_GET_TIMEOUT_EXCEPTION
import com.example.appsample.business.data.network.FakeJsonPlaceHolderApiSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.AlbumEntityToAlbumMapper
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.implementation.AlbumsRepositoryImpl
import junit.framework.Assert
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

@InternalCoroutinesApi
class AlbumsRepositoryTest {

    // system in test
    private val albumsRepository: AlbumsRepository

    // resources needed
    val networkApi: JsonPlaceholderApiSource = FakeJsonPlaceHolderApiSource()

    init {
        albumsRepository = AlbumsRepositoryImpl(networkApi)
    }

    @Test
    fun getAlbums_success() = runBlocking {

        val userId = 1
        val networkValue = networkApi.getAlbumsFromUser(userId).await()
        val repositoryValue = albumsRepository.getAlbums(userId)

        Assert.assertEquals(AlbumEntityToAlbumMapper.map(networkValue!!), repositoryValue.data)
    }

    @Test
    fun getAlbums_passedErrorCorrectly() = runBlocking {

        val userId = FORCE_GET_EXCEPTION

        var exception: Exception = Exception("Any")
        val repositoryValue = albumsRepository.getAlbums(userId)
        try {
            networkApi.getAlbumsFromUser(userId).await()
        } catch (e: Exception) {
            exception = e
        }

        Assert.assertEquals(exception.javaClass, repositoryValue.exception?.javaClass)
        Assert.assertEquals(exception.message, repositoryValue.exception?.message)
        Assert.assertEquals(exception.localizedMessage, repositoryValue.exception?.localizedMessage)
    }

    @Test
    fun getAlbums_timeoutCheck() = runBlocking {

        val userId = FORCE_GET_TIMEOUT_EXCEPTION

        val repositoryValue = albumsRepository.getAlbums(userId)

        Assert.assertNotNull(repositoryValue.exception)
        Assert.assertEquals(
            TimeoutCancellationException::class.java,
            repositoryValue.exception!!.javaClass
        )
    }
}