package com.example.appsample.business.interactors.common

import com.example.appsample.business.data.network.FakeJsonPlaceHolderApiSource
import com.example.appsample.business.data.network.abstraction.JsonPlaceholderApiSource
import com.example.appsample.business.domain.mappers.UserEntityToUserMapper
import com.example.appsample.business.domain.repository.abstraction.UserRepository
import com.example.appsample.business.domain.repository.implementation.UserRepositoryImpl
import junit.framework.Assert
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

@InternalCoroutinesApi
class GetUserUseCaseTest {

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

}