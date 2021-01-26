package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.UserCacheDataSource
import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserEntityToUserMapper
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.NetworkBoundResource
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val userCacheDataSource: UserCacheDataSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource
) : UserRepository {

    private val TAG = "UserRepositoryImpl"

    override suspend fun getUser(id: Int): Flow<User?> {

        return object : NetworkBoundResource<UserEntity, User>(
            { userCacheDataSource.searchUserById(id.toString()) },
            { jsonPlaceholderApiSource.getUserAsync(id).await() },

            ) {
            override suspend fun updateCache(entity: UserEntity) {
                Log.d(TAG, "updateCache called for user with id: ${entity.id}")
                userCacheDataSource.insertUser(entity)
                // TODO: call WorkManager
            }

            // TODO: Set any logical solution here, now it is set fake condition here since it's sample
            //       we know that user data is not updating on server
            override suspend fun shouldFetch(entity: User?): Boolean = entity == null

            override suspend fun map(entity: UserEntity) = UserEntityToUserMapper.map(entity)
        }.result
    }
}