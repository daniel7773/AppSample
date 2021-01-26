package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.CommentCacheDataSource
import com.example.appsample.business.data.models.CommentEntity
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.CommentEntityToCommentMapper
import com.example.appsample.business.domain.model.Comment
import com.example.appsample.business.domain.repository.NetworkBoundResource
import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class CommentsRepositoryImpl @Inject constructor(
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val commentCacheDataSource: CommentCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource
) : CommentsRepository {

    private val TAG = "CommentsRepositoryImpl"

    override suspend fun getCommentList(postId: Int): Flow<List<Comment>?> {

        return object : NetworkBoundResource<List<CommentEntity>, List<Comment>>(
            { commentCacheDataSource.getAllComments(postId) },
            { jsonPlaceholderApiSource.getCommentListByPostIdAsync(postId).await() },
        ) {
            override suspend fun updateCache(entity: List<CommentEntity>) {
                Log.d(TAG, "updateCache called for commentList with size: ${entity.size}")
                commentCacheDataSource.insertCommentList(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here, now it is fake condition here since it's sample,
            //  we know each post got 5 comments on server
            override suspend fun shouldFetch(entity: List<Comment>?) = entity?.size != 5
            override suspend fun map(entity: List<CommentEntity>) =
                CommentEntityToCommentMapper.map(entity)
        }.result
    }

    override suspend fun getCommentsNum(postId: Int): Int? {

        return object : NetworkBoundResource<List<CommentEntity>, Int>(
            { commentCacheDataSource.getAllComments(postId) },
            { jsonPlaceholderApiSource.getCommentListByPostIdAsync(postId).await() },
        ) {
            override suspend fun updateCache(entity: List<CommentEntity>) {
                Log.d(TAG, "updateCache called for commentList with size: ${entity.size}")
                commentCacheDataSource.insertCommentList(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here, now it is fake condition here since it's sample,
            //  we know each post got 5 comments on server
            override suspend fun shouldFetch(entity: Int?): Boolean = entity != 5
            override suspend fun map(entity: List<CommentEntity>) = entity.size
        }.resultSuspend()
    }
}