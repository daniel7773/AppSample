package com.example.appsample.framework.presentation.profile.mappers

import com.example.appsample.business.domain.model.Post
import com.example.appsample.framework.presentation.profile.model.PostModel

object PostToPostModelMapper {

    fun mapList(postEntity: List<Post>) = postEntity.map { createPost(it) }

    fun map(postEntity: Post) = createPost(postEntity)

    private fun createPost(post: Post) = PostModel(
        userId = post.userId,
        id = post.id,
        commentsSize = post.commentsSize,
        title = post.title,
        body = post.body
    )
}