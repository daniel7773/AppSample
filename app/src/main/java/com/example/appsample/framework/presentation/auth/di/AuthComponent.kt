package com.example.appsample.framework.presentation.auth.di

import com.example.appsample.framework.presentation.auth.di.factories.fragments.AuthNavHostFragment
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi


@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AuthFragmentScope
@Subcomponent(
    modules = [
        AuthModule::class,
        AuthViewModelModule::class,
        AuthFragmentBuildersModule::class
    ]
)
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(): AuthComponent
    }

    fun inject(authNavHostFragment: AuthNavHostFragment)
}