package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostListUseCase @Inject constructor(private val postsRepository: PostsRepository) {

    suspend fun getPostList(userId: Int): Flow<DataState<List<Post>?>> {
        return postsRepository.getPostsList(userId)
    }
}