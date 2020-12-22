package com.example.appsample.framework.presentation.profile.di

import com.example.appsample.framework.presentation.profile.di.factories.fragments.ProfileNavHostFragment
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi


@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
@ProfileFragmentScope
@Subcomponent(
    modules = [
        ProfileModule::class,
        ProfileFragmentBuildersModule::class
    ]
)
interface ProfileComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(): ProfileComponent
    }

    fun inject(profileNavHostFragment: ProfileNavHostFragment)

}














