package com.example.appsample.framework.datasource.cache.mappers

import com.example.appsample.business.data.util.EntityMapper
import com.example.appsample.business.domain.model.Post
import com.example.appsample.framework.datasource.cache.model.PostCacheEntity
import com.example.appsample.framework.utils.DataHelper
import javax.inject.Inject

/**
 * Maps Post to PostCacheEntity or PostCacheEntity to Post.
 */
class PostCacheMapper @Inject constructor() : EntityMapper<Post, PostCacheEntity> {

    fun cacheEntityListToList(entities: List<PostCacheEntity>): List<Post> {
        val list: ArrayList<Post> = ArrayList()
        for (entity in entities) {
            list.add(mapFromCacheEntity(entity))
        }
        return list
    }

    fun listToCacheEntityList(posts: List<Post>): List<PostCacheEntity> {
        val entities: ArrayList<PostCacheEntity> = ArrayList()
        for (post in posts) {
            entities.add(mapToCacheEntity(post))
        }
        return entities
    }

    override fun mapToCacheEntity(obj: Post): PostCacheEntity {
        return PostCacheEntity(
            id = obj.id,
            user_id = obj.userId ?: 0,
            title = obj.title ?: "NULL",
            body = obj.body ?: "NULL",
            DataHelper.getData(),
            DataHelper.getData()
        )
    }

    override fun mapFromCacheEntity(cacheEntity: PostCacheEntity): Post {
        return Post(
            id = cacheEntity.id,
            userId = cacheEntity.user_id,
            title = cacheEntity.title,
            body = cacheEntity.body
        )
    }
}







