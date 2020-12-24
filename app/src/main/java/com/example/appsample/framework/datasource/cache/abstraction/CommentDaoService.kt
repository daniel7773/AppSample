package com.example.appsample.framework.datasource.cache.abstraction

import com.example.appsample.business.data.models.CommentEntity

interface CommentDaoService {

    suspend fun insertComment(commentEntity: CommentEntity): Long

    suspend fun insertCommentList(commentList: List<CommentEntity>): LongArray

    suspend fun searchCommentById(id: Int): CommentEntity?

    suspend fun searchComments(query: String, page: Int): List<CommentEntity>?

    suspend fun updateComment(
        id: Int,
        post_id: Int,
        name: String,
        email: String,
        body: String?,
        timestamp: String?
    ): Int

    suspend fun deleteComment(id: Int): Int

    suspend fun deleteComments(comments: List<CommentEntity>): Int

    suspend fun getAllComments(postId: Int): List<CommentEntity>

    suspend fun getNumComments(userId: Int): Int
}












