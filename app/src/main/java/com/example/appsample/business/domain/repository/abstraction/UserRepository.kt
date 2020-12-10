package com.example.appsample.business.domain.repository.abstraction

import com.example.appsample.business.domain.model.User

interface UserRepository {

    suspend fun getUser(id: Int?): Resource<User?>
}