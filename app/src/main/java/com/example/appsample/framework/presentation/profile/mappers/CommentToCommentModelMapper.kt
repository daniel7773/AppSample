package com.example.appsample.framework.presentation.profile.mappers

import com.example.appsample.business.domain.model.Comment
import com.example.appsample.framework.presentation.profile.model.CommentModel

object CommentToCommentModelMapper {

    fun map(commentList: List<Comment>) = commentList.map { createCommentModel(it) }

    private fun createCommentModel(comment: Comment) = CommentModel(
        postId = comment.postId,
        id = comment.id,
        name = comment.name,
        email = comment.email,
        body = comment.body
    )
}