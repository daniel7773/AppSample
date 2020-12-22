package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.Comment
import com.example.appsample.business.domain.repository.Resource

interface CommentsRepository {
    suspend fun getCommentList(postId: Int): Resource<List<Comment>?>
}