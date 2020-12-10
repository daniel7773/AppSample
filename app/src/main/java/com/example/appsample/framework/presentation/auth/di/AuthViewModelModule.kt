package com.example.appsample.framework.presentation.auth.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appsample.framework.presentation.auth.AuthViewModel
import com.example.appsample.framework.presentation.auth.di.factories.viewmodels.AuthViewModelFactory
import com.example.appsample.framework.presentation.auth.di.keys.AuthViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
abstract class AuthViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: AuthViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @AuthViewModelKey(AuthViewModel::class)
    abstract fun bindMainViewModel(viewModel: AuthViewModel): ViewModel

}