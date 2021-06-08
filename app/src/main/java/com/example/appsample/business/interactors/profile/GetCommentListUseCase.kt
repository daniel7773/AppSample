package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.model.Comment
import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommentListUseCase @Inject constructor(private val commentsRepository: CommentsRepository) {

    suspend fun getCommentList(postId: Int): Flow<DataState<List<Comment>?>> {
        return commentsRepository.getCommentList(postId)
    }
}