package com.example.appsample.framework.datasource.cache.mappers

import com.example.appsample.business.data.models.AddressEntity
import com.example.appsample.business.data.models.CompanyEntity
import com.example.appsample.business.data.models.GeoEntity
import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.business.data.util.EntityMapper
import com.example.appsample.framework.datasource.cache.model.UserCacheEntity
import javax.inject.Inject

/**
 * Maps User to UserCacheEntity or UserCacheEntity to User.
 */
class UserCacheMapper
@Inject
constructor() : EntityMapper<UserEntity, UserCacheEntity> {

    fun cacheEntityListToEntityList(entities: List<UserCacheEntity>): List<UserEntity> {
        val list: ArrayList<UserEntity> = ArrayList()
        for (entity in entities) {
            list.add(mapFromCacheEntity(entity))
        }
        return list
    }

    fun entityListToCacheEntityList(users: List<UserEntity>): List<UserCacheEntity> {
        val entities: ArrayList<UserCacheEntity> = ArrayList()
        for (user in users) {
            entities.add(mapToCacheEntity(user))
        }
        return entities
    }

    override fun mapToCacheEntity(entity: UserEntity): UserCacheEntity {
        return UserCacheEntity(
            id = entity.id,
            username = entity.username ?: "NULL",
            email = entity.email ?: "NULL",
            street = entity.address?.street ?: "NULL",
            suite = entity.address?.suite ?: "NULL",
            city = entity.address?.city ?: "NULL",
            zipcode = entity.address?.zipcode ?: "NULL",
            lat = entity.address?.geo?.lat ?: 0.0,
            lng = entity.address?.geo?.lng ?: 0.0,
            phone = entity.phone ?: "NULL",
            website = entity.website ?: "NULL",
            company_name = entity.company?.name ?: "NULL",
            catch_phrase = entity.company?.catchPhrase ?: "NULL",
            bs = entity.company?.bs ?: "NULL",
            updated_at = "NOW",
            created_at = "NOW" // TODO: FIX
        )
    }

    override fun mapFromCacheEntity(cacheEntity: UserCacheEntity): UserEntity {
        return UserEntity(
            id = cacheEntity.id,
            username = cacheEntity.username,
            email = cacheEntity.email,
            address = AddressEntity(
                street = cacheEntity.street,
                suite = cacheEntity.suite,
                city = cacheEntity.city,
                zipcode = cacheEntity.zipcode,
                geo = GeoEntity(
                    lat = cacheEntity.lat,
                    lng = cacheEntity.lng
                )
            ),
            phone = cacheEntity.phone,
            website = cacheEntity.website,
            company = CompanyEntity(
                name = cacheEntity.company_name,
                catchPhrase = cacheEntity.catch_phrase,
                bs = cacheEntity.bs
            )
        )
    }
}







