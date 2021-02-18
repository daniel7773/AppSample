package com.example.appsample.business.data.cache.implementation

import com.example.appsample.business.data.cache.abstraction.UserCacheSource
import com.example.appsample.business.domain.model.User
import com.example.appsample.framework.datasource.cache.abstraction.UserDaoService
import javax.inject.Inject

class UserCacheSourceImpl
@Inject
constructor(
    private val userDaoService: UserDaoService
) : UserCacheSource {

    override suspend fun insertUser(user: User): Long {
        return userDaoService.insertUser(user)
    }

    override suspend fun searchUserById(id: String) = userDaoService.searchUserById(id)

    override suspend fun deleteUser(id: String): Int {
        return userDaoService.deleteUser(id)
    }

    override suspend fun updateUser(user: User, updated_at: String): Int {
        return userDaoService.updateUser(user, updated_at)
    }


}