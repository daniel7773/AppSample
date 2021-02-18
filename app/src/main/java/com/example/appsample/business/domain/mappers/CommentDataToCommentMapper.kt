package com.example.appsample.business.domain.mappers

import com.example.appsample.business.data.models.CommentData
import com.example.appsample.business.domain.model.Comment

object CommentDataToCommentMapper {

    fun map(commentDataList: List<CommentData>) = commentDataList.map { createComment(it) }

    private fun createComment(commentData: CommentData) = Comment(
        postId = commentData.postId,
        id = commentData.id,
        name = commentData.name,
        email = commentData.email,
        body = commentData.body
    )
}