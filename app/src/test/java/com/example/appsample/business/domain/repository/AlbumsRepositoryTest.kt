package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.cache.abstraction.AlbumCacheSource
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.AlbumDataToAlbumMapper
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import com.example.appsample.business.domain.repository.implementation.AlbumsRepositoryImpl
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.rules.InstantExecutorExtension
import com.example.appsample.rules.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class AlbumsRepositoryTest {

    // system in test
    private val albumsRepository: AlbumsRepository

    // resources needed
    @get:Extensions
    val mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()

    val networkApi: JsonPlaceholderApiSource = mockk()
    val albumCacheSource: AlbumCacheSource = mockk()
    val photoRepository: PhotoRepository = mockk()

    init {
        albumsRepository = AlbumsRepositoryImpl(
            mainDispatcher = mainCoroutineRule.testDispatcher,
            ioDispatcher = mainCoroutineRule.testDispatcher,
            albumCacheSource = albumCacheSource,
            jsonPlaceholderApiSource = networkApi,
            photoRepository = photoRepository
        )

        coEvery {
            albumCacheSource.insertAlbum(any())
        } returns 1L

        coEvery {
            albumCacheSource.insertAlbumList(any())
        } returns LongArray(1)
    }

    @Test
    fun `getAlbums Success`() = runBlockingTest {

        val userId = 1
        val albumNum = 3

        coEvery {
            networkApi.getAlbumsFromUserAsync(userId).await()
        } returns DataFactory.produceListOfAlbumsEntity(albumNum)

        coEvery {
            albumCacheSource.getAllAlbums(userId)
        } returns DataFactory.produceListOfAlbums(albumNum)

        coEvery {
            photoRepository.getPhotoByIdSuspend(any(), any())
        } returns Photo()

        val networkValue = networkApi.getAlbumsFromUserAsync(userId).await()
        val repositoryValue = albumsRepository.getAlbumList(userId)

        Assertions.assertThat(AlbumDataToAlbumMapper.mapList(networkValue!!).size)
            .isEqualTo(repositoryValue.first().data?.size)
    }

    @Test
    fun `getAlbums Error passes through repository`() = runBlockingTest {

        val userId = 1
        val exception: Exception = Exception("Any")

        coEvery { networkApi.getAlbumsFromUserAsync(userId).await() } throws exception

        coEvery { albumCacheSource.getAllAlbums(userId) } throws exception

        val repositoryValue = albumsRepository.getAlbumList(userId)

        Assertions.assertThat(repositoryValue.first()).isInstanceOf(DataState.Error::class.java)
    }
}