package com.example.appsample.framework.presentation.profile.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appsample.framework.presentation.profile.ProfileViewModel
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.ProfileViewModelFactory
import com.example.appsample.framework.presentation.profile.di.keys.ProfileViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
abstract class ProfileViewModelsModule {

    @Binds
    abstract fun bindFeature1VMFactory(factory: ProfileViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ProfileViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

}