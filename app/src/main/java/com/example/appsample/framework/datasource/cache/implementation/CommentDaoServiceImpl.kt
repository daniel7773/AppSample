package com.example.appsample.framework.datasource.cache.implementation

import com.example.appsample.business.data.models.CommentEntity
import com.example.appsample.framework.datasource.cache.abstraction.CommentDaoService
import com.example.appsample.framework.datasource.cache.database.CommentDao
import com.example.appsample.framework.datasource.cache.mappers.CommentCacheMapper
import javax.inject.Inject

class CommentDaoServiceImpl
@Inject
constructor(
    private val commentDao: CommentDao,
    private val commentCacheMapper: CommentCacheMapper
) : CommentDaoService {
    override suspend fun insertComment(commentEntity: CommentEntity): Long {
        return commentDao.insertComment(commentCacheMapper.mapToCacheEntity(commentEntity))
    }

    override suspend fun insertCommentList(commentList: List<CommentEntity>): LongArray {
        return commentDao.insertComments(commentCacheMapper.entityListToCacheEntityList(commentList))
    }

    override suspend fun searchCommentById(id: Int) = commentDao.searchCommentById(id)?.run {
        commentCacheMapper.mapFromCacheEntity(this)
    }

    override suspend fun searchComments(query: String, page: Int) =
        commentCacheMapper.cacheEntityListToEntityList(commentDao.searchComments(query, page))

    override suspend fun updateComment(
        id: Int,
        post_id: Int,
        name: String,
        email: String,
        body: String?,
        timestamp: String?
    ): Int {
        return commentDao.updateComment(id, post_id, name, email, body, "NOW")
    }

    override suspend fun deleteComment(id: Int): Int {
        return commentDao.deleteComment(id)
    }

    override suspend fun deleteComments(comments: List<CommentEntity>): Int {
        val ids = comments.mapIndexed { index, value -> value.id ?: 0 }
        return commentDao.deleteComments(ids)
    }

    override suspend fun getAllComments(postId: Int): List<CommentEntity> {
        return commentCacheMapper.cacheEntityListToEntityList(commentDao.getAllComments(postId))
    }

    override suspend fun getNumComments(userId: Int): Int {
        return commentDao.getNumComments(userId)
    }
}