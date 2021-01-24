package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Comment
import com.example.appsample.business.domain.repository.Resource
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {
    suspend fun getCommentList(postId: Int): Flow<Resource<List<Comment>?>>
    suspend fun getCommentsNum(postId: Int): Resource<Int?>
}