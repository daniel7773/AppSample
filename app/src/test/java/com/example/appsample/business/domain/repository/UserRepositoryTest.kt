package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.cache.abstraction.UserCacheDataSource
import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserEntityToUserMapper
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.repository.implementation.UserRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
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
    private val networkApi: JsonPlaceholderApiSource = mockk()
    private val userCacheDataSource: UserCacheDataSource = mockk()

    init {
        userRepository = UserRepositoryImpl(userCacheDataSource, networkApi)
        coEvery {
            userCacheDataSource.searchUserById(any())
        } returns null // TODO: rework and add to tests logic
        coEvery {
            userCacheDataSource.insertUser(any())
        } returns 1L // TODO: rework and add to tests logic
    }

    @Test
    fun `getUser Success`() = runBlockingTest {

        val userId = 1

        coEvery {
            networkApi.getUserAsync(userId).await()
        } returns DataFactory.produceUserEntity()

        val networkValue = networkApi.getUserAsync(userId).await()
        val repositoryValue = userRepository.getUser(userId)

        Assertions.assertThat(UserEntityToUserMapper.map(networkValue!!))
            .isEqualTo(repositoryValue.data)
    }

    @Test
    fun `getUser Error passes through repository`() = runBlockingTest {

        val userId = 1

        var exception: Exception = Exception("Any")

        coEvery {
            networkApi.getUserAsync(userId).await()
        } throws exception

        val repositoryValue = userRepository.getUser(userId)


        Assertions.assertThat(repositoryValue.exception).isInstanceOf(exception::class.java)
        Assertions.assertThat(exception.message).isEqualTo(repositoryValue.exception?.message)
        Assertions.assertThat(exception.localizedMessage)
            .isEqualTo(repositoryValue.exception?.localizedMessage)
    }

    @Test
    fun `getUser TimeoutCancellationException passes through repository`() = runBlockingTest {

        val userId = 1

        coEvery {
            networkApi.getUserAsync(userId)
        }.answers {
            val deferredUserEntity = CompletableDeferred<UserEntity?>()

            deferredUserEntity
        }

        val repositoryValue = userRepository.getUser(userId)

        Assertions.assertThat(repositoryValue.exception).isNotNull
        Assertions.assertThat(repositoryValue.exception)
            .isInstanceOf(TimeoutCancellationException::class.java)
    }

}