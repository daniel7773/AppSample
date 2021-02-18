package com.example.appsample.framework.datasource.cache.mappers

import com.example.appsample.business.data.util.EntityMapper
import com.example.appsample.business.domain.model.Album
import com.example.appsample.framework.datasource.cache.model.AlbumCacheEntity
import com.example.appsample.framework.utils.DataHelper
import javax.inject.Inject

/**
 * Maps Album to AlbumCacheEntity or AlbumCacheEntity to Album.
 */
class AlbumCacheMapper @Inject constructor() : EntityMapper<Album, AlbumCacheEntity> {

    fun cacheEntityListToEntityList(entities: List<AlbumCacheEntity>): List<Album> {
        val list: ArrayList<Album> = ArrayList()
        for (entity in entities) {
            list.add(mapFromCacheEntity(entity))
        }
        return list
    }

    fun entityListToCacheEntityList(albumList: List<Album>): List<AlbumCacheEntity> {
        val entities: ArrayList<AlbumCacheEntity> = ArrayList()
        for (album in albumList) {
            entities.add(mapToCacheEntity(album))
        }
        return entities
    }

    override fun mapToCacheEntity(obj: Album): AlbumCacheEntity {
        return AlbumCacheEntity(
            id = obj.id,
            user_id = obj.userId ?: 0,
            title = obj.title ?: "NULL",
            DataHelper.getData(),
            DataHelper.getData()
        )
    }

    override fun mapFromCacheEntity(cacheEntity: AlbumCacheEntity): Album {
        return Album(
            id = cacheEntity.id,
            userId = cacheEntity.user_id ?: 0,
            title = cacheEntity.title ?: "NULL"
        )
    }
}







