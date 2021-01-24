package com.example.appsample.business.domain.repository

sealed class Resource<out T> {

    data class Success<out T>(val data: T, val message: String?) : Resource<T>()

    data class Error(val message: String?, val exception: Exception) : Resource<Nothing>()

    data class Loading<out T>(val data: T, val message: String?) : Resource<T>()
}