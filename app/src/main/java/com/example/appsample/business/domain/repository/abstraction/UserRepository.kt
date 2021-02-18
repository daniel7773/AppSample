package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUser(id: Int): Flow<DataState<User?>>
}