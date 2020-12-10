package com.example.appsample.framework.presentation.profile.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.appsample.framework.presentation.profile.ProfileFragment
import com.example.appsample.framework.presentation.profile.di.factories.fragments.ProfileFragmentFactory
import com.example.appsample.framework.presentation.profile.di.keys.ProfileFragmentKey
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
abstract class ProfileFragmentBuildersModule {

    @Binds
    abstract fun bindFragmentFactory(profileFragmentFactory: ProfileFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @ProfileFragmentKey(ProfileFragment::class)
    abstract fun bindMainFragment(feature1MainFragment: ProfileFragment): Fragment

//    @Binds
//    @IntoMap
//    @ProfileFragmentKey(Feature1NextFragment::class)
//    abstract fun bindNextFragment(feature1NextFragment: Feature1NextFragment): Fragment
//
//    @Binds
//    @IntoMap
//    @ProfileFragmentKey(Feature1FinalFragment::class)
//    abstract fun bindFinalFragment(feature1FinalFragment: Feature1FinalFragment): Fragment
}