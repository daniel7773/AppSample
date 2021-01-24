package com.example.appsample.framework.presentation.common.model

sealed class State<T>(
    open val data: T?,
    open val message: String?,
    open val exception: Exception? = null
) {
    data class Success<T>(
        override val data: T,
        override val message: String
    ) : State<T>(data, message)

    data class Loading<T>(
        override val message: String
    ) : State<T>(null, message)

    // message keeps info about the error happened above, Exceptions is about current class
    data class Error<T>(
        override val message: String,
        override val exception: Exception
    ) : State<T>(null, message, exception)

    class Unknown<T> : State<T>(null, "UNKNOWN STATE")
}