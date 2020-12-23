package com.example.appsample.framework.datasource.cache.implementation

import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.framework.datasource.cache.abstraction.UserDaoService
import com.example.appsample.framework.datasource.cache.database.UserDao
import com.example.appsample.framework.datasource.cache.mappers.CacheMapper
import javax.inject.Inject

class UserDaoServiceImpl
@Inject
constructor(
    private val userDao: UserDao,
    private val userMapper: CacheMapper
) : UserDaoService {
    override suspend fun insertUser(user: UserEntity) =
        userDao.insertUser(userMapper.mapToCacheEntity(user))

    override suspend fun searchUserById(id: String) = userDao.searchUserById(id)?.run {
        userMapper.mapFromCacheEntity(this)
    }

    override suspend fun deleteUser(primaryKey: String) = userDao.deleteUser(primaryKey)

    override suspend fun updateUser(user: UserEntity, updated_at: String): Int {
        val cacheUser = userMapper.mapToCacheEntity(user)
        return userDao.updateUser(
            primaryKey = cacheUser.id.toString(),
            username = cacheUser.username,
            email = cacheUser.email,
            street = cacheUser.email,
            suite = cacheUser.email,
            city = cacheUser.email,
            zipcode = cacheUser.email,
            lat = cacheUser.lat,
            lng = cacheUser.lng,
            phone = cacheUser.phone,
            website = cacheUser.website,
            company_name = cacheUser.company_name,
            catch_phrase = cacheUser.catch_phrase,
            bs = cacheUser.bs,
            updated_at = cacheUser.updated_at
        )
    }
}











