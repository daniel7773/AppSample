package com.example.appsample.framework.datasource.cache.mappers

import com.example.appsample.business.data.util.EntityMapper
import com.example.appsample.business.domain.model.Address
import com.example.appsample.business.domain.model.Company
import com.example.appsample.business.domain.model.Geo
import com.example.appsample.business.domain.model.User
import com.example.appsample.framework.datasource.cache.model.UserCacheEntity
import com.example.appsample.framework.utils.DataHelper
import javax.inject.Inject

/**
 * Maps User to UserCacheEntity or UserCacheEntity to User.
 */
class UserCacheMapper @Inject constructor() : EntityMapper<User, UserCacheEntity> {

    fun cacheEntityListToList(entities: List<UserCacheEntity>): List<User> {
        val list: ArrayList<User> = ArrayList()
        for (entity in entities) {
            list.add(mapFromCacheEntity(entity))
        }
        return list
    }

    fun listToCacheEntityList(users: List<User>): List<UserCacheEntity> {
        val entities: ArrayList<UserCacheEntity> = ArrayList()
        for (user in users) {
            entities.add(mapToCacheEntity(user))
        }
        return entities
    }

    override fun mapToCacheEntity(obj: User): UserCacheEntity {
        return UserCacheEntity(
            id = obj.id,
            username = obj.username ?: "NULL",
            email = obj.email ?: "NULL",
            street = obj.address?.street ?: "NULL",
            suite = obj.address?.suite ?: "NULL",
            city = obj.address?.city ?: "NULL",
            zipcode = obj.address?.zipcode ?: "NULL",
            lat = obj.address?.geo?.lat ?: 0.0,
            lng = obj.address?.geo?.lng ?: 0.0,
            phone = obj.phone ?: "NULL",
            website = obj.website ?: "NULL",
            company_name = obj.company?.name ?: "NULL",
            catch_phrase = obj.company?.catchPhrase ?: "NULL",
            bs = obj.company?.bs ?: "NULL",
            updated_at = DataHelper.getData(),
            created_at = DataHelper.getData()
        )
    }

    override fun mapFromCacheEntity(cacheEntity: UserCacheEntity): User {
        return User(
            id = cacheEntity.id,
            username = cacheEntity.username,
            email = cacheEntity.email,
            address = Address(
                street = cacheEntity.street,
                suite = cacheEntity.suite,
                city = cacheEntity.city,
                zipcode = cacheEntity.zipcode,
                geo = Geo(
                    lat = cacheEntity.lat,
                    lng = cacheEntity.lng
                )
            ),
            phone = cacheEntity.phone,
            website = cacheEntity.website,
            company = Company(
                name = cacheEntity.company_name,
                catchPhrase = cacheEntity.catch_phrase,
                bs = cacheEntity.bs
            )
        )
    }
}







