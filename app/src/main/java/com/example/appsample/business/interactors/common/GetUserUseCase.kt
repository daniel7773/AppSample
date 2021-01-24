package com.example.appsample.business.interactors.common

import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun getUser(id: Int): Flow<Resource<User?>> {
        return userRepository.getUser(id)
    }
}