package com.example.appsample.framework.app.di

import com.example.appsample.framework.presentation.auth.di.AuthComponent
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module(
    subcomponents = [
        AuthComponent::class
    ]
)
class SubComponentsModule