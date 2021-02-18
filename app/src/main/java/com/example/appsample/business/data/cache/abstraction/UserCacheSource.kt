package com.example.appsample.business.data.cache.abstraction

import com.example.appsample.business.domain.model.User


interface UserCacheSource {

    suspend fun insertUser(user: User): Long

    suspend fun searchUserById(id: String): User?

    suspend fun deleteUser(id: String): Int

    suspend fun updateUser(
        user: User,
        updated_at: String
    ): Int
}






