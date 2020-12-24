package com.example.appsample.business.data.cache.abstraction

import com.example.appsample.business.data.models.PostEntity

interface PostCacheDataSource {

    suspend fun insertPost(post: PostEntity): Long

    suspend fun insertPostList(posts: List<PostEntity>): LongArray

    suspend fun deletePost(id: Int): Int

    suspend fun deletePosts(posts: List<PostEntity>): Int

    suspend fun updatePost(
        postEntity: PostEntity,
        timestamp: String?
    ): Int

    suspend fun searchPosts(
        query: String,
        page: Int
    ): List<PostEntity>

    suspend fun getAllPosts(userId: Int): List<PostEntity>

    suspend fun searchPostById(id: Int): PostEntity?

    suspend fun getNumPosts(userId: Int): Int
}






