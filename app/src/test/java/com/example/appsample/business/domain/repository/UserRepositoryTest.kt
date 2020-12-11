package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.network.FORCE_GET_EXCEPTION
import com.example.appsample.business.data.network.FORCE_GET_TIMEOUT_EXCEPTION
import com.example.appsample.business.data.network.FakeJsonPlaceHolderApiSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserEntityToUserMapper
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.repository.implementation.UserRepositoryImpl
import junit.framework.Assert
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

@InternalCoroutinesApi
class UserRepositoryTest {

    private val userRepository: UserRepository
    private val networkApi: JsonPlaceholderApiSource

    init {
        networkApi = FakeJsonPlaceHolderApiSource()
        userRepository = UserRepositoryImpl(networkApi)
    }

    @Test
    fun getUser_success() = runBlocking {

        val userId = 1

        val networkValue = networkApi.getUser(userId).await()
        val repositoryValue = userRepository.getUser(userId)

        Assert.assertEquals(UserEntityToUserMapper.map(networkValue!!), repositoryValue.data)
    }

    @Test
    fun getUser_passedErrorCorrectly() = runBlocking {

        val userId = FORCE_GET_EXCEPTION

        var exception: Exception = Exception("Any")
        val repositoryValue = userRepository.getUser(userId)
        try {
            networkApi.getUser(userId).await()
        } catch (e: Exception) {
            exception = e
        }

        Assert.assertEquals(exception.javaClass, repositoryValue.exception?.javaClass)
        Assert.assertEquals(exception.message, repositoryValue.exception?.message)
        Assert.assertEquals(exception.localizedMessage, repositoryValue.exception?.localizedMessage)
    }

    @Test
    fun getUser_timeoutCheck() = runBlocking {

        val userId = FORCE_GET_TIMEOUT_EXCEPTION

        val repositoryValue = userRepository.getUser(userId)

        Assert.assertNotNull(repositoryValue.exception)
        Assert.assertEquals(
            TimeoutCancellationException::class.java,
            repositoryValue.exception!!.javaClass
        )
    }

}