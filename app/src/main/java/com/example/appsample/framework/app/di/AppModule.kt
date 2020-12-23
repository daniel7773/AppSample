package com.example.appsample.framework.app.di

import androidx.room.Room
import com.example.appsample.framework.base.presentation.BaseApplication
import com.example.appsample.framework.datasource.cache.database.UserDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Named
import javax.inject.Singleton

@FlowPreview
@InternalCoroutinesApi
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

    @Singleton
    @Provides
    fun provideMainDispatcher() = Dispatchers.Main as CoroutineDispatcher

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserDb(app: BaseApplication): UserDatabase {
        return Room
            .databaseBuilder(app, UserDatabase::class.java, UserDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}

