package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.CommentCacheDataSource
import com.example.appsample.business.data.models.CommentEntity
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.CommentEntityToCommentMapper
import com.example.appsample.business.domain.model.Comment
import com.example.appsample.business.domain.repository.NetworkBoundResource
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val commentCacheDataSource: CommentCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource
) : CommentsRepository {

    override suspend fun getCommentList(postId: Int): Flow<Resource<List<Comment>?>> {

        return object : NetworkBoundResource<List<CommentEntity>, List<Comment>>(
            mainDispatcher,
            { commentCacheDataSource.getAllComments(postId) },
            { jsonPlaceholderApiSource.getCommentListByPostIdAsync(postId).await() },
        ) {
            override suspend fun updateCache(entity: List<CommentEntity>) {
                Log.d("Asdasddwc", "updateCache called for commentList with size: ${entity.size}")
                commentCacheDataSource.insertCommentList(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here
            override suspend fun shouldFetch(entity: List<Comment>?) = true
            override suspend fun map(entity: List<CommentEntity>) =
                CommentEntityToCommentMapper.map(entity)
        }.result
    }

    override suspend fun getCommentsNum(postId: Int): Resource<Int?> {

        return object : NetworkBoundResource<List<CommentEntity>, Int>(
            mainDispatcher,
            { commentCacheDataSource.getAllComments(postId) },
            { jsonPlaceholderApiSource.getCommentListByPostIdAsync(postId).await() },
        ) {
            override suspend fun updateCache(entity: List<CommentEntity>) {
                Log.d("Asdasddwc", "updateCache called for commentList with size: ${entity.size}")
                commentCacheDataSource.insertCommentList(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here
            override suspend fun shouldFetch(entity: Int?): Boolean = true
            override suspend fun map(entity: List<CommentEntity>) = entity.size
        }.resultSuspend()
    }
}