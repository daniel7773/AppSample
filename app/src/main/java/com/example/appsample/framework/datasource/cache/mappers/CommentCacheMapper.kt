package com.example.appsample.framework.datasource.cache.mappers

import com.example.appsample.business.data.util.EntityMapper
import com.example.appsample.business.domain.model.Comment
import com.example.appsample.framework.datasource.cache.model.CommentCacheEntity
import com.example.appsample.framework.utils.DataHelper
import javax.inject.Inject

/**
 * Maps Comment to CommentCacheEntity or CommentCacheEntity to Comment.
 */
class CommentCacheMapper @Inject constructor() : EntityMapper<Comment, CommentCacheEntity> {

    fun cacheEntityListToList(entities: List<CommentCacheEntity>): List<Comment> {
        val list: ArrayList<Comment> = ArrayList()
        for (entity in entities) {
            list.add(mapFromCacheEntity(entity))
        }
        return list
    }

    fun listToCacheEntityList(comments: List<Comment>): List<CommentCacheEntity> {
        val entities: ArrayList<CommentCacheEntity> = ArrayList()
        for (comment in comments) {
            entities.add(mapToCacheEntity(comment))
        }
        return entities
    }

    override fun mapToCacheEntity(obj: Comment): CommentCacheEntity {
        return CommentCacheEntity(
            id = obj.id,
            post_id = obj.postId ?: 0,
            name = obj.name ?: "NULL",
            email = obj.email ?: "NULL",
            body = obj.body ?: "NULL",
            DataHelper.getData(),
            DataHelper.getData()
        )
    }

    override fun mapFromCacheEntity(cacheEntity: CommentCacheEntity): Comment {
        return Comment(
            id = cacheEntity.id,
            postId = cacheEntity.post_id ?: 0,
            name = cacheEntity.name ?: "NULL",
            email = cacheEntity.email ?: "NULL",
            body = cacheEntity.body
        )
    }
}







