package com.example.appsample.framework.presentation.profile.di.factories.viewmodels.implementations

import androidx.lifecycle.SavedStateHandle
import com.example.appsample.business.interactors.profile.GetPhotoListUseCase
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.ViewModelAssistedFactory
import com.example.appsample.framework.presentation.profile.screens.album.AlbumViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AlbumViewModelFactory @ExperimentalCoroutinesApi
@Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val getPhotoListUseCase: GetPhotoListUseCase,
) : ViewModelAssistedFactory<AlbumViewModel> {
    override fun create(handle: SavedStateHandle): AlbumViewModel {
        return AlbumViewModel(
            mainDispatcher,
            getPhotoListUseCase,
            handle
        )
    }
}