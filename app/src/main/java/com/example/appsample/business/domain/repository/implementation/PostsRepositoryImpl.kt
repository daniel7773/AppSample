package com.example.appsample.business.domain.repository.implementation

import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.data.network.abstraction.GET_POSTS_TIMEOUT
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostEntityToPostMapper
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.repository.abstraction.Resource
import com.example.appsample.business.domain.repository.abstraction.Resource.Error
import com.example.appsample.business.domain.repository.abstraction.Resource.Success
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
) : PostsRepository {

    override suspend fun getPosts(userId: Int?): Resource<List<Post>?> {
        var postEntityList: List<PostEntity>? = null

        try {
            postEntityList = withTimeout(GET_POSTS_TIMEOUT) {
                return@withTimeout jsonPlaceholderApiSource.getPostsFromUser(userId ?: 0).await()
            }
        } catch (e: Exception) {
            return Error(null, "catched error in try block of getPosts", e)
        }

        if (postEntityList == null) {
            return Error(null, "DATA IS NULL", NullPointerException())
        }

        return Success(PostEntityToPostMapper.map(postEntityList), null)
    }
}