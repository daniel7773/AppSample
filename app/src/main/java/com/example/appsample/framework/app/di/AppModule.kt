package com.example.appsample.framework.app.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Named
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
object AppModule {

    @Singleton
    @Provides
    @Named("application_name")
    @JvmStatic
    fun provideApplicationName(): String {
        return "Sample App"
    }
}

