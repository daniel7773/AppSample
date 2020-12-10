package com.example.appsample.business.domain.repository.implementation

import com.example.appsample.business.data.network.abstraction.GET_USER_TIMEOUT
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostEntityToPostMapper
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.repository.abstraction.Resource
import com.example.appsample.business.domain.repository.abstraction.Resource.Error
import com.example.appsample.business.domain.repository.abstraction.Resource.Success
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
) : PostsRepository {

    override suspend fun getPosts(userId: Int?): Resource<List<Post>> {
        val postEntityList = withTimeoutOrNull(GET_USER_TIMEOUT) {
            return@withTimeoutOrNull jsonPlaceholderApiSource.getPostsFromUser(userId ?: 0).await()
        }
        if (postEntityList == null) {
            return Error(null, "DATA IS NULL", NullPointerException())
        }

        return Success(PostEntityToPostMapper.map(postEntityList), null)
    }
}