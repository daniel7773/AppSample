package com.example.appsample.framework.app.di

import com.example.appsample.framework.presentation.auth.di.AuthComponent
import com.example.appsample.framework.presentation.profile.di.ProfileComponent
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module(
    subcomponents = [
        AuthComponent::class,
        ProfileComponent::class
    ]
)
class SubComponentsModule