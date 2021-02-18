package com.example.appsample.framework.datasource.cache.abstraction

import com.example.appsample.business.domain.model.User


interface UserDaoService {

    suspend fun insertUser(user: User): Long

    suspend fun searchUserById(id: String): User?

    suspend fun deleteUser(primaryKey: String): Int

    suspend fun updateUser(
        user: User,
        updated_at: String
    ): Int
}












