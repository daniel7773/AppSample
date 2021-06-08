package com.example.appsample.business.domain.state

sealed class DataState<T>(
    open val data: T?,
    open val message: String?,
    open val exception: Exception? = null
) {
    data class Success<T>(
        override val data: T,
        override val message: String? = null
    ) : DataState<T>(data, message)

    data class Loading<T>(
        override val data: T?,
        override val message: String
    ) : DataState<T>(null, message)

    data class Error<T>(
        override val data: T?,
        override val message: String,
        override val exception: Exception?
    ) : DataState<T>(null, message, exception)

    class Idle<T> : DataState<T>(null, "INIT STATE")
}