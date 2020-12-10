package com.example.appsample.business.domain.repository.implementation

import com.example.appsample.business.data.network.abstraction.GET_USER_TIMEOUT
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserEntityToUserMapper
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.repository.abstraction.Resource
import com.example.appsample.business.domain.repository.abstraction.Resource.Error
import com.example.appsample.business.domain.repository.abstraction.Resource.Success
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
) : UserRepository {

    override suspend fun getUser(id: Int?): Resource<User?> {
        val userEntity = withTimeoutOrNull(GET_USER_TIMEOUT) {
            return@withTimeoutOrNull jsonPlaceholderApiSource.getUser(id ?: 0).await()
        }
        if (userEntity == null) {
            return Error(null, "DATA IS NULL", NullPointerException())
        }

        return Success(UserEntityToUserMapper.map(userEntity), null)
    }
}