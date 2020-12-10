package com.example.appsample.business.domain.repository.abstraction

sealed class Resource<T>(
    open val data: T?,
    open val message: String?,
    open val exception: Exception?
) {
    data class Success<T>(
        override val data: T,
        override val message: String?
    ) : Resource<T>(data, message, null)

    data class Error<T>(
        override val data: T? = null,
        override val message: String?,
        override val exception: Exception
    ) : Resource<T>(data, message, exception)
}