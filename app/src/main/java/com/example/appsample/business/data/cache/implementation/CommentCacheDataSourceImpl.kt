package com.example.appsample.business.data.cache.implementation

import com.example.appsample.business.data.cache.abstraction.CommentCacheDataSource
import com.example.appsample.business.data.models.CommentEntity
import com.example.appsample.framework.datasource.cache.abstraction.CommentDaoService
import javax.inject.Inject


class CommentCacheDataSourceImpl
@Inject
constructor(
    private val commentDaoService: CommentDaoService
) : CommentCacheDataSource {

    override suspend fun insertComment(comment: CommentEntity): Long {
        return commentDaoService.insertComment(comment)
    }

    override suspend fun insertCommentList(comments: List<CommentEntity>): LongArray {
        return commentDaoService.insertCommentList(comments)
    }

    override suspend fun deleteComment(id: Int): Int {
        return commentDaoService.deleteComment(id)
    }

    override suspend fun deleteComments(comments: List<CommentEntity>): Int {
        return commentDaoService.deleteComments(comments)
    }

    override suspend fun updateComment(commentEntity: CommentEntity, timestamp: String?): Int {
        return commentDaoService.updateComment(
            commentEntity.id ?: 0,
            commentEntity.postId ?: 0,
            commentEntity.name ?: "",
            commentEntity.email ?: "",
            commentEntity.body,
            timestamp
        )
    }

    override suspend fun searchComments(query: String, page: Int): List<CommentEntity> {
        return commentDaoService.searchComments(query, page) ?: emptyList()
    }

    override suspend fun getAllComments(postId: Int): List<CommentEntity> {
        return commentDaoService.getAllComments(postId)
    }

    override suspend fun searchCommentById(id: Int): CommentEntity? {
        return commentDaoService.searchCommentById(id)
    }

    override suspend fun getNumComments(userId: Int): Int {
        return commentDaoService.getNumComments(userId)
    }
}






