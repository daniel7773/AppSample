package com.example.appsample.framework.presentation.profile.di.factories.viewmodels.implementations

import androidx.lifecycle.SavedStateHandle
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.business.interactors.profile.GetAlbumListUseCase
import com.example.appsample.business.interactors.profile.GetPostListUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.ViewModelAssistedFactory
import com.example.appsample.framework.presentation.profile.screens.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject
import javax.inject.Named

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class ProfileViewModelFactory @ExperimentalCoroutinesApi
@Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val getPostListUseCase: GetPostListUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAlbumListUseCase: GetAlbumListUseCase
) : ViewModelAssistedFactory<ProfileViewModel> {
    override fun create(handle: SavedStateHandle): ProfileViewModel {
        return ProfileViewModel(
            mainDispatcher,
            ioDispatcher,
            sessionManager,
            getPostListUseCase,
            getUserUseCase,
            getAlbumListUseCase,
            handle
        )
    }
}