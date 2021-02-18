package com.example.appsample.framework.datasource.cache.abstraction

import com.example.appsample.business.domain.model.Post


interface PostDaoService {

    suspend fun insertPost(postData: Post): Long

    suspend fun insertPostList(posts: List<Post>): LongArray

    suspend fun searchPostById(id: Int): Post?

    suspend fun searchPosts(query: String, page: Int): List<Post>?

    suspend fun updatePost(
        id: Int,
        user_id: Int,
        title: String,
        body: String?,
        timestamp: String?
    ): Int

    suspend fun deletePost(id: Int): Int

    suspend fun deletePosts(posts: List<Post>): Int

    suspend fun getAllPosts(userId: Int): List<Post>

    suspend fun getNumPosts(userId: Int): Int
}












