package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.cache.abstraction.AlbumCacheDataSource
import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.data.models.PhotoEntity
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.AlbumEntityToAlbumMapper
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.repository.abstraction.AlbumsRepository
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import com.example.appsample.business.domain.repository.implementation.AlbumsRepositoryImpl
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.rules.InstantExecutorExtension
import com.example.appsample.rules.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
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
    val albumCacheDataSource: AlbumCacheDataSource = mockk()
    val photoRepository: PhotoRepository = mockk()

    init {
        albumsRepository = AlbumsRepositoryImpl(
            mainDispatcher = mainCoroutineRule.testDispatcher,
            albumCacheDataSource = albumCacheDataSource,
            jsonPlaceholderApiSource = networkApi,
            photoRepository = photoRepository
        )

        coEvery {
            albumCacheDataSource.insertAlbum(any())
        } returns 1L

        coEvery {
            albumCacheDataSource.insertAlbumList(any())
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
            albumCacheDataSource.getAllAlbums(userId)
        } returns DataFactory.produceListOfAlbumsEntity(albumNum)

        coEvery {
            photoRepository.getPhotoById(any(), any())
        } returns Resource.Success(Photo(), "mocked data")

        val networkValue = networkApi.getAlbumsFromUserAsync(userId).await()
        val repositoryValue = albumsRepository.getAlbumList(userId)

        Assertions.assertThat(AlbumEntityToAlbumMapper.mapList(networkValue!!).size)
            .isEqualTo((repositoryValue as Resource.Success).data!!.size)
    }

    @Test
    fun `getAlbums Error passes through repository`() = runBlockingTest {

        val userId = 1
        val exception: Exception = Exception("Any")

        coEvery { networkApi.getAlbumsFromUserAsync(userId).await() } throws exception

        coEvery { albumCacheDataSource.getAllAlbums(userId) } throws exception

        val repositoryValue = albumsRepository.getAlbumList(userId)

        Assertions.assertThat((repositoryValue as Resource.Error).exception).isInstanceOf(exception::class.java)
        Assertions.assertThat(exception.message).isEqualTo(repositoryValue.exception.message)
        Assertions.assertThat(exception.localizedMessage)
            .isEqualTo(repositoryValue.exception.localizedMessage)
    }
}