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
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@FlowPreview
class PostsRepositoryImpl @Inject constructor(
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val postCacheDataSource: PostCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
    private val commentsRepository: CommentsRepository
) : PostsRepository {

    private val TAG = "PostsRepositoryImpl"

    override suspend fun getPostsList(userId: Int): Flow<Resource<List<Post>?>> {
        return object : NetworkBoundResource<List<PostEntity>, List<Post>>(
            { postCacheDataSource.getAllPosts(userId) },
            { jsonPlaceholderApiSource.getPostsListFromUserAsync(userId).await() },
        ) {
            override suspend fun updateCache(entity: List<PostEntity>) {
                Log.d(TAG, "updateCache called for postList with size: ${entity.size}")
                postCacheDataSource.insertPostList(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here, now it is fake condition here since it's sample,
            //  we know that every user got 10 posts on server
            override suspend fun shouldFetch(entity: List<Post>?) = entity?.size != 10
            override suspend fun map(entity: List<PostEntity>) =
                PostEntityToPostMapper.mapList(entity)
        }.result.transform { postListResource ->
            if (postListResource is Success) {
                postListResource.data?.forEach { post ->
                    post.id?.run {
                        val commentsPostResponse = withContext(ioDispatcher) {
                            commentsRepository.getCommentsNum(this@run)
                        }
                        if (commentsPostResponse is Success) {
                            post.commentsSize = commentsPostResponse.data ?: 0
                        } else {
                            post.commentsSize = 0
                        }

                    }
                }
            }
            emit(postListResource)
        }
    }

    override suspend fun getPost(postId: Int): Flow<Resource<Post?>> {
        return object : NetworkBoundResource<PostEntity, Post>(
            { postCacheDataSource.searchPostById(postId) },
            { jsonPlaceholderApiSource.getPostByIdAsync(postId).await() },
        ) {
            override suspend fun updateCache(entity: PostEntity) {
                Log.d(TAG, "updateCache called for post with id: ${entity.id}")
                postCacheDataSource.insertPost(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here, now it is fake condition here since it's sample,
            //  post is always same on server
            override suspend fun shouldFetch(entity: Post?) = entity == null

            override suspend fun map(entity: PostEntity) = PostEntityToPostMapper.map(entity)
        }.result
    }
}