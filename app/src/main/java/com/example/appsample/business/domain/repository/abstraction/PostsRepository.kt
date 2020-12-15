package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Post

interface PostsRepository {

    suspend fun getPostsList(userId: Int?): Resource<List<Post>?>
}