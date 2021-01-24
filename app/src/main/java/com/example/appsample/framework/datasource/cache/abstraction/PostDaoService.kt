package com.example.appsample.framework.datasource.cache.abstraction

import com.example.appsample.business.data.models.PostEntity

interface PostDaoService {

    suspend fun insertPost(postEntity: PostEntity): Long

    suspend fun insertPostList(posts: List<PostEntity>): LongArray

    suspend fun searchPostById(id: Int): PostEntity?

    suspend fun searchPosts(query: String, page: Int): List<PostEntity>?

    suspend fun updatePost(
        id: Int,
        user_id: Int,
        title: String,
        body: String?,
        timestamp: String?
    ): Int

    suspend fun deletePost(id: Int): Int

    suspend fun deletePosts(posts: List<PostEntity>): Int

    suspend fun getAllPosts(userId: Int): List<PostEntity>

    suspend fun getNumPosts(userId: Int): Int
}












