package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow

interface PostsRepository {

    suspend fun getPostsList(userId: Int): Flow<DataState<List<Post>?>>

    suspend fun getPost(postId: Int): Flow<DataState<Post?>>
}