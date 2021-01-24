package com.example.appsample.framework.datasource.cache.abstraction

import com.example.appsample.business.data.models.UserEntity

interface UserDaoService {

    suspend fun insertUser(user: UserEntity): Long

    suspend fun searchUserById(id: String): UserEntity?

    suspend fun deleteUser(primaryKey: String): Int

    suspend fun updateUser(
        user: UserEntity,
        updated_at: String
    ): Int
}












