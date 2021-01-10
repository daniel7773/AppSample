package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.cache.abstraction.PostCacheDataSource
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostEntityToPostMapper
import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.repository.implementation.PostsRepositoryImpl
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
    val postCacheDataSource: PostCacheDataSource = mockk()
    private val commentsRepository: CommentsRepository = mockk()

    init {

        coEvery {
            postCacheDataSource.insertPostList(any())
        } returns LongArray(1)

        coEvery {
            postCacheDataSource.insertPost(any())
        } returns 1L

        coEvery {
            postCacheDataSource.getAllPosts(any())
        } returns emptyList()

        coEvery {
            postCacheDataSource.searchPostById(any())
        } returns null


        postsRepository = PostsRepositoryImpl(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            postCacheDataSource = postCacheDataSource,
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
            postCacheDataSource.getAllPosts(userId)
        } returns DataFactory.produceListOfPostsEntity(3)

        coEvery {
            commentsRepository.getCommentsNum(any())
        } returns Resource.Success(10, "mocked data")

        val networkValue = networkApi.getPostsListFromUserAsync(userId).await()
        val repositoryValue = postsRepository.getPostsList(userId)

        Assertions.assertThat(PostEntityToPostMapper.mapList(networkValue!!).size)
            .isEqualTo((repositoryValue.first() as Resource.Success).data!!.size)
    }

    @Test
    fun `getPosts Error passes through repository`() = runBlockingTest {

        val userId = 1

        var exception: Exception = Exception("Any")
        coEvery {
            networkApi.getPostsListFromUserAsync(userId).await()
        } throws exception

        coEvery { postCacheDataSource.getAllPosts(userId) } throws exception

        val repositoryValue = postsRepository.getPostsList(userId)

        Assertions.assertThat((repositoryValue.first() as Resource.Error).exception)
            .isInstanceOf(exception::class.java)
        Assertions.assertThat(exception.message).isEqualTo((repositoryValue.first() as Resource.Error).exception.message)
        Assertions.assertThat(exception.localizedMessage)
            .isEqualTo((repositoryValue.first() as Resource.Error).exception.localizedMessage)
    }
}