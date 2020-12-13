package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.network.FORCE_GET_EXCEPTION
import com.example.appsample.business.data.network.FORCE_GET_TIMEOUT_EXCEPTION
import com.example.appsample.business.data.network.FakeJsonPlaceHolderApiSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostEntityToPostMapper
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.repository.implementation.PostsRepositoryImpl
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
    val networkApi: JsonPlaceholderApiSource = FakeJsonPlaceHolderApiSource()

    init {
        postsRepository = PostsRepositoryImpl(networkApi)
    }

    @Test
    fun `getPosts Success`() = runBlockingTest {

        val userId = 1
        val networkValue = networkApi.getPostsFromUser(userId).await()
        val repositoryValue = postsRepository.getPosts(userId)

        Assertions.assertThat(PostEntityToPostMapper.map(networkValue!!))
            .isEqualTo(repositoryValue.data)
    }

    @Test
    fun `getPosts Error passes through repository`() = runBlockingTest {

        val userId = FORCE_GET_EXCEPTION

        var exception: Exception = Exception("Any")
        val repositoryValue = postsRepository.getPosts(userId)
        try {
            networkApi.getPostsFromUser(userId).await()
        } catch (e: Exception) {
            exception = e
        }

        Assertions.assertThat(repositoryValue.exception).isInstanceOf(exception::class.java)
        Assertions.assertThat(exception.message).isEqualTo(repositoryValue.exception?.message)
        Assertions.assertThat(exception.localizedMessage)
            .isEqualTo(repositoryValue.exception?.localizedMessage)
    }

    @Test
    fun `getPosts TimeoutCancellationException passes through repository`() = runBlockingTest {

        val userId = FORCE_GET_TIMEOUT_EXCEPTION

        val repositoryValue = postsRepository.getPosts(userId)

        Assertions.assertThat(repositoryValue.exception).isNotNull
        Assertions.assertThat(repositoryValue.exception)
            .isInstanceOf(TimeoutCancellationException::class.java)
    }
}