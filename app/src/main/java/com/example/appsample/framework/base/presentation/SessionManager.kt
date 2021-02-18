package com.example.appsample.framework.base.presentation

import android.util.Log
import com.example.appsample.business.domain.model.User
import com.example.appsample.framework.presentation.common.model.AuthResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
@Singleton
class SessionManager @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher
) : CoroutineScope {

    private val job = Job() // a lifecycle of a SessionManager coroutine

    override val coroutineContext: CoroutineContext // guaranties that when it's lifecycle ends everything gonna be cancelled
        get() = job + mainDispatcher

    private var _stateFlow: MutableSharedFlow<AuthResource> = MutableSharedFlow()
    val stateFlow = _stateFlow.asSharedFlow() // publicly exposed as read-only shared flow

    var user: User = User()

    init {
        launch(coroutineContext) {
            _stateFlow.collectLatest { authResource ->
                when (authResource) {
                    is AuthResource.Authenticated -> {
                        user = authResource.user
                        Log.d(TAG, "user authentication ------ Authenticated ------ ")
                    }
                    is AuthResource.Loading -> {
                        Log.d(TAG, "user authentication ---------- Loading ---------- ")
                    }
                    is AuthResource.Error -> {
                        Log.d(TAG, "user authentication ---------- Error ---------- ")
                    }
                    is AuthResource.NotAuthenticated -> {
                        user = User()
                        Log.d(TAG, "user authentication ----- NotAuthenticated ----- ")
                        Log.d(TAG, "Moving to Auth Fragment ")
                    }
                }
            }
        }
    }

    suspend fun setAuthState(authResource: AuthResource) {
        _stateFlow.emit(authResource) // suspends until all subscribers receive it
    }

    fun logOut() {
        Log.d(TAG, "logOut: logging out...")
    }

    // since a class has lifecycle it can be closed or disposed, doesnt matter how  it called, it can be ended, in coroutine it called canceled
    fun close() {
        job.cancel()
    }

    companion object {
        private const val TAG = "SessionManager"
    }
}