package com.example.appsample.framework.datasource.cache.mappers

import com.example.appsample.business.data.models.PostEntity
import com.example.appsample.business.data.util.EntityMapper
import com.example.appsample.framework.datasource.cache.model.PostCacheEntity
import javax.inject.Inject

/**
 * Maps PostEntity to PostCacheEntity or PostCacheEntity to PostEntity.
 */
class PostCacheMapper
@Inject
constructor() : EntityMapper<PostEntity, PostCacheEntity> {

    fun cacheEntityListToEntityList(entities: List<PostCacheEntity>): List<PostEntity> {
        val list: ArrayList<PostEntity> = ArrayList()
        for (entity in entities) {
            list.add(mapFromCacheEntity(entity))
        }
        return list
    }

    fun entityListToCacheEntityList(posts: List<PostEntity>): List<PostCacheEntity> {
        val entities: ArrayList<PostCacheEntity> = ArrayList()
        for (post in posts) {
            entities.add(mapToCacheEntity(post))
        }
        return entities
    }

    override fun mapToCacheEntity(entity: PostEntity): PostCacheEntity {
        return PostCacheEntity(
            id = entity.id,
            user_id = entity.userId ?: 0,
            title = entity.title ?: "NULL",
            body = entity.body ?: "NULL",
            updated_at = "NOW",
            created_at = "NOW" // TODO: FIX
        )
    }

    override fun mapFromCacheEntity(cacheEntity: PostCacheEntity): PostEntity {
        return PostEntity(
            id = cacheEntity.id,
            userId = cacheEntity.user_id,
            title = cacheEntity.title,
            body = cacheEntity.body
        )
    }
}







