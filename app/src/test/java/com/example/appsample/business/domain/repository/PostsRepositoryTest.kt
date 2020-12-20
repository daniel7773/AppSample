package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostEntityToPostMapper
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.repository.implementation.PostsRepositoryImpl
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
class PostsRepositoryTest {

    // system in test
    private val postsRepository: PostsRepository

    // resources needed
    val networkApi: JsonPlaceholderApiSource = mockk()

    init {
        postsRepository = PostsRepositoryImpl(networkApi)
    }

    @Test
    fun `getPosts Success`() = runBlockingTest {

        val userId = 1
        coEvery {
            networkApi.getPostsListFromUser(userId).await()
        } returns DataFactory.produceListOfPostsEntity(3)

        val networkValue = networkApi.getPostsListFromUser(userId).await()
        val repositoryValue = postsRepository.getPostsList(userId)

        Assertions.assertThat(PostEntityToPostMapper.map(networkValue!!))
            .isEqualTo(repositoryValue.data)
    }

    @Test
    fun `getPosts Error passes through repository`() = runBlockingTest {

        val userId = 1

        var exception: Exception = Exception("Any")
        coEvery {
            networkApi.getPostsListFromUser(userId).await()
        } throws exception

        val repositoryValue = postsRepository.getPostsList(userId)

        Assertions.assertThat(repositoryValue.exception).isInstanceOf(exception::class.java)
        Assertions.assertThat(exception.message).isEqualTo(repositoryValue.exception?.message)
        Assertions.assertThat(exception.localizedMessage)
            .isEqualTo(repositoryValue.exception?.localizedMessage)
    }

    @Test
    fun `getPosts TimeoutCancellationException passes through repository`() = runBlockingTest {

        val userId = 1

        coEvery {
            networkApi.getPostsListFromUser(userId)
        } answers {
            val deferredPostListEntities = CompletableDeferred<List<PostEntity>?>()

            deferredPostListEntities
        }

        val repositoryValue = postsRepository.getPostsList(userId)

        Assertions.assertThat(repositoryValue.exception).isNotNull
        Assertions.assertThat(repositoryValue.exception)
            .isInstanceOf(TimeoutCancellationException::class.java)
    }
}