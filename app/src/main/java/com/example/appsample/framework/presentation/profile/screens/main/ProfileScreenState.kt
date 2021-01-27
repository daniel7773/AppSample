package com.example.appsample.framework.presentation.profile.screens.main

import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement

sealed class ProfileScreenState {
    object Idle : ProfileScreenState()
    object Loading : ProfileScreenState()
    data class Success(val value: List<AdapterElement>) : ProfileScreenState()
    data class Failed(val reason: Throwable) : ProfileScreenState()
}
