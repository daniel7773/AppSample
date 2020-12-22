package com.example.appsample.business.domain.repository.implementation

import com.example.appsample.business.data.models.CommentEntity
import com.example.appsample.business.data.network.abstraction.GET_ALBUMS_TIMEOUT
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.CommentEntityToCommentMapper
import com.example.appsample.business.domain.model.Comment
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.abstraction.CommentsRepository
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource
) : CommentsRepository {

    override suspend fun getCommentList(postId: Int): Resource<List<Comment>?> {

        var commentEntityList: List<CommentEntity>? = null

        try {
            commentEntityList = withTimeout(GET_ALBUMS_TIMEOUT) {
                return@withTimeout jsonPlaceholderApiSource.getCommentsByPostIdAsync(postId)
                    .await()
            }
        } catch (e: Exception) {
            return Resource.Error(null, "Catch error while calling getComments", e)
        }

        if (commentEntityList == null) {
            return Resource.Error(null, "Data from repository is null", NullPointerException())
        }

        val commentList =
            CommentEntityToCommentMapper.map(commentEntityList).filter { it.id != null }

        return Resource.Success(commentList, "Success")
    }
}