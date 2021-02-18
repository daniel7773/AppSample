package com.example.appsample.framework.presentation.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.business.domain.state.DataState.*
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.presentation.common.model.AuthResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class AuthViewModel @Inject constructor(
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
    private val getUserUseCase: GetUserUseCase,
    val sessionManager: SessionManager
) : ViewModel() {

    private val TAG = "AuthViewModel"

    private val _authState: MutableLiveData<DataState<User?>> by lazy {
        MutableLiveData(Idle())
    }
    val authState: LiveData<DataState<User?>> by lazy {
        _authState
    }

    val isLoading = Transformations.map(_authState) { state ->
        return@map state is Loading
    }

    val userId: MutableLiveData<String> = MutableLiveData()

    private val loadingExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Error message: ${throwable.localizedMessage}")
        throwable.printStackTrace()
        refreshUserState(
            state = Error(null, throwable.message.toString(), null)
        )

        viewModelScope.launch {
            setErrorState("UNHANDLED ERROR IN RESPONSE OF AUTHENTICATION", Exception(throwable.localizedMessage))
        }
    }

    fun startLoading() {
        var id = -1
        try {
            userId.value?.let { id = userId.value!!.toInt() }
        } catch (nfe: NumberFormatException) {
            Log.d(TAG, "tried to parse not valid Int value")
            Log.d(TAG, "error message ${nfe.localizedMessage}")
        }

        // TODO: handle how to get that user is not authenticated
        viewModelScope.launch(ioDispatcher + loadingExceptionHandler) {
            refreshUserState(Loading(null, "init loading"))
            getUserUseCase.getUser(id).collect { userData ->
                when (userData) {
                    is Idle -> {
                    }
                    is Loading -> {
                    }
                    is Success -> {
                        val user: User = userData.data!!
                        refreshUserState(Success(user, "SUCCESS"))
                        authenticateUser(user)
                    }
                    is Error -> {
                        if (userData.data != null) {
                            val user = userData.data!!
                            refreshUserState(Error(user, "Error while getting user", null))
                            authenticateUser(user)
                        } else {
                            refreshUserState(userData)
                            setErrorState(userData.message, userData.exception)
                        }
                    }
                }
            }
        }
    }

    private fun refreshUserState(state: DataState<User?>) = viewModelScope.launch(mainDispatcher) {
        _authState.value = state
    }

    private suspend fun authenticateUser(user: User) =
        sessionManager.setAuthState(AuthResource.Authenticated(user, "SUCCESS"))

    private suspend fun setErrorState(message: String, exception: Exception?) =
        sessionManager.setAuthState(AuthResource.Error(message, exception ?: java.lang.Exception("Exception is NULL")))
}
