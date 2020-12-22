package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.repository.Resource

interface PostsRepository {

    suspend fun getPostsList(userId: Int): Resource<List<Post>?>

    suspend fun getPost(postId: Int): Resource<Post?>
}