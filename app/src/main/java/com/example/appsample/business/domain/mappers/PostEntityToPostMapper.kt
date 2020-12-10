package com.example.appsample.business.domain.mappers

import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.domain.model.Post

object PostEntityToPostMapper {

    fun map(postEntity: List<PostEntity>) = postEntity.map { createPost(it) }

    private fun createPost(postEntity: PostEntity) = Post(
        userId = postEntity.userId,
        id = postEntity.id,
        title = postEntity.title,
        body = postEntity.body
    )
}