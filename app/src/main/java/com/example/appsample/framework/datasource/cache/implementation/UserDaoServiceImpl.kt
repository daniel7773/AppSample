package com.example.appsample.framework.datasource.cache.implementation

import com.example.appsample.business.domain.model.User
import com.example.appsample.framework.datasource.cache.abstraction.UserDaoService
import com.example.appsample.framework.datasource.cache.database.UserDao
import com.example.appsample.framework.datasource.cache.mappers.UserCacheMapper
import javax.inject.Inject

class UserDaoServiceImpl
@Inject
constructor(
    private val userDao: UserDao,
    private val userMapperUser: UserCacheMapper
) : UserDaoService {
    override suspend fun insertUser(user: User) =
        userDao.insertUser(userMapperUser.mapToCacheEntity(user))

    override suspend fun searchUserById(id: String) = userDao.searchUserById(id)?.run {
        userMapperUser.mapFromCacheEntity(this)
    }

    override suspend fun deleteUser(primaryKey: String) = userDao.deleteUser(primaryKey)

    override suspend fun updateUser(user: User, updated_at: String): Int {
        val cacheUser = userMapperUser.mapToCacheEntity(user)
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











