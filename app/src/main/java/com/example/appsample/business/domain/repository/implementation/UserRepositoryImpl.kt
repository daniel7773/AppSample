package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.UserCacheDataSource
import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserEntityToUserMapper
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.NetworkBoundResource
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val userCacheDataSource: UserCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource
) : UserRepository {

    override suspend fun getUser(id: Int): Flow<Resource<User?>> {

        return object : NetworkBoundResource<UserEntity, User>(
            mainDispatcher,
            { userCacheDataSource.searchUserById(id.toString()) },
            { jsonPlaceholderApiSource.getUserAsync(id).await() },

            ) {
            override suspend fun updateCache(entity: UserEntity) {
                Log.d("Asdasddwc", "updateCache called for user with id: ${entity.id}")
                userCacheDataSource.insertUser(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here
            override suspend fun shouldFetch(entity: User?): Boolean = true

            override suspend fun map(entity: UserEntity) = UserEntityToUserMapper.map(entity)
        }.result
    }

    /**
     *
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
    return Error("Catch error while calling getUser", e)
    }

    if (userEntity == null) {
    return Error("Data from repository is null", NullPointerException())
    }

    userCacheDataSource.insertUser(userEntity)
    val user = UserEntityToUserMapper.map(userEntity)

    return Success(user, "Success")
     */
}