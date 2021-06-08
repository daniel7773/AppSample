package com.example.appsample.business.data.cache.implementation

import com.example.appsample.business.data.cache.abstraction.PostCacheSource
import com.example.appsample.business.domain.model.Post
import com.example.appsample.framework.datasource.cache.abstraction.PostDaoService
import javax.inject.Inject


class PostCacheSourceImpl
@Inject
constructor(
    private val postDaoService: PostDaoService
) : PostCacheSource {

    override suspend fun insertPost(post: Post): Long {
        return postDaoService.insertPost(post)
    }

    override suspend fun insertPostList(posts: List<Post>): LongArray {
        return postDaoService.insertPostList(posts)
    }

    override suspend fun deletePost(id: Int): Int {
        return postDaoService.deletePost(id)
    }

    override suspend fun deletePosts(posts: List<Post>): Int {
        return postDaoService.deletePosts(posts)
    }

    override suspend fun updatePost(post: Post, timestamp: String?): Int {
        return postDaoService.updatePost(
            post.id ?: 0,
            post.userId ?: 0,
            post.title ?: "",
            post.body,
            timestamp
        )
    }

    override suspend fun searchPosts(query: String, page: Int): List<Post> {
        return postDaoService.searchPosts(query, page) ?: emptyList()
    }

    override suspend fun getAllPosts(userId: Int): List<Post> {
        return postDaoService.getAllPosts(userId)
    }

    override suspend fun searchPostById(id: Int): Post? {
        return postDaoService.searchPostById(id)
    }

    override suspend fun getNumPosts(userId: Int): Int {
        return postDaoService.getNumPosts(userId)
    }
}






