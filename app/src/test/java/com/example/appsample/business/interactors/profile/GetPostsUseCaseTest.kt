package com.example.appsample.business.interactors.profile

import com.example.appsample.business.data.network.FakeJsonPlaceHolderApiSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostEntityToPostMapper
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.repository.implementation.PostsRepositoryImpl
import junit.framework.Assert
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

@InternalCoroutinesApi
class GetPostsUseCaseTest {

    private val postsRepository: PostsRepository
    private val networkApi: JsonPlaceholderApiSource

    init {
        networkApi = FakeJsonPlaceHolderApiSource()
        postsRepository = PostsRepositoryImpl(networkApi)
    }

    @Test
    fun getPosts_success() = runBlocking {

        val userId = 1
        val networkValue = networkApi.getPostsFromUser(userId).await()
        val repositoryValue = postsRepository.getPosts(userId)

        Assert.assertEquals(PostEntityToPostMapper.map(networkValue!!), repositoryValue.data)
    }

}