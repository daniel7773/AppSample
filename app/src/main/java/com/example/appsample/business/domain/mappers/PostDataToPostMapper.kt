package com.example.appsample.business.domain.mappers

import com.example.appsample.business.data.models.PostData
import com.example.appsample.business.domain.model.Post

object PostDataToPostMapper {

    fun mapList(postData: List<PostData>) = postData.map { createPost(it) }

    fun map(postData: PostData) = createPost(postData)

    private fun createPost(postData: PostData) = Post(
        userId = postData.userId,
        id = postData.id,
        title = postData.title,
        body = postData.body
    )
}