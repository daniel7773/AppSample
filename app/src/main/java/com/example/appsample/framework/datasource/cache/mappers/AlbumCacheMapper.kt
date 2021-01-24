package com.example.appsample.framework.datasource.cache.mappers

import com.example.appsample.business.data.models.AlbumEntity
import com.example.appsample.business.data.util.EntityMapper
import com.example.appsample.framework.datasource.cache.model.AlbumCacheEntity
import com.example.appsample.framework.utils.DataHelper
import javax.inject.Inject

/**
 * Maps AlbumEntity to AlbumCacheEntity or AlbumCacheEntity to AlbumEntity.
 */
class AlbumCacheMapper
@Inject
constructor() : EntityMapper<AlbumEntity, AlbumCacheEntity> {

    fun cacheEntityListToEntityList(entities: List<AlbumCacheEntity>): List<AlbumEntity> {
        val list: ArrayList<AlbumEntity> = ArrayList()
        for (entity in entities) {
            list.add(mapFromCacheEntity(entity))
        }
        return list
    }

    fun entityListToCacheEntityList(albumList: List<AlbumEntity>): List<AlbumCacheEntity> {
        val entities: ArrayList<AlbumCacheEntity> = ArrayList()
        for (album in albumList) {
            entities.add(mapToCacheEntity(album))
        }
        return entities
    }

    override fun mapToCacheEntity(entity: AlbumEntity): AlbumCacheEntity {
        return AlbumCacheEntity(
            id = entity.id,
            user_id = entity.userId ?: 0,
            title = entity.title ?: "NULL",
            DataHelper.getData(),
            DataHelper.getData()
        )
    }

    override fun mapFromCacheEntity(cacheEntity: AlbumCacheEntity): AlbumEntity {
        return AlbumEntity(
            id = cacheEntity.id,
            userId = cacheEntity.user_id ?: 0,
            title = cacheEntity.title ?: "NULL"
        )
    }
}







