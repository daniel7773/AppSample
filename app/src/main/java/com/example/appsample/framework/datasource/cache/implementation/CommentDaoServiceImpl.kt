package com.example.appsample.framework.datasource.cache.implementation

import com.example.appsample.business.domain.model.Comment
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
    override suspend fun insertComment(comment: Comment): Long {
        return commentDao.insertComment(commentCacheMapper.mapToCacheEntity(comment))
    }

    override suspend fun insertCommentList(commentList: List<Comment>): LongArray {
        return commentDao.insertComments(commentCacheMapper.listToCacheEntityList(commentList))
    }

    override suspend fun searchCommentById(id: Int) = commentDao.searchCommentById(id)?.run {
        commentCacheMapper.mapFromCacheEntity(this)
    }

    override suspend fun searchComments(query: String, page: Int) =
        commentCacheMapper.cacheEntityListToList(commentDao.searchComments(query, page))

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

    override suspend fun deleteComments(comments: List<Comment>): Int {
        val ids = comments.mapIndexed { index, value -> value.id ?: 0 }
        return commentDao.deleteComments(ids)
    }

    override suspend fun getAllComments(postId: Int): List<Comment> {
        return commentCacheMapper.cacheEntityListToList(commentDao.getAllComments(postId))
    }

    override suspend fun getNumComments(userId: Int): Int {
        return commentDao.getNumComments(userId)
    }
}