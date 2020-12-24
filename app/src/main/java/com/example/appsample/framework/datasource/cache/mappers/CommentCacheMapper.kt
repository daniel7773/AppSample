package com.example.appsample.framework.datasource.cache.mappers

import com.example.appsample.business.data.models.CommentEntity
import com.example.appsample.business.data.util.EntityMapper
import com.example.appsample.framework.datasource.cache.model.CommentCacheEntity
import javax.inject.Inject

/**
 * Maps CommentEntity to CommentCacheEntity or CommentCacheEntity to CommentEntity.
 */
class CommentCacheMapper
@Inject
constructor() : EntityMapper<CommentEntity, CommentCacheEntity> {

    fun cacheEntityListToEntityList(entities: List<CommentCacheEntity>): List<CommentEntity> {
        val list: ArrayList<CommentEntity> = ArrayList()
        for (entity in entities) {
            list.add(mapFromCacheEntity(entity))
        }
        return list
    }

    fun entityListToCacheEntityList(comments: List<CommentEntity>): List<CommentCacheEntity> {
        val entities: ArrayList<CommentCacheEntity> = ArrayList()
        for (comment in comments) {
            entities.add(mapToCacheEntity(comment))
        }
        return entities
    }

    override fun mapToCacheEntity(entity: CommentEntity): CommentCacheEntity {
        return CommentCacheEntity(
            id = entity.id,
            post_id = entity.postId ?: 0,
            name = entity.name ?: "NULL",
            email = entity.email ?: "NULL",
            body = entity.body ?: "NULL",
            updated_at = "NOW",
            created_at = "NOW" // TODO: FIX
        )
    }

    override fun mapFromCacheEntity(cacheEntity: CommentCacheEntity): CommentEntity {
        return CommentEntity(
            id = cacheEntity.id,
            postId = cacheEntity.post_id ?: 0,
            name = cacheEntity.name ?: "NULL",
            email = cacheEntity.email ?: "NULL",
            body = cacheEntity.body
        )
    }
}







