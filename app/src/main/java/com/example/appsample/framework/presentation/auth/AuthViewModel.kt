package com.example.appsample.framework.presentation.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        Log.d(TAG, "error message: ${throwable.localizedMessage}")
        throwable.printStackTrace()
        refreshUserState(
            State.Error(
                throwable.message.toString(), Exception(throwable.localizedMessage)
            )
        )

        viewModelScope.launch {
            setErrorState(
                "ERROR IN RESPONSE OF AUTHENTICATION",
                Exception(throwable.localizedMessage)
            )
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

        Log.d(TAG, "about to start search")
        // TODO: handle how to get that user is not authenticated
        viewModelScope.launch(ioDispatcher + loadingExceptionHandler) {
            refreshUserState(State.Loading("init loading"))
            getUserUseCase.getUser(id).collect { user ->
                Log.d(TAG, "getUser(id).collect: ${user}")
                if (user == null) {
                    refreshUserState(State.Error("Error while getting user", Exception()))
                    setErrorState("Error while getting user", Exception())
                    return@collect
                }
                val userModel = UserToUserModelMapper.map(user)
                refreshUserState(State.Success(userModel, "SUCCESS"))
                setSuccessState(userModel)
            }
        }
    }

    private fun refreshUserState(state: State<UserModel>) = viewModelScope.launch(mainDispatcher) {
        _authState.value = state
    }

    private suspend fun setSuccessState(userModel: UserModel) =
        sessionManager.setAuthState(AuthResource.Authenticated(userModel, "SUCCESS"))

    private suspend fun setErrorState(message: String, exception: Exception) =
        sessionManager.setAuthState(AuthResource.Error(message, exception))
}
