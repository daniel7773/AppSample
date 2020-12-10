package com.example.appsample.framework.base.presentation

import android.app.Application
import com.example.appsample.framework.app.di.AppComponent
import com.example.appsample.framework.app.di.DaggerAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
class BaseApplication : Application() {

    private val TAG: String = "AppDebug"

    private val appComponent = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }


    fun getAppComponent(): AppComponent {
        return appComponent
    }
}