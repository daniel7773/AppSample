package com.example.appsample.framework.datasource.cache.abstraction

import com.example.appsample.business.domain.model.Comment


interface CommentDaoService {

    suspend fun insertComment(comment: Comment): Long

    suspend fun insertCommentList(commentList: List<Comment>): LongArray

    suspend fun searchCommentById(id: Int): Comment?

    suspend fun searchComments(query: String, page: Int): List<Comment>?

    suspend fun updateComment(
        id: Int,
        post_id: Int,
        name: String,
        email: String,
        body: String?,
        timestamp: String?
    ): Int

    suspend fun deleteComment(id: Int): Int

    suspend fun deleteComments(comments: List<Comment>): Int

    suspend fun getAllComments(postId: Int): List<Comment>

    suspend fun getNumComments(userId: Int): Int
}












