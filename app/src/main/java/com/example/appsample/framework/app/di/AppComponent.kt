package com.example.appsample.framework.app.di

import com.example.appsample.framework.base.presentation.BaseApplication
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.presentation.auth.di.AuthComponent
import com.example.appsample.framework.presentation.profile.di.ProfileComponent
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton


@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
@Component(
    modules = [
        AppModule::class,
        SubComponentsModule::class
    ]
)
interface AppComponent {

    fun sessionManager(): SessionManager

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance app: BaseApplication): AppComponent
    }

    fun inject(application: BaseApplication)

    fun profileComponent(): ProfileComponent.Factory

    fun authComponent(): AuthComponent.Factory
}









