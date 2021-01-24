package com.example.appsample.business.data.util

interface EntityMapper<Entity, CacheEntity> {

    fun mapFromCacheEntity(cacheEntity: CacheEntity): Entity

    fun mapToCacheEntity(entity: Entity): CacheEntity
}