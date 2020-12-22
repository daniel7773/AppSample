package com.example.appsample.framework.presentation.profile.di.factories.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

// https://proandroiddev.com/saving-ui-state-with-viewmodel-savedstate-and-dagger-f77bcaeb8b08

interface ViewModelAssistedFactory<T : ViewModel> {
    fun create(handle: SavedStateHandle): T
}