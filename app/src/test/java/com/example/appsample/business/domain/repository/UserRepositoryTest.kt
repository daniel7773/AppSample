package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.cache.abstraction.UserCacheDataSource
import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserEntityToUserMapper
import com.example.appsample.business.domain.repository.abstraction.PostsRepository
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.repository.implementation.UserRepositoryImpl
import com.example.appsample.rules.InstantExecutorExtension
import com.example.appsample.rules.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class UserRepositoryTest {

    // system in test
    private val userRepository: UserRepository

    // Resource needed
    @get:Extensions
    val mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()

    private val networkApi: JsonPlaceholderApiSource = mockk()
    private val userCacheDataSource: UserCacheDataSource = mockk()

    init {
        userRepository = UserRepositoryImpl(
            mainDispatcher = mainCoroutineRule.testDispatcher,
            userCacheDataSource = userCacheDataSource,
            jsonPlaceholderApiSource = networkApi
        )


        coEvery {
            userCacheDataSource.insertUser(any())
        } returns 1L // TODO: rework and add to tests logic
    }

    @Test
    fun `getUser Success`() = runBlockingTest {

        val userId = 1

        coEvery {
            userCacheDataSource.searchUserById(any())
        } returns DataFactory.produceUserEntity()

        coEvery {
            networkApi.getUserAsync(userId).await()
        } returns DataFactory.produceUserEntity()

        val networkValue = networkApi.getUserAsync(userId).await()
        val repositoryValue = userRepository.getUser(userId).first()

        Assertions.assertThat(UserEntityToUserMapper.map(networkValue!!))
            .isEqualTo((repositoryValue as Resource.Success).data)
    }

    @Test
    fun `getUser Error passes through repository`() = runBlockingTest {

        val userId = 1

        val exception: Exception = Exception("Any")

        coEvery {
            networkApi.getUserAsync(userId).await()
        } throws exception

        coEvery {
            userCacheDataSource.searchUserById(any())
        } throws exception

        val repositoryValue = userRepository.getUser(userId).first()

        Assertions.assertThat((repositoryValue as Resource.Error).exception).isInstanceOf(exception::class.java)
        Assertions.assertThat(exception.message).isEqualTo(repositoryValue.exception.message)
        Assertions.assertThat(exception.localizedMessage)
            .isEqualTo(repositoryValue.exception.localizedMessage)
    }

}