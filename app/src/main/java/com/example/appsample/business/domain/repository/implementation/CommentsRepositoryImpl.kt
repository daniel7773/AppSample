package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.CommentCacheSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.CommentDataToCommentMapper
import com.example.appsample.business.domain.model.Comment
import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Named

class CommentsRepositoryImpl @Inject constructor(
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val commentCacheSource: CommentCacheSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource
) : CommentsRepository {

    private val TAG = "CommentsRepositoryImpl"

    override suspend fun getCommentList(postId: Int): Flow<DataState<List<Comment>?>> = flow {
        val cache: List<Comment>? = getCacheData(postId)

        if (!cache.isNullOrEmpty()) {
            emit(DataState.Loading<List<Comment>?>(cache, "Data from cache"))
        } else {
            Log.d(TAG, "Value from cache is empty")
        }

        val networkData = getNetworkData(cache, postId)
        if (!networkData.data.isNullOrEmpty()) {
            commentCacheSource.insertCommentList(networkData.data!!)
        }
        emit(networkData)
    }.flowOn(ioDispatcher)

    private suspend fun getNetworkData(cacheValue: List<Comment>?, postId: Int): DataState<List<Comment>?> {
        try {
            val commentDataList = withTimeout(15000L) {
                jsonPlaceholderApiSource.getCommentListByPostIdAsync(postId).await()
            }
            return if (!commentDataList.isNullOrEmpty()) {
                val commentList = CommentDataToCommentMapper.map(commentDataList)
                DataState.Success(commentList)
            } else {
                DataState.Error(null, "NULL came from network", NullPointerException())
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting commentList from network")
            }
            e.printStackTrace()
            return DataState.Error(cacheValue, "Can't get value from network", e)
        }
    }

    private suspend fun getCacheData(postId: Int): List<Comment>? {
        var commentList: List<Comment>?
        try {
            withTimeout(3000L) {
                commentList = commentCacheSource.getAllComments(postId)
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting commentList from cache")
            }
            e.printStackTrace()
            commentList = null
        }
        return commentList
    }

    override suspend fun getCommentsNum(postId: Int): Int {
        try {
            return getNetworkData(null, postId).data?.size ?: 0
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Error while getting comments num from network.")
            e.printStackTrace()
            val commentNum = getCacheData(postId)?.size
            if (commentNum == null) {
                Log.e(TAG, "Error while getting comments num from cache, or empty cache.")
            }
            return commentNum ?: 0
        }
    }
}