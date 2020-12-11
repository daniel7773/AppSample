package com.example.appsample.framework.base.presentation

import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseActivity : FragmentActivity() {

    private val TAG = "BaseActivity"

    abstract fun onAuthenticated()
    abstract fun onLogOut()
    abstract fun onError()

    companion object {
        private const val TAG = "BaseActivity"
    }
}