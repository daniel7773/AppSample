package com.example.appsample.framework.presentation.profile.di.factories.viewmodels.implementations

import androidx.lifecycle.SavedStateHandle
import com.example.appsample.business.interactors.profile.GetPhotoListUseCase
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.ViewModelAssistedFactory
import com.example.appsample.framework.presentation.profile.screens.photo.PhotoViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
class PhotoViewModelFactory @ExperimentalCoroutinesApi
@Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val getPhotoListUseCase: GetPhotoListUseCase,
) : ViewModelAssistedFactory<PhotoViewModel> {
    override fun create(handle: SavedStateHandle): PhotoViewModel {
        return PhotoViewModel(
            mainDispatcher,
            ioDispatcher,
            getPhotoListUseCase,
            handle
        )
    }
}