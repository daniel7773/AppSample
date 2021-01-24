package com.example.appsample.business.data.cache.implementation

import com.example.appsample.business.data.cache.abstraction.PostCacheDataSource
import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.framework.datasource.cache.abstraction.PostDaoService
import javax.inject.Inject


class PostCacheDataSourceImpl
@Inject
constructor(
    private val postDaoService: PostDaoService
) : PostCacheDataSource {

    override suspend fun insertPost(post: PostEntity): Long {
        return postDaoService.insertPost(post)
    }

    override suspend fun insertPostList(posts: List<PostEntity>): LongArray {
        return postDaoService.insertPostList(posts)
    }

    override suspend fun deletePost(id: Int): Int {
        return postDaoService.deletePost(id)
    }

    override suspend fun deletePosts(posts: List<PostEntity>): Int {
        return postDaoService.deletePosts(posts)
    }

    override suspend fun updatePost(postEntity: PostEntity, timestamp: String?): Int {
        return postDaoService.updatePost(
            postEntity.id ?: 0,
            postEntity.userId ?: 0,
            postEntity.title ?: "",
            postEntity.body,
            timestamp
        )
    }

    override suspend fun searchPosts(query: String, page: Int): List<PostEntity> {
        return postDaoService.searchPosts(query, page) ?: emptyList()
    }

    override suspend fun getAllPosts(userId: Int): List<PostEntity> {
        return postDaoService.getAllPosts(userId)
    }

    override suspend fun searchPostById(id: Int): PostEntity? {
        return postDaoService.searchPostById(id)
    }

    override suspend fun getNumPosts(userId: Int): Int {
        return postDaoService.getNumPosts(userId)
    }
}






