package com.example.appsample.business.data.cache.implementation

import com.example.appsample.business.data.cache.abstraction.CommentCacheSource
import com.example.appsample.business.domain.model.Comment
import com.example.appsample.framework.datasource.cache.abstraction.CommentDaoService
import javax.inject.Inject


class CommentCacheSourceImpl
@Inject
constructor(
    private val commentDaoService: CommentDaoService
) : CommentCacheSource {

    override suspend fun insertComment(comment: Comment): Long {
        return commentDaoService.insertComment(comment)
    }

    override suspend fun insertCommentList(comments: List<Comment>): LongArray {
        return commentDaoService.insertCommentList(comments)
    }

    override suspend fun deleteComment(id: Int): Int {
        return commentDaoService.deleteComment(id)
    }

    override suspend fun deleteComments(comments: List<Comment>): Int {
        return commentDaoService.deleteComments(comments)
    }

    override suspend fun updateComment(comment: Comment, timestamp: String?): Int {
        return commentDaoService.updateComment(
            comment.id ?: 0,
            comment.postId ?: 0,
            comment.name ?: "",
            comment.email ?: "",
            comment.body,
            timestamp
        )
    }

    override suspend fun searchComments(query: String, page: Int): List<Comment> {
        return commentDaoService.searchComments(query, page) ?: emptyList()
    }

    override suspend fun getAllComments(postId: Int): List<Comment> {
        return commentDaoService.getAllComments(postId)
    }

    override suspend fun searchCommentById(id: Int): Comment? {
        return commentDaoService.searchCommentById(id)
    }

    override suspend fun getNumComments(userId: Int): Int {
        return commentDaoService.getNumComments(userId)
    }
}






