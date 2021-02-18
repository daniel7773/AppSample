package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.cache.abstraction.PostCacheSource
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostDataToPostMapper
import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.repository.implementation.PostsRepositoryImpl
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class PostsRepositoryTest {

    // system in test
    private val postsRepository: PostsRepository

    // resources needed
    @get:Extensions
    val mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()

    val networkApi: JsonPlaceholderApiSource = mockk()
    val postCacheSource: PostCacheSource = mockk()
    private val commentsRepository: CommentsRepository = mockk()

    init {

        coEvery {
            postCacheSource.insertPostList(any())
        } returns LongArray(1)

        coEvery {
            postCacheSource.insertPost(any())
        } returns 1L

        coEvery {
            postCacheSource.getAllPosts(any())
        } returns emptyList()

        coEvery {
            postCacheSource.searchPostById(any())
        } returns null


        postsRepository = PostsRepositoryImpl(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            postCacheSource = postCacheSource,
            jsonPlaceholderApiSource = networkApi,
            commentsRepository = commentsRepository
        )
    }

    @Test
    fun `getPosts Success`() = runBlockingTest {

        val userId = 1
        coEvery {
            networkApi.getPostsListFromUserAsync(userId).await()
        } returns DataFactory.produceListOfPostsEntity(3)

        coEvery {
            postCacheSource.getAllPosts(userId)
        } returns DataFactory.produceListOfPosts(3)

        coEvery {
            commentsRepository.getCommentsNum(any())
        } returns 10

        val networkValue = networkApi.getPostsListFromUserAsync(userId).await()
        val repositoryValue = postsRepository.getPostsList(userId)

        Assertions.assertThat(PostDataToPostMapper.mapList(networkValue!!).size)
            .isEqualTo(repositoryValue.first().data?.size)
    }

    @Test
    fun `getPosts Error passes through repository`() = runBlockingTest {

        val userId = 1

        var exception: Exception = Exception("Any")
        coEvery {
            networkApi.getPostsListFromUserAsync(userId).await()
        } throws exception

        coEvery { postCacheSource.getAllPosts(userId) } throws exception

        val repositoryValue = postsRepository.getPostsList(userId)

        Assertions.assertThat(repositoryValue.first()).isInstanceOf(DataState.Error::class.java)
    }
}