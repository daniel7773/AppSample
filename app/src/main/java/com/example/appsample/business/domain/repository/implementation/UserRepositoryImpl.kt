package com.example.appsample.business.domain.repository.implementation

import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.business.data.network.abstraction.GET_USER_TIMEOUT
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserEntityToUserMapper
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.abstraction.Resource
import com.example.appsample.business.domain.repository.abstraction.Resource.Error
import com.example.appsample.business.domain.repository.abstraction.Resource.Success
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
) : UserRepository {

    override suspend fun getUser(id: Int?): Resource<User?> {

        var userEntity: UserEntity? = null

        try {
            userEntity = withTimeout(GET_USER_TIMEOUT) {
                return@withTimeout jsonPlaceholderApiSource.getUser(id ?: 0).await()
            }
        } catch (e: Exception) {
            return Error(null, "catched error in try block of getUser", e)
        }

        if (userEntity == null) {
            return Error(null, "DATA IS NULL", NullPointerException())
        }

        return Success(UserEntityToUserMapper.map(userEntity), null)
    }
}