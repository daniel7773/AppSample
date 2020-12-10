package com.example.appsample.framework.presentation.profile.mappers

import com.example.appsample.business.domain.model.Post
import com.example.appsample.framework.presentation.profile.models.PostModel

object PostToPostModelMapper {

    fun map(postEntity: List<Post>) = postEntity.map { createPost(it) }

    private fun createPost(post: Post) = PostModel(
        userId = post.userId,
        id = post.id,
        title = post.title,
        body = post.body
    )
}