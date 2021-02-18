package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.cache.abstraction.PhotoCacheSource
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PhotoDataToPhotoMapper
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import com.example.appsample.business.domain.repository.implementation.PhotoRepositoryImpl
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.rules.InstantExecutorExtension
import com.example.appsample.rules.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class PhotoRepositoryTest {

    // system in test
    private val photoRepository: PhotoRepository

    // resources needed
    @get:Extensions
    val mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()

    val networkApi: JsonPlaceholderApiSource = mockk()
    private val photoCacheSource: PhotoCacheSource = mockk()

    init {
        photoRepository = PhotoRepositoryImpl(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            photoCacheSource = photoCacheSource,
            jsonPlaceholderApiSource = networkApi
        )


        coEvery {
            photoCacheSource.insertPhoto(any())
        } returns 1L

        coEvery {
            photoCacheSource.insertPhotoList(any())
        } returns LongArray(1)
    }

    @Nested
    inner class SinglePhoto {
        // TODO: add tests
    }

    @Nested
    inner class AlbumPhotos {

        @Test
        fun `getAlbumPhotoList Success`() = runBlockingTest {

            val albumId = 1
            val photoNum = 3

            coEvery { networkApi.getAlbumPhotosAsync(albumId).await() } returns DataFactory.produceListOfPhotoEntity(photoNum)

            coEvery { photoCacheSource.getAllPhotos(albumId) } returns DataFactory.produceListOfPhoto(photoNum)

            val networkValue = networkApi.getAlbumPhotosAsync(albumId).await()
            val repositoryValue = photoRepository.getAlbumPhotoList(albumId).first()

            Assertions.assertThat(PhotoDataToPhotoMapper.mapPhotoList(networkValue!!))
                .isEqualTo(repositoryValue.data)
        }

        @Test
        fun `getAlbumPhotoList Error passes through repository`() = runBlockingTest {

            val albumId = 1

            var exception: Exception = Exception("Any")
            coEvery { networkApi.getAlbumPhotosAsync(albumId).await() } throws exception

            coEvery { photoCacheSource.getAllPhotos(albumId) } throws exception

            val repositoryValue = photoRepository.getAlbumPhotoList(albumId).first()

            Assertions.assertThat(repositoryValue).isInstanceOf(DataState.Error::class.java)
        }
    }
}