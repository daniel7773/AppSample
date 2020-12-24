package com.example.appsample.framework.datasource.cache.mappers

import com.example.appsample.business.data.models.PhotoEntity
import com.example.appsample.business.data.util.EntityMapper
import com.example.appsample.framework.datasource.cache.model.PhotoCacheEntity
import javax.inject.Inject

/**
 * Maps PhotoEntity to PhotoCacheEntity or PhotoCacheEntity to PhotoEntity.
 */
class PhotoCacheMapper
@Inject
constructor() : EntityMapper<PhotoEntity, PhotoCacheEntity> {

    fun cacheEntityListToEntityList(entities: List<PhotoCacheEntity>): List<PhotoEntity> {
        val list: ArrayList<PhotoEntity> = ArrayList()
        for (entity in entities) {
            list.add(mapFromCacheEntity(entity))
        }
        return list
    }

    fun entityListToCacheEntityList(comments: List<PhotoEntity>): List<PhotoCacheEntity> {
        val entities: ArrayList<PhotoCacheEntity> = ArrayList()
        for (comment in comments) {
            entities.add(mapToCacheEntity(comment))
        }
        return entities
    }

    override fun mapToCacheEntity(entity: PhotoEntity): PhotoCacheEntity {
        return PhotoCacheEntity(
            id = entity.id,
            album_id = entity.albumId ?: 0,
            title = entity.title ?: "NULL",
            url = entity.url ?: "NULL",
            thumbnail_url = entity.thumbnailUrl ?: "NULL",
            updated_at = "NOW",
            created_at = "NOW" // TODO: FIX
        )
    }

    override fun mapFromCacheEntity(cacheEntity: PhotoCacheEntity): PhotoEntity {
        return PhotoEntity(
            id = cacheEntity.id,
            albumId = cacheEntity.album_id ?: 0,
            title = cacheEntity.title ?: "NULL",
            url = cacheEntity.url ?: "NULL",
            thumbnailUrl = cacheEntity.thumbnail_url ?: "NULL"
        )
    }
}







