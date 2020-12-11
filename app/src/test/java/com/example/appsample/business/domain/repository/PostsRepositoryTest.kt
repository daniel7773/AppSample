package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.network.FORCE_GET_EXCEPTION
import com.example.appsample.business.data.network.FORCE_GET_TIMEOUT_EXCEPTION
import com.example.appsample.business.data.network.FakeJsonPlaceHolderApiSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostEntityToPostMapper
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.repository.implementation.PostsRepositoryImpl
import junit.framework.Assert
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

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
    fun getPosts_success() = runBlocking {

        val userId = 1
        val networkValue = networkApi.getPostsFromUser(userId).await()
        val repositoryValue = postsRepository.getPosts(userId)

        Assert.assertEquals(PostEntityToPostMapper.map(networkValue!!), repositoryValue.data)
    }

    @Test
    fun getPosts_passedErrorCorrectly() = runBlocking {

        val userId = FORCE_GET_EXCEPTION

        var exception: Exception = Exception("Any")
        val repositoryValue = postsRepository.getPosts(userId)
        try {
            networkApi.getPostsFromUser(userId).await()
        } catch (e: Exception) {
            exception = e
        }

        Assert.assertEquals(exception.javaClass, repositoryValue.exception?.javaClass)
        Assert.assertEquals(exception.message, repositoryValue.exception?.message)
        Assert.assertEquals(exception.localizedMessage, repositoryValue.exception?.localizedMessage)
    }

    @Test
    fun getPosts_timeoutCheck() = runBlocking {

        val userId = FORCE_GET_TIMEOUT_EXCEPTION

        val repositoryValue = postsRepository.getPosts(userId)

        Assert.assertNotNull(repositoryValue.exception)
        Assert.assertEquals(
            TimeoutCancellationException::class.java,
            repositoryValue.exception!!.javaClass
        )
    }
}