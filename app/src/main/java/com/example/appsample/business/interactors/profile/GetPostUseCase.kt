package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostUseCase @Inject constructor(private val postsRepository: PostsRepository) {

    suspend fun getPost(postId: Int): Flow<Post?> {
        return postsRepository.getPost(postId)
    }
}