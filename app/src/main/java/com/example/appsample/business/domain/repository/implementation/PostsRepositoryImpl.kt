package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.PostCacheDataSource
import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostEntityToPostMapper
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.repository.NetworkBoundResource
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.Resource.Success
import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@FlowPreview
class PostsRepositoryImpl @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val postCacheDataSource: PostCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
    private val commentsRepository: CommentsRepository
) : PostsRepository {

    override suspend fun getPostsList(userId: Int): Resource<List<Post>?> {
        val resourceList = object : NetworkBoundResource<List<PostEntity>, List<Post>>(
            mainDispatcher,
            { postCacheDataSource.getAllPosts(userId) },
            { jsonPlaceholderApiSource.getPostsListFromUserAsync(userId).await() },
        ) {
            override suspend fun updateCache(entity: List<PostEntity>) {
                Log.d("Asdasddwc", "updateCache called for postList with size: ${entity.size}")
                postCacheDataSource.insertPostList(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here
            override suspend fun shouldFetch(entity: List<Post>?) = true
            override suspend fun map(entity: List<PostEntity>) =
                PostEntityToPostMapper.mapList(entity)
        }.resultSuspend()

        if (resourceList is Success) {
            resourceList.data?.forEach { post ->
                post.id?.run {
                    val commentsPostResponse = commentsRepository.getCommentsNum(this)

                    if (commentsPostResponse is Success) {
                        post.commentsSize = commentsPostResponse.data ?: 0
                    } else {
                        post.commentsSize = 0
                    }
                }
            }
        }
        return resourceList
    }

    override suspend fun getPost(postId: Int): Flow<Resource<Post?>> {
        return object : NetworkBoundResource<PostEntity, Post>(
            mainDispatcher,
            { postCacheDataSource.searchPostById(postId) },
            { jsonPlaceholderApiSource.getPostByIdAsync(postId).await() },
        ) {
            override suspend fun updateCache(entity: PostEntity) {
                Log.d("Asdasddwc", "updateCache called for post with id: ${entity.id}")
                postCacheDataSource.insertPost(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here
            override suspend fun shouldFetch(entity: Post?) = true

            override suspend fun map(entity: PostEntity) = PostEntityToPostMapper.map(entity)
        }.result
    }
}