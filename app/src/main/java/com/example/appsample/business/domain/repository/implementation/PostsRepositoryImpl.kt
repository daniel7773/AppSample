package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.PostCacheSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.PostDataToPostMapper
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Named

@FlowPreview
class PostsRepositoryImpl @Inject constructor(
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val postCacheSource: PostCacheSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
    private val commentsRepository: CommentsRepository
) : PostsRepository {

    private val TAG = PostsRepositoryImpl::class.java.simpleName

    override suspend fun getPostsList(userId: Int) = flow {
        val cache: List<Post>? = getPostListCacheData(userId)

        if (!cache.isNullOrEmpty()) {
            emit(DataState.Loading<List<Post>?>(cache, "Data from cache"))
        } else {
            Log.d(TAG, "Value from cache is empty")
        }

        val networkData = getPostListNetworkData(cache, userId)
        if (!networkData.data.isNullOrEmpty()) {
            postCacheSource.insertPostList(networkData.data!!)
        }
        emit(networkData)
    }.flowOn(ioDispatcher)

    private suspend fun getPostListNetworkData(cacheValue: List<Post>?, postId: Int): DataState<List<Post>?> {
        try {
            val postDataList = withTimeout(15000L) {
                jsonPlaceholderApiSource.getPostsListFromUserAsync(postId).await()
            }
            return if (!postDataList.isNullOrEmpty()) {
                val postList = PostDataToPostMapper.mapList(postDataList)
                DataState.Success(addComments(postList))
            } else {
                DataState.Error(null, "NULL came from network", NullPointerException())
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting postList from network")
            }
            e.printStackTrace()
            return DataState.Error(addComments(cacheValue), "Can't get value from network", e)
        }
    }

    private suspend fun getPostListCacheData(userId: Int): List<Post>? {
        var postList: List<Post>?
        try {
            withTimeout(3000L) {
                postList = postCacheSource.getAllPosts(userId)
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting postList from cache")
            }
            e.printStackTrace()
            postList = null
        }
        return postList
    }

    private fun addComments(postList: List<Post>?) = runBlocking(ioDispatcher) {
        postList?.map { post ->
            async {
                if (post.id != null) {
                    post.commentsSize = commentsRepository.getCommentsNum(post.id!!)
                }
            }
        }?.map {
            it.await()
        }
        joinAll()
        postList
    }

    private fun addComments(post: Post) = runBlocking(ioDispatcher) {

        if (post.id == null) return@runBlocking post
        try {
            post.commentsSize = commentsRepository.getCommentsNum(post.id!!) // handle case, why send such response?
            return@runBlocking post
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Error while getting comments for post")
            e.printStackTrace()
            return@runBlocking post
        }
    }

    override suspend fun getPost(postId: Int) = flow {
        val cache: Post? = getPostCacheData(postId)

        if (cache != null) {
            emit(DataState.Loading<Post?>(cache, "Data from cache"))
        } else {
            Log.d(TAG, "Post value from cache is empty")
        }

        val networkData = getPostNetworkData(cache, postId)
        if (networkData.data != null) {
            postCacheSource.insertPost(networkData.data!!)
        }
        emit(networkData)
    }.flowOn(ioDispatcher)

    private suspend fun getPostNetworkData(cacheValue: Post?, postId: Int): DataState<Post?> {
        try {
            val postData = withTimeout(15000L) {
                jsonPlaceholderApiSource.getPostByIdAsync(postId).await()
            }
            return if (postData != null) {
                val post = PostDataToPostMapper.map(postData)
                DataState.Success(addComments(post))
            } else {
                DataState.Error(null, "NULL came from network", null)
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting post from network")
            }
            e.printStackTrace()
            if (cacheValue != null) {
                addComments(cacheValue)
            }
            return DataState.Error(cacheValue, "Can't get value from network", e)
        }
    }

    private suspend fun getPostCacheData(userId: Int): Post? {
        var post: Post?
        try {
            withTimeout(3000L) {
                post = postCacheSource.searchPostById(userId)
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting post from cache")
            }
            e.printStackTrace()
            post = null
        }
        return post
    }
}