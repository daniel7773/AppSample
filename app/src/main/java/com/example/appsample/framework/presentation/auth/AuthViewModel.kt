package com.example.appsample.framework.presentation.auth

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.repository.abstraction.Resource
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.presentation.common.mappers.UserToUserModelMapper
import com.example.appsample.framework.presentation.common.model.AuthResource
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.common.model.UserModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class AuthViewModel @Inject constructor(
    private val useCase: GetUserUseCase,
    val sessionManager: SessionManager
) : ViewModel() {

    private val TAG = "AuthViewModel"

    private val _loadingState: MutableLiveData<State<UserModel>> = MutableLiveData(State.Unknown())
    val loadingState: LiveData<State<UserModel>> = _loadingState
    val isLoading = Transformations.map(_loadingState) { state ->
        when (state) {
            is State.Loading -> {
                return@map View.VISIBLE
            }
            else -> {
                return@map View.GONE
            }
        }
    }

    val userId: MutableLiveData<String> = MutableLiveData()

    fun startLoading() {
        var id = -1
        try {
            userId.value?.let { id = userId.value!!.toInt() }
        } catch (nfe: NumberFormatException) {
            Log.d(TAG, "tried to parse not valid Int value")
            Log.d(TAG, "error message ${nfe.localizedMessage}")
        }

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _loadingState.value = State.Error(
                throwable.message.toString(), Exception(throwable.localizedMessage)
            )

            viewModelScope.launch {
                sessionManager.setAuthState(
                    AuthResource.Error(
                        "ERROR IN RESPONSE",
                        java.lang.Exception(throwable.localizedMessage)
                    )
                )
            }
            Log.e(TAG, "Error in viewModel getUser coroutine chain: ", throwable)
        }

        viewModelScope.launch(exceptionHandler) {
            _loadingState.value = State.Loading("init loading")
            when (val response = useCase.getUser(id)) {
                is Resource.Success -> {
                    val user = UserToUserModelMapper.map(response.data!!)
                    _loadingState.value = State.Success(user, "")
                    setSuccessState(user)
                }
                is Resource.Error -> {
                    Log.d(TAG, "error ${response.exception.localizedMessage}")
                    _loadingState.value =
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
