package com.example.appsample.business.domain.repository.implementation

import android.util.Log
import com.example.appsample.business.data.cache.abstraction.UserCacheSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserDataToUserMapper
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.state.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val userCacheSource: UserCacheSource,
    private val jsonPlaceholderApiSource: JsonPlaceholderApiSource
) : UserRepository {

    private val TAG = "UserRepositoryImpl"

    override suspend fun getUser(id: Int): Flow<DataState<User?>> = flow {
        val cache: User? = getCacheData(id)

        if (cache != null) {
            emit(DataState.Loading<User?>(cache, "Data from cache"))
        } else {
            Log.d(TAG, "Value from cache is empty")
        }

        val networkData = getNetworkData(cache, id)
        if (networkData.data != null) {
            userCacheSource.insertUser(networkData.data!!)
        }
        emit(networkData)
    }.flowOn(ioDispatcher)

    private suspend fun getCacheData(id: Int): User? {
        var user: User?
        try {
            withTimeout(3000L) {
                user = userCacheSource.searchUserById(id.toString())
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting user from cache")
            }
            e.printStackTrace()
            user = null
        }
        return user
    }

    private suspend fun getNetworkData(cacheValue: User?, userId: Int): DataState<User?> {
        try {
            val user = withTimeout(15000L) {
                jsonPlaceholderApiSource.getUserAsync(userId).await()
            }
            return if (user != null) {
                DataState.Success(UserDataToUserMapper.map(user))
            } else {
                DataState.Error(null, "NULL came from network", NullPointerException())
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.e(TAG, "Timeout while getting user from network")
            }
            e.printStackTrace()
            return DataState.Error(cacheValue, "Can't get value from network", e)
        }
    }
}