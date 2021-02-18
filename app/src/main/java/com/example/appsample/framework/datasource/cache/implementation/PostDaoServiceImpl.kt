package com.example.appsample.framework.datasource.cache.implementation

import com.example.appsample.business.domain.model.Post
import com.example.appsample.framework.datasource.cache.abstraction.PostDaoService
import com.example.appsample.framework.datasource.cache.database.PostDao
import com.example.appsample.framework.datasource.cache.mappers.PostCacheMapper
import javax.inject.Inject

class PostDaoServiceImpl
@Inject
constructor(
    private val postDao: PostDao,
    private val postCacheMapper: PostCacheMapper
) : PostDaoService {
    override suspend fun insertPost(post: Post): Long {
        return postDao.insertPost(postCacheMapper.mapToCacheEntity(post))
    }

    override suspend fun insertPostList(posts: List<Post>): LongArray {
        return postDao.insertPosts(postCacheMapper.listToCacheEntityList(posts))
    }

    override suspend fun searchPostById(id: Int) = postDao.searchPostById(id)?.run {
        postCacheMapper.mapFromCacheEntity(this)
    }

    override suspend fun searchPosts(query: String, page: Int) =
        postCacheMapper.cacheEntityListToList(postDao.searchPosts(query, page))

    override suspend fun updatePost(
        id: Int,
        user_id: Int,
        title: String,
        body: String?,
        timestamp: String?
    ): Int {
        return postDao.updatePost(id, user_id, title, body, "NOW")
    }

    override suspend fun deletePost(id: Int): Int {
        return postDao.deletePost(id)
    }

    override suspend fun deletePosts(posts: List<Post>): Int {
        val ids = posts.mapIndexed { index, value -> value.id ?: 0 }
        return postDao.deletePosts(ids)
    }

    override suspend fun getAllPosts(userId: Int): List<Post> {
        return postCacheMapper.cacheEntityListToList(postDao.getAllPosts(userId))
    }

    override suspend fun getNumPosts(userId: Int): Int {
        return postDao.getNumPosts(userId)
    }
}