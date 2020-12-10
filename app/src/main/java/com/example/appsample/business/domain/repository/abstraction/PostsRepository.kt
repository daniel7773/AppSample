package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Post

interface PostsRepository {

    suspend fun getPosts(userId: Int?): Resource<List<Post>>
}