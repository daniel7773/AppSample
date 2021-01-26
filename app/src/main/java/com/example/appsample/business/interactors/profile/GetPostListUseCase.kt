package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostListUseCase @Inject constructor(private val postsRepository: PostsRepository) {

    suspend fun getPostList(userId: Int): Flow<List<Post>?> {
        return postsRepository.getPostsList(userId)
    }
}