package com.example.appsample.framework.presentation.auth.di.keys

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class AuthViewModelKey(val value: KClass<out ViewModel>)
