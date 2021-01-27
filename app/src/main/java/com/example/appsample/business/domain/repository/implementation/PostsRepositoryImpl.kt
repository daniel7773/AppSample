package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.PostCacheDataSource
import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostEntityToPostMapper
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.repository.NetworkBoundResource
import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Named

@FlowPreview
class PostsRepositoryImpl @Inject constructor(
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val postCacheDataSource: PostCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
    private val commentsRepository: CommentsRepository
) : PostsRepository {

    private val TAG = PostsRepositoryImpl::class.java.simpleName

    override suspend fun getPostsList(userId: Int): Flow<List<Post>?> {
        val postList = object : NetworkBoundResource<List<PostEntity>, List<Post>>(
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
        }.resultSuspend()



        return flowOf(addComments(postList))
    }

    private fun addComments(postList: List<Post>?) = runBlocking(ioDispatcher) {
        postList?.map { post ->
            async {
                post.commentsSize = commentsRepository.getCommentsNum(post.id ?: 0)
            }
        }?.map {
            it.await()
        }
        joinAll()
        postList
    }

    override suspend fun getPost(postId: Int): Flow<Post?> {
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