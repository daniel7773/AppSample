package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.models.PhotoEntity
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PhotoEntityToPhotoMapper
import com.example.appsample.business.domain.repository.abstraction.PhotoRepository
import com.example.appsample.business.domain.repository.implementation.PhotoRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class PhotoRepositoryTest {

    // system in test
    private val photoRepository: PhotoRepository

    // resources needed
    val networkApi: JsonPlaceholderApiSource = mockk()

    init {
        photoRepository = PhotoRepositoryImpl(networkApi)
    }

    @Nested
    inner class SinglePhoto {
        @Test
        fun `getPhoto Success`() = runBlockingTest {

            val albumId = 1
            val photoId = 1

            coEvery {
                networkApi.getPhotoById(photoId).await()
            } returns DataFactory.producePhotoEntity(photoId)

            val networkValue = networkApi.getPhotoById(photoId).await()
            val repositoryValue = photoRepository.getPhotoById(albumId, photoId)

            Assertions.assertThat(PhotoEntityToPhotoMapper.mapPhoto(networkValue!!))
                .isEqualTo(repositoryValue.data)
        }

        @Test
        fun `getPhoto Error passes through repository`() = runBlockingTest {

            val albumId = 1
            val photoId = 1

            var exception: Exception = Exception("Any")
            coEvery {
                networkApi.getPhotoById(photoId).await()
            } throws exception

            val repositoryValue = photoRepository.getPhotoById(albumId, photoId)

            Assertions.assertThat(repositoryValue.exception).isInstanceOf(exception::class.java)
            Assertions.assertThat(exception.message).isEqualTo(repositoryValue.exception?.message)
            Assertions.assertThat(exception.localizedMessage)
                .isEqualTo(repositoryValue.exception?.localizedMessage)
        }

        @Test
        fun `getPhoto TimeoutCancellationException passes through repository`() = runBlockingTest {

            val albumId = 1
            val photoId = 1

            coEvery {
                networkApi.getPhotoById(photoId)
            } answers {
                val deferredPhotoEntity = CompletableDeferred<PhotoEntity?>()

                deferredPhotoEntity
            }

            val repositoryValue = photoRepository.getPhotoById(albumId, photoId)

            Assertions.assertThat(repositoryValue.exception).isNotNull
            Assertions.assertThat(repositoryValue.exception)
                .isInstanceOf(TimeoutCancellationException::class.java)
        }
    }

    @Nested
    inner class AlbumPhotos {

        @Test
        fun `getAlbumPhotos Success`() = runBlockingTest {

            val userId = 1

            coEvery {
                networkApi.getAlbumPhotos(userId).await()
            } returns DataFactory.produceListOfPhotoEntity(userId)

            val networkValue = networkApi.getAlbumPhotos(userId).await()
            val repositoryValue = photoRepository.getAlbumPhotoList(userId)

            Assertions.assertThat(PhotoEntityToPhotoMapper.mapPhotoList(networkValue!!))
                .isEqualTo(repositoryValue.data)
        }

        @Test
        fun `getAlbumPhotos Error passes through repository`() = runBlockingTest {

            val userId = 1

            var exception: Exception = Exception("Any")
            coEvery {
                networkApi.getAlbumPhotos(userId).await()
            } throws exception

            val repositoryValue = photoRepository.getAlbumPhotoList(userId)

            Assertions.assertThat(repositoryValue.exception).isInstanceOf(exception::class.java)
            Assertions.assertThat(exception.message).isEqualTo(repositoryValue.exception?.message)
            Assertions.assertThat(exception.localizedMessage)
                .isEqualTo(repositoryValue.exception?.localizedMessage)
        }

        @Test
        fun `getAlbumPhotos TimeoutCancellationException passes through repository`() =
            runBlockingTest {

                val userId = 1

                coEvery {
                    networkApi.getAlbumPhotos(userId)
                } answers {
                    val deferredPhotoListEntities = CompletableDeferred<List<PhotoEntity>?>()

                    deferredPhotoListEntities
                }

                val repositoryValue = photoRepository.getAlbumPhotoList(userId)

                Assertions.assertThat(repositoryValue.exception).isNotNull
                Assertions.assertThat(repositoryValue.exception)
                    .isInstanceOf(TimeoutCancellationException::class.java)
            }
    }
}