package com.example.appsample.framework.presentation.common.model

import com.example.appsample.business.domain.model.User

sealed class AuthResource(
    open val user: User?,
    open val message: String?,
    val isLoading: Boolean
) {
    data class Authenticated(
        override val user: User,
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