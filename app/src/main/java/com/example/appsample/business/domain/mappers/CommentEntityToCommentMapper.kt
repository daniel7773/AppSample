package com.example.appsample.business.domain.mappers

import com.example.appsample.business.data.models.CommentEntity
import com.example.appsample.business.domain.model.Comment

object CommentEntityToCommentMapper {

    fun map(commentEntityList: List<CommentEntity>) = commentEntityList.map { createComment(it) }

    private fun createComment(commentEntity: CommentEntity) = Comment(
        postId = commentEntity.postId,
        id = commentEntity.id,
        name = commentEntity.name,
        email = commentEntity.email,
        body = commentEntity.body
    )
}