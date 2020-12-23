package com.example.appsample.business.data.cache.abstraction

import com.example.appsample.business.data.models.UserEntity

interface UserCacheDataSource {

    suspend fun insertUser(user: UserEntity): Long

    suspend fun searchUserById(id: String): UserEntity?

    suspend fun deleteUser(id: String): Int

    suspend fun updateUser(
        user: UserEntity,
        updated_at: String
    ): Int
}






