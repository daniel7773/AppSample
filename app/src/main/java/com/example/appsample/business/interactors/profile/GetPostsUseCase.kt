package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {

    suspend fun getPosts(userId: Int?) = postsRepository.getPosts(userId)
}