package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.cache.abstraction.PhotoCacheDataSource
import com.example.appsample.business.data.models.PhotoEntity
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PhotoEntityToPhotoMapper
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import com.example.appsample.business.domain.repository.implementation.PhotoRepositoryImpl
import com.example.appsample.rules.InstantExecutorExtension
import com.example.appsample.rules.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
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
    private val photoCacheDataSource: PhotoCacheDataSource = mockk()

    init {
        photoRepository = PhotoRepositoryImpl(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            photoCacheDataSource = photoCacheDataSource,
            jsonPlaceholderApiSource = networkApi
        )


        coEvery {
            photoCacheDataSource.insertPhoto(any())
        } returns 1L

        coEvery {
            photoCacheDataSource.insertPhotoList(any())
        } returns LongArray(1)
    }

    @Nested
    inner class SinglePhoto {
        @Test
        fun `getPhoto Success`() = runBlockingTest {

            val albumId = 1
            val photoId = 1

            coEvery {
                networkApi.getPhotoByIdAsync(photoId).await()
            } returns DataFactory.producePhotoEntity(photoId)
//

            coEvery {
                photoCacheDataSource.searchPhotoById(photoId)
            } returns DataFactory.producePhotoEntity(photoId)

            val networkValue = networkApi.getPhotoByIdAsync(photoId).await()
            val repositoryValue = photoRepository.getPhotoById(albumId, photoId)

            Assertions.assertThat(PhotoEntityToPhotoMapper.mapPhoto(networkValue!!))
                .isEqualTo(repositoryValue.first())
        }

        @Test
        fun `getPhoto Error passes through repository`() = runBlockingTest {

            val albumId = 1
            val photoId = 1

            var exception: Exception = Exception("Any")
            coEvery {
                networkApi.getPhotoByIdAsync(photoId).await()
            } throws exception

            coEvery {
                photoCacheDataSource.searchPhotoById(photoId)
            } throws exception

            val repositoryValue = photoRepository.getPhotoById(albumId, photoId)

            Assertions.assertThat(repositoryValue.first()).isEqualTo(null)
        }
    }

    @Nested
    inner class AlbumPhotos {

        @Test
        fun `getAlbumPhotoList Success`() = runBlockingTest {

            val albumId = 1
            val photoNum = 3

            coEvery {
                networkApi.getAlbumPhotosAsync(albumId).await()
            } returns DataFactory.produceListOfPhotoEntity(photoNum)

            coEvery {
                photoCacheDataSource.getAllPhotos(albumId)
            } returns DataFactory.produceListOfPhotoEntity(photoNum)

            val networkValue = networkApi.getAlbumPhotosAsync(albumId).await()
            val repositoryValue = photoRepository.getAlbumPhotoList(albumId).first()

            Assertions.assertThat(PhotoEntityToPhotoMapper.mapPhotoList(networkValue!!))
                .isEqualTo(repositoryValue)
        }

        @Test
        fun `getAlbumPhotoList Error passes through repository`() = runBlockingTest {

            val albumId = 1

            var exception: Exception = Exception("Any")
            coEvery {
                networkApi.getAlbumPhotosAsync(albumId).await()
            } throws exception

            coEvery {
                photoCacheDataSource.getAllPhotos(albumId)
            } throws exception

            val repositoryValue = photoRepository.getAlbumPhotoList(albumId).first()

            Assertions.assertThat(repositoryValue).isEqualTo(null)
        }
    }
}