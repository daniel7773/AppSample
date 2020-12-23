package com.example.appsample.business.data.cache.implementation

import com.example.appsample.business.data.cache.abstraction.UserCacheDataSource
import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.framework.datasource.cache.abstraction.UserDaoService
import javax.inject.Inject

class UserCacheDataSourceImpl
@Inject
constructor(
    private val userDaoService: UserDaoService
) : UserCacheDataSource {

    override suspend fun insertUser(user: UserEntity): Long {
        return userDaoService.insertUser(user)
    }

    override suspend fun searchUserById(id: String) = userDaoService.searchUserById(id)

    override suspend fun deleteUser(id: String): Int {
        return userDaoService.deleteUser(id)
    }

    override suspend fun updateUser(user: UserEntity, updated_at: String): Int {
        return userDaoService.updateUser(user, updated_at)
    }


}