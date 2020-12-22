package com.example.appsample.business.domain.repository.implementation

import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.data.network.abstraction.GET_POSTS_TIMEOUT
import com.example.appsample.business.data.network.abstraction.GET_POST_TIMEOUT
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostEntityToPostMapper
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.Resource.Error
import com.example.appsample.business.domain.repository.Resource.Success
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
) : PostsRepository {

    override suspend fun getPostsList(userId: Int): Resource<List<Post>?> {
        var postEntityList: List<PostEntity>? = null

        try {
            postEntityList = withTimeout(GET_POSTS_TIMEOUT) {
                return@withTimeout jsonPlaceholderApiSource.getPostsListFromUserAsync(userId)
                    .await()
            }
        } catch (e: Exception) {
            return Error(null, "Catch error while calling getPostList", e)
        }

        if (postEntityList == null) {
            return Error(null, "Data from repository is null", NullPointerException())
        }

        val postList = PostEntityToPostMapper.mapList(postEntityList)

        return Success(postList, "Success")
    }

    override suspend fun getPost(postId: Int): Resource<Post?> {
        var postEntity: PostEntity? = null

        try {
            postEntity = withTimeout(GET_POST_TIMEOUT) {
                return@withTimeout jsonPlaceholderApiSource.getPostByIdAsync(postId)
                    .await()
            }
        } catch (e: Exception) {
            return Error(null, "Catch error while calling getPost", e)
        }

        if (postEntity == null) {
            return Error(null, "Data from repository is null", NullPointerException())
        }

        val post = PostEntityToPostMapper.map(postEntity)

        return Success(post, "Success")
    }
}