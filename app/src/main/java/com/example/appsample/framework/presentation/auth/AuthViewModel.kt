package com.example.appsample.framework.presentation.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.abstraction.Resource
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.presentation.common.mappers.UserToUserModelMapper
import com.example.appsample.framework.presentation.common.model.AuthResource
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.common.model.UserModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class AuthViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val getUserUseCase: GetUserUseCase,
    val sessionManager: SessionManager
) : ViewModel() {

    private val TAG = "AuthViewModel"

    private val _authState: MutableLiveData<State<UserModel>> by lazy {
        MutableLiveData(State.Unknown())
    }
    val authState: LiveData<State<UserModel>> by lazy {
        _authState
    }

    val isLoading = Transformations.map(_authState) { state ->
        return@map state is State.Loading
    }

    val userId: MutableLiveData<String> = MutableLiveData()

    private val loadingExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _authState.value = State.Error(
            throwable.message.toString(), Exception(throwable.localizedMessage)
        )

        viewModelScope.launch(mainDispatcher) {
            sessionManager.setAuthState(
                AuthResource.Error(
                    "ERROR IN RESPONSE OF AUTHENTICATION",
                    java.lang.Exception(throwable.localizedMessage)
                )
            )
        }
        Log.e(TAG, "Error in viewModel getUser coroutine chain: ", throwable)
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
        viewModelScope.launch(mainDispatcher + loadingExceptionHandler) {
            _authState.value = State.Loading("init loading")
            when (val response = getUserUseCase.getUser(id)) {
                is Resource.Success -> {
                    // force unwrap because null values must be handled earlier
                    val user = UserToUserModelMapper.map(response.data!!)
                    _authState.value = State.Success(user, "")
                    setSuccessState(user)
                }
                is Resource.Error -> {
                    Log.d(TAG, "error ${response.exception.localizedMessage}")
                    _authState.value =
                        State.Error(response.message.toString(), response.exception)
                    setErrorState(response)
                }
            }
        }
    }

    private suspend fun setSuccessState(userModel: UserModel) =
        sessionManager.setAuthState(
            AuthResource.Authenticated(
                userModel,
                "success"
            )
        )

    private suspend fun setErrorState(response: Resource.Error<User?>) =
        sessionManager.setAuthState(
            AuthResource.Error(
                response.message,
                response.exception
            )
        )
}
