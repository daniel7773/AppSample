package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.AlbumEntityToAlbumMapper
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.implementation.AlbumsRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
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
    val networkApi: JsonPlaceholderApiSource = mockk()

    init {
        albumsRepository = AlbumsRepositoryImpl(networkApi)
    }

    @Test
    fun `getAlbums Success`() = runBlockingTest {

        val userId = 1
        coEvery {
            networkApi.getAlbumsFromUserAsync(userId).await()
        } returns DataFactory.produceListOfAlbumsEntity(3)

        val networkValue = networkApi.getAlbumsFromUserAsync(userId).await()
        val repositoryValue = albumsRepository.getAlbumList(userId)

        Assertions.assertThat(AlbumEntityToAlbumMapper.map(networkValue!!))
            .isEqualTo(repositoryValue.data)
    }

    @Test
    fun `getAlbums Error passes through repository`() = runBlockingTest {

        val userId = 1
        val exception: Exception = Exception("Any")

        coEvery { networkApi.getAlbumsFromUserAsync(userId).await() } throws exception

        val repositoryValue = albumsRepository.getAlbumList(userId)

        Assertions.assertThat(repositoryValue.exception).isInstanceOf(exception::class.java)
        Assertions.assertThat(exception.message).isEqualTo(repositoryValue.exception?.message)
        Assertions.assertThat(exception.localizedMessage)
            .isEqualTo(repositoryValue.exception?.localizedMessage)
    }

    @Test
    fun `getAlbums TimeoutCancellationException passes through repository`() = runBlockingTest {

        val userId = 1

        coEvery {
            networkApi.getAlbumsFromUserAsync(userId)
        }.answers {
            val deferredAlbumsListEntities = CompletableDeferred<List<AlbumEntity>?>()

            deferredAlbumsListEntities
        }


        val repositoryValue = albumsRepository.getAlbumList(userId)

        Assertions.assertThat(repositoryValue.exception).isNotNull
        Assertions.assertThat(repositoryValue.exception)
            .isInstanceOf(TimeoutCancellationException::class.java)
    }
}