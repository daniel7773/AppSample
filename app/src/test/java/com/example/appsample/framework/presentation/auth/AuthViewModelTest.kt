package com.example.appsample.framework.presentation.auth

import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.rules.InstantExecutorExtension
import com.example.appsample.rules.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class AuthViewModelTest {

    @get:Extensions
    var mainCoroutineRule = MainCoroutineRule()

    val getUserUseCase: GetUserUseCase = mockk()
    val sessionManager: SessionManager = SessionManager(mainCoroutineRule.testDispatcher)
    val authViewModel: AuthViewModel =
        AuthViewModel(mainCoroutineRule.testDispatcher, mainCoroutineRule.testDispatcher, getUserUseCase, sessionManager)

    @Test
    fun `Loading Success data came`() = runBlockingTest {

        // Given
        coEvery {
            getUserUseCase.getUser(any())
        } returns flowOf(DataFactory.produceUser())
        authViewModel.userId.value = "2"

        // When
        authViewModel.startLoading()
        advanceTimeBy(5000)
        val user = (authViewModel.authState.value as State.Success)

        // Then
        assertThat(user).isInstanceOf(State.Success::class.java)
    }

    @Test
    fun `Loading Error exception received correctly`() = runBlockingTest {

        // Given
        coEvery {
            getUserUseCase.getUser(any())
        } throws Exception()
        authViewModel.userId.value = "2"

        // When
        authViewModel.startLoading()

        val user = (authViewModel.authState.value as State.Error)
        // Then
        assertThat(authViewModel.authState.value!!.exception).isNotNull
        assertThat(authViewModel.authState.value?.exception).isInstanceOf(user.exception::class.java)
    }
}