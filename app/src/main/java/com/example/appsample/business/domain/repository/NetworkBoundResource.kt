package com.example.appsample.business.domain.repository

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException

const val GET_CACHE_TIMEOUT = 20000L
const val GET_NETWORK_TIMEOUT = 45000L

abstract class NetworkBoundResource<EntityObj, ResultObj>
constructor(
    private val dispatcher: CoroutineDispatcher,
    private val cacheCall: suspend () -> EntityObj?,
    private val apiCall: suspend () -> EntityObj?
) {

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * val for resulting all logic to Flow
     */
    val result: Flow<Resource<ResultObj?>> = flow {
        // ****** STEP 1: VIEW CACHE ******
        Log.d("Asdasddwc", "flow result collecting...")
        val cache = returnCache()
        emit(cache)
        if (cache is Resource.Success) {
            Log.d("Asdasddwc", "cache is Resource.Success")
            if (!shouldFetch(cache.data)) {
                Log.d("Asdasddwc", "returning from flow")
                return@flow
            }
        }
        Log.d("Asdasddwc", "calling api")
        val apiResult = safeApiCall(apiCall, dispatcher)

        emit(apiResult)
    }

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * suspending fun for getting data
     */
    suspend fun resultSuspend(): Resource<ResultObj?> {
        // ****** STEP 1: VIEW CACHE ******
        Log.d("Asdasddwc", "flow result collecting...")
        val cache = returnCache()

        if (cache is Resource.Success) {
            Log.d("Asdasddwc", "cache is Resource.Success")
            if (!shouldFetch(cache.data)) {
                Log.d("Asdasddwc", "returning from resultSuspend")
                return cache
            }
        }
        Log.d("Asdasddwc", "calling api")
        val apiResult = safeApiCall(apiCall, dispatcher)

        return apiResult
    }

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * function for getting abstract data from server and calling cache update
     */
    suspend fun safeApiCall(
        apiCall: suspend () -> EntityObj?,
        dispatcher: CoroutineDispatcher
    ): Resource<ResultObj?> {
        Log.d("Asdasddwc", "safeApiCall called")
        return withContext(dispatcher) {
            try {
                withTimeout(GET_NETWORK_TIMEOUT) {
                    val apiResult: EntityObj? = apiCall.invoke()

                    return@withTimeout if (apiResult == null) {
                        Resource.Error("NULL came from api", NullPointerException())
                    } else {
                        val success = map(apiResult)
                        Log.d("Asdasddwc", "apiResult is Resource.Success")
                        updateCache(apiResult)
                        joinAll()
                        Resource.Success(success, "Success")
                    }
                }
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    e.printStackTrace()
                }
                when (e) {
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
    }

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * function for getting abstract data from database
     */
    private suspend fun returnCache(): Resource<ResultObj?> {
        val cacheResult: EntityObj?
        Log.d("Asdasddwc", "returnCache called")
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
     * function for calling cache update
     */
    abstract suspend fun updateCache(entity: EntityObj)

    abstract suspend fun map(entity: EntityObj): ResultObj

    /**
     * @param EntityObj - object that cache and network dataSources are using
     * function for deciding should we update cache or not
     */
    abstract suspend fun shouldFetch(entity: ResultObj?): Boolean
}