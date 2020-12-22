package com.example.appsample.business.interactors.profile

import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import javax.inject.Inject

class GetCommentListUseCase @Inject constructor(
    private val commentsRepository: CommentsRepository
) {
    suspend fun getCommentList(postId: Int) = commentsRepository.getCommentList(postId)
}