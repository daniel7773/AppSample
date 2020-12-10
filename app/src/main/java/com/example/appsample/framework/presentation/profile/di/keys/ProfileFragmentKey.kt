package com.example.appsample.framework.presentation.profile.di.keys

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ProfileFragmentKey(val value: KClass<out Fragment>)
