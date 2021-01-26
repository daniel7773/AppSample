package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostsRepository {

    suspend fun getPostsList(userId: Int): Flow<List<Post>?>

    suspend fun getPost(postId: Int): Flow<Post?>
}