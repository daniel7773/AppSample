package com.example.appsample.business.domain.repository

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

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * @param ResultObj - object that is in use in domain layer
     * val for resulting all logic to Flow
     */
    val result: Flow<Resource<ResultObj?>> = flow {
        // ****** STEP 1: VIEW CACHE ******
        val cache = returnCache()
        emit(cache)
        if (cache is Resource.Success) {
            if (!shouldFetch(cache.data)) {
                return@flow
            }
        }
        val apiResult = safeApiCall(apiCall)

        emit(apiResult)
    }

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * @param ResultObj - object that is in use in domain layer
     * suspending fun for getting data
     */
    suspend fun resultSuspend(): Resource<ResultObj?> {
        // ****** STEP 1: VIEW CACHE ******
        val cache = returnCache()

        if (cache is Resource.Success) {
            if (!shouldFetch(cache.data)) {
                return cache
            }
        }
        val apiResult = safeApiCall(apiCall)

        return apiResult
    }

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * @param ResultObj - object that is in use in domain layer
     * function for getting abstract data from server and calling cache update
     */
    suspend fun safeApiCall(
        apiCall: suspend () -> EntityObj?
    ): Resource<ResultObj?> {
        try {
            return withTimeout(GET_NETWORK_TIMEOUT) {
                val apiResult: EntityObj? = apiCall.invoke()

                if (apiResult == null) {
                    Resource.Error("NULL came from api", NullPointerException())
                } else {
                    val success = map(apiResult)
                    updateCache(apiResult)
                    joinAll()
                    Resource.Success(success, "Success")
                }
            }
        } catch (e: Exception) {
            if (e !is CancellationException) {
                e.printStackTrace()
            }
            return when (e) {
                is TimeoutCancellationException -> {
                    Resource.Error("NETWORK_ERROR_TIMEOUT", e)
                }
                is HttpException -> {
                    val code = e.code()
                    Resource.Error("Error code: ${code}", e)
                }
                else -> {
                    Resource.Error("Unknown Error", e)
                }
            }
        }
    }

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * @param ResultObj - object that is in use in domain layer
     * function for getting abstract data from database
     */
    private suspend fun returnCache(): Resource<ResultObj?> {
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
                return Resource.Error("Some problems with cache, it is loading too long", e)
            }
            return Resource.Error("Error from cache", e)
        }

        if (cacheResult == null) {
            return Resource.Error("Looks like cache is empty", NullPointerException())
        }

        return Resource.Success(map(cacheResult), "Success")
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