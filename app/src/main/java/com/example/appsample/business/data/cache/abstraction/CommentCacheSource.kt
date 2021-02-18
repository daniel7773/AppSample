package com.example.appsample.business.data.cache.abstraction

import com.example.appsample.business.domain.model.Comment

interface CommentCacheSource {

    suspend fun insertComment(comment: Comment): Long

    suspend fun insertCommentList(comments: List<Comment>): LongArray

    suspend fun deleteComment(id: Int): Int

    suspend fun deleteComments(comments: List<Comment>): Int

    suspend fun updateComment(
        comment: Comment,
        timestamp: String?
    ): Int

    suspend fun searchComments(
        query: String,
        page: Int
    ): List<Comment>

    suspend fun getAllComments(postId: Int): List<Comment>

    suspend fun searchCommentById(id: Int): Comment?

    suspend fun getNumComments(userId: Int): Int
}






