package com.example.appsample.business.data.cache.abstraction

import com.example.appsample.business.domain.model.Post


interface PostCacheSource {

    suspend fun insertPost(post: Post): Long

    suspend fun insertPostList(posts: List<Post>): LongArray

    suspend fun deletePost(id: Int): Int

    suspend fun deletePosts(posts: List<Post>): Int

    suspend fun updatePost(
        postData: Post,
        timestamp: String?
    ): Int

    suspend fun searchPosts(
        query: String,
        page: Int
    ): List<Post>

    suspend fun getAllPosts(userId: Int): List<Post>

    suspend fun searchPostById(id: Int): Post?

    suspend fun getNumPosts(userId: Int): Int
}






