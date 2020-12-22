package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import javax.inject.Inject

class GetPostListUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {

    suspend fun getPostList(userId: Int) = postsRepository.getPostsList(userId)
}