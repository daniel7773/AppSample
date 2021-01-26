package com.example.appsample.business.domain.repository

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException

const val GET_CACHE_TIMEOUT = 35000L
const val GET_NETWORK_TIMEOUT = 35000L

abstract class NetworkBoundResource<EntityObj, ResultObj>
constructor(
    private val cacheCall: suspend () -> EntityObj?,
    private val apiCall: suspend () -> EntityObj?
) {

    private val TAG = NetworkBoundResource::class.java.simpleName

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * @param ResultObj - object that is in use in domain layer
     * val for resulting all logic to Flow
     */
    val result: Flow<ResultObj?> = flow {
        // ****** STEP 1: VIEW CACHE ******
        val cache = returnCache()

        if(cache != null) {
            emit(cache)
        }

        if (!shouldFetch(cache)) {
            return@flow
        }
        val apiResult = safeApiCall(apiCall)

        emit(apiResult)
    }

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * @param ResultObj - object that is in use in domain layer
     * suspending fun for getting data
     */
    suspend fun resultSuspend(): ResultObj? {
        // ****** STEP 1: VIEW CACHE ******
        val cache = returnCache()

        if (!shouldFetch(cache)) {
            return cache
        }
        return safeApiCall(apiCall)
    }

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * @param ResultObj - object that is in use in domain layer
     * function for getting abstract data from server and calling cache update
     */
    suspend fun safeApiCall(
        apiCall: suspend () -> EntityObj?
    ): ResultObj? {
        try {
            return withTimeout(GET_NETWORK_TIMEOUT) {
                val apiResult: EntityObj? = apiCall.invoke()

                if (apiResult == null) {
                    Log.d(TAG, "safeApiCall result is null")
                    null
                } else {
                    val success = map(apiResult)
                    updateCache(apiResult)
                    Log.d(TAG, "safeApiCall SUCCEED")
                    success
                }
            }
        } catch (e: Exception) {
            if (e !is CancellationException) {
                e.printStackTrace()
            }
            when (e) {
                is TimeoutCancellationException -> {
                    Log.e(TAG, "TimeoutCancellationException in safeApiCall")
                }
                is HttpException -> {
                    val code = e.code()
                    Log.e(TAG, "HttpException, code: $code")
                }
                else -> {
                    Log.e(TAG, "Unknown Exception")
                    e.printStackTrace()
                }

            }
            return null
        }
    }

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * @param ResultObj - object that is in use in domain layer
     * function for getting abstract data from database
     */
    private suspend fun returnCache(): ResultObj? {
        var cacheResult: EntityObj? = null

        try {
            cacheResult = withTimeout(GET_CACHE_TIMEOUT) {
                return@withTimeout cacheCall.invoke()
            }
        } catch (e: Exception) {
            if (e !is CancellationException) {
                e.printStackTrace()
            }

            if (e is TimeoutCancellationException) {
                Log.e(TAG, "TimeoutCancellationException in returnCache")
                return null
            }
            Log.e(TAG, "Exception in returnCache")
            return null
        }

        if (cacheResult == null) return null

        return map(cacheResult)
    }

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * @param ResultObj - object that is in use in domain layer
     * function for calling cache update
     */
    abstract suspend fun updateCache(entity: EntityObj)

    abstract suspend fun map(entity: EntityObj): ResultObj

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * @param ResultObj - object that is in use in domain layer
     * function for deciding should we update cache or not
     */
    abstract suspend fun shouldFetch(entity: ResultObj?): Boolean
}