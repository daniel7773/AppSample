package com.example.appsample.framework.presentation.auth.di.keys

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class AuthFragmentKey(val value: KClass<out Fragment>)
