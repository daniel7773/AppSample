package com.example.appsample.business.interactors.common

import com.example.appsample.business.domain.repository.abstraction.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun getUser(id: Int?) = userRepository.getUser(id)
}