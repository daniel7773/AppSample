package com.example.appsample.business.domain.repository

import com.example.appsample.business.data.cache.abstraction.UserCacheSource
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserDataToUserMapper
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.repository.implementation.UserRepositoryImpl
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.rules.InstantExecutorExtension
import com.example.appsample.rules.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
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
    private val userCacheSource: UserCacheSource = mockk()


    val userId = 1L

    init {
        userRepository = UserRepositoryImpl(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            userCacheSource = userCacheSource,
            jsonPlaceholderApiSource = networkApi
        )

        coEvery {
            userCacheSource.insertUser(any())
        } returns userId
    }

    @Test
    fun `getUser Success`() = runBlockingTest {

        val userId = 1

        coEvery {
            userCacheSource.searchUserById(any())
        } returns DataFactory.produceUser()

        coEvery {
            networkApi.getUserAsync(userId).await()
        } returns DataFactory.produceUserEntity()

        val networkValue = networkApi.getUserAsync(userId).await()
        val repositoryValue = userRepository.getUser(userId).first()

        Assertions.assertThat(UserDataToUserMapper.map(networkValue!!))
            .isEqualTo(repositoryValue.data)
    }

    @Test
    fun `getUser Error passes through repository`() = runBlockingTest {


        val exception: Exception = Exception("Any")

        coEvery {
            networkApi.getUserAsync(userId.toInt()).await()
        } throws exception

        coEvery {
            userCacheSource.searchUserById(any())
        } throws exception

        val repositoryValue = userRepository.getUser(userId.toInt()).first()

        Assertions.assertThat(repositoryValue).isInstanceOf(DataState.Error::class.java)
    }

}