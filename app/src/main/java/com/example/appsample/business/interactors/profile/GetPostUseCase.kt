package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {

    suspend fun getPost(postId: Int) = postsRepository.getPost(postId)
}