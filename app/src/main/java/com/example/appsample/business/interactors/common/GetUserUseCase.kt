package com.example.appsample.business.interactors.common

import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun getUser(id: Int): Flow<DataState<User?>> {
        return userRepository.getUser(id)
    }
}