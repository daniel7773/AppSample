package com.example.appsample.framework.presentation.auth.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.appsample.framework.presentation.auth.AuthFragment
import com.example.appsample.framework.presentation.auth.di.factories.fragments.AuthFragmentFactory
import com.example.appsample.framework.presentation.auth.di.keys.AuthFragmentKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
abstract class AuthFragmentBuildersModule {

    @Binds
    abstract fun bindFragmentFactory(authFragmentFactory: AuthFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @AuthFragmentKey(AuthFragment::class)
    abstract fun bindAuthFragment(authFragment: AuthFragment): Fragment
}