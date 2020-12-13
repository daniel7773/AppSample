package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.network.FORCE_GET_EXCEPTION
import com.example.appsample.business.data.network.FORCE_GET_TIMEOUT_EXCEPTION
import com.example.appsample.business.data.network.FakeJsonPlaceHolderApiSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserEntityToUserMapper
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.repository.implementation.UserRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class UserRepositoryTest {

    private val userRepository: UserRepository
    private val networkApi: JsonPlaceholderApiSource

    init {
        networkApi = FakeJsonPlaceHolderApiSource()
        userRepository = UserRepositoryImpl(networkApi)
    }

    @Test
    fun `getUser Success`() = runBlockingTest {

        val userId = 1

        val networkValue = networkApi.getUser(userId).await()
        val repositoryValue = userRepository.getUser(userId)

        Assertions.assertThat(UserEntityToUserMapper.map(networkValue!!))
            .isEqualTo(repositoryValue.data)
    }

    @Test
    fun `getUser Error passes through repository`() = runBlockingTest {

        val userId = FORCE_GET_EXCEPTION

        var exception: Exception = Exception("Any")
        val repositoryValue = userRepository.getUser(userId)
        try {
            networkApi.getUser(userId).await()
        } catch (e: Exception) {
            exception = e
        }

        Assertions.assertThat(repositoryValue.exception).isInstanceOf(exception::class.java)
        Assertions.assertThat(exception.message).isEqualTo(repositoryValue.exception?.message)
        Assertions.assertThat(exception.localizedMessage)
            .isEqualTo(repositoryValue.exception?.localizedMessage)
    }

    @Test
    fun `getUser TimeoutCancellationException passes through repository`() = runBlockingTest {

        val userId = FORCE_GET_TIMEOUT_EXCEPTION

        val repositoryValue = userRepository.getUser(userId)

        Assertions.assertThat(repositoryValue.exception).isNotNull
        Assertions.assertThat(repositoryValue.exception)
            .isInstanceOf(TimeoutCancellationException::class.java)
    }

}