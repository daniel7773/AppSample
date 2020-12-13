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
        AuthViewModel(mainCoroutineRule.testDispatcher, getUserUseCase, sessionManager)

    @Test
    fun `Loading Success data came`() = mainCoroutineRule.testDispatcher.runBlockingTest {

        // Given
        coEvery {
            getUserUseCase.getUser(any())
        } returns DataFactory.provideResourceSuccess(DataFactory.produceUser())

        // When
        authViewModel.startLoading()

        // Then
        assertThat(authViewModel.loadingState.value).isInstanceOf(State.Success::class.java)
    }

    @Test
    fun `Loading Error exception received correctly`() =
        mainCoroutineRule.testDispatcher.runBlockingTest {

            // Given
            coEvery {
                getUserUseCase.getUser(any())
            } returns DataFactory.provideResourceError(DataFactory.produceUser())

            // When
            authViewModel.startLoading()

            // Then
            assertThat(authViewModel.loadingState.value!!.exception).isNotNull
            assertThat(authViewModel.loadingState.value?.exception).isInstanceOf(
                getUserUseCase.getUser(
                    3
                ).exception!!::class.java
            )

        }
}