package com.example.appsample.framework.presentation.common.model

sealed class AuthResource(
    open val user: UserModel?,
    open val message: String?,
    val isLoading: Boolean
) {
    data class Authenticated(
        override val user: UserModel,
        override val message: String
    ) : AuthResource(user, message, false)

    data class Loading(
        override val message: String
    ) : AuthResource(null, message, true)

    data class Error(
        override val message: String?,
        val exception: Exception
    ) : AuthResource(null, message, false)

    class NotAuthenticated : AuthResource(null, "NotAuthenticated", false)
}