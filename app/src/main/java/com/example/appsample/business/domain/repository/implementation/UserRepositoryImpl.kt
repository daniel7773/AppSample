package com.example.appsample.business.domain.repository.implementation

import com.example.appsample.business.data.cache.abstraction.UserCacheDataSource
import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.business.data.network.abstraction.GET_USER_TIMEOUT
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserEntityToUserMapper
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.Resource.Error
import com.example.appsample.business.domain.repository.Resource.Success
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userCacheDataSource: UserCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource,
) : UserRepository {

    override suspend fun getUser(id: Int): Resource<User?> {

        val cacheResult = userCacheDataSource.searchUserById(id.toString())

        if (cacheResult != null) {
            val user = UserEntityToUserMapper.map(cacheResult)

            return Success(user, "Success")
        }
        var userEntity: UserEntity? = null

        try {
            userEntity = withTimeout(GET_USER_TIMEOUT) {
                return@withTimeout jsonPlaceholderApiSource.getUserAsync(id).await()
            }
        } catch (e: Exception) {
            return Error(null, "Catch error while calling getUser", e)
        }

        if (userEntity == null) {
            return Error(null, "Data from repository is null", NullPointerException())
        }

        userCacheDataSource.insertUser(userEntity)
        val user = UserEntityToUserMapper.map(userEntity)

        return Success(user, "Success")
    }
}