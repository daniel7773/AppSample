package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {
    suspend fun getCommentList(postId: Int): Flow<List<Comment>?>
    suspend fun getCommentsNum(postId: Int): Int?
}