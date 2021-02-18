package com.example.appsample.business.data.util

interface EntityMapper<Object, CacheEntity> {

    fun mapFromCacheEntity(cacheEntity: CacheEntity): Object

    fun mapToCacheEntity(obj: Object): CacheEntity
}