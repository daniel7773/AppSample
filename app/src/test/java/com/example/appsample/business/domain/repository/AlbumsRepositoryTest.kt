package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.network.FORCE_GET_EXCEPTION
import com.example.appsample.business.data.network.FORCE_GET_TIMEOUT_EXCEPTION
import com.example.appsample.business.data.network.FakeJsonPlaceHolderApiSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.AlbumEntityToAlbumMapper
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.implementation.AlbumsRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
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
    fun `getAlbums Success`() = runBlockingTest {

        val userId = 1
        val networkValue = networkApi.getAlbumsFromUser(userId).await()
        val repositoryValue = albumsRepository.getAlbums(userId)

        Assertions.assertThat(AlbumEntityToAlbumMapper.map(networkValue!!))
            .isEqualTo(repositoryValue.data)
    }

    @Test
    fun `getAlbums Error passes through repository`() = runBlockingTest {

        val userId = FORCE_GET_EXCEPTION

        var exception: Exception = Exception("Any")
        val repositoryValue = albumsRepository.getAlbums(userId)
        try {
            networkApi.getAlbumsFromUser(userId).await()
        } catch (e: Exception) {
            exception = e
        }

        Assertions.assertThat(repositoryValue.exception).isInstanceOf(exception::class.java)
        Assertions.assertThat(exception.message).isEqualTo(repositoryValue.exception?.message)
        Assertions.assertThat(exception.localizedMessage)
            .isEqualTo(repositoryValue.exception?.localizedMessage)
    }

    @Test
    fun `getAlbums TimeoutCancellationException passes through repository`() = runBlockingTest {

        val userId = FORCE_GET_TIMEOUT_EXCEPTION

        val repositoryValue = albumsRepository.getAlbums(userId)

        Assertions.assertThat(repositoryValue.exception).isNotNull
        Assertions.assertThat(repositoryValue.exception)
            .isInstanceOf(TimeoutCancellationException::class.java)
    }
}