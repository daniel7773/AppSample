package com.example.appsample.business.data.cache.abstraction

import com.example.appsample.business.data.models.CommentEntity

interface CommentCacheDataSource {

    suspend fun insertComment(comment: CommentEntity): Long

    suspend fun insertCommentList(comments: List<CommentEntity>): LongArray

    suspend fun deleteComment(id: Int): Int

    suspend fun deleteComments(comments: List<CommentEntity>): Int

    suspend fun updateComment(
        commentEntity: CommentEntity,
        timestamp: String?
    ): Int

    suspend fun searchComments(
        query: String,
        page: Int
    ): List<CommentEntity>

    suspend fun getAllComments(userId: Int): List<CommentEntity>

    suspend fun searchCommentById(id: Int): CommentEntity?

    suspend fun getNumComments(userId: Int): Int
}






