package com.example.appsample.framework.datasource.cache.mappers

import com.example.appsample.business.data.util.EntityMapper
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.framework.datasource.cache.model.PhotoCacheEntity
import com.example.appsample.framework.utils.DataHelper
import javax.inject.Inject

/**
 * Maps Photo to PhotoCacheEntity or PhotoCacheEntity to Photo.
 */
class PhotoCacheMapper
@Inject
constructor() : EntityMapper<Photo, PhotoCacheEntity> {

    fun cacheEntityListToList(entities: List<PhotoCacheEntity>): List<Photo> {
        val list: ArrayList<Photo> = ArrayList()
        for (entity in entities) {
            list.add(mapFromCacheEntity(entity))
        }
        return list
    }

    fun listToCacheEntityList(comments: List<Photo>): List<PhotoCacheEntity> {
        val entities: ArrayList<PhotoCacheEntity> = ArrayList()
        for (comment in comments) {
            entities.add(mapToCacheEntity(comment))
        }
        return entities
    }

    override fun mapToCacheEntity(obj: Photo): PhotoCacheEntity {
        return PhotoCacheEntity(
            id = obj.id,
            album_id = obj.albumId ?: 0,
            title = obj.title ?: "NULL",
            url = obj.url ?: "NULL",
            thumbnail_url = obj.thumbnailUrl ?: "NULL",
            DataHelper.getData(),
            DataHelper.getData()
        )
    }

    override fun mapFromCacheEntity(cacheEntity: PhotoCacheEntity): Photo {
        return Photo(
            id = cacheEntity.id,
            albumId = cacheEntity.album_id ?: 0,
            title = cacheEntity.title ?: "NULL",
            url = cacheEntity.url ?: "NULL",
            thumbnailUrl = cacheEntity.thumbnail_url ?: "NULL"
        )
    }
}







