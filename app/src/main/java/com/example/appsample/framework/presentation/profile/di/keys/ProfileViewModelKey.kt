package com.example.appsample.framework.presentation.profile.di.keys

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ProfileViewModelKey(val value: KClass<out ViewModel>)
