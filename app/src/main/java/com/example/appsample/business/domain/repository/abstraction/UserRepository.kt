package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUser(id: Int): Flow<Resource<User?>>
}