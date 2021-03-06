package com.example.appsample.framework.presentation.profile.di.factories.viewmodels.implementations

import androidx.lifecycle.SavedStateHandle
import com.example.appsample.business.interactors.profile.GetCommentListUseCase
import com.example.appsample.business.interactors.profile.GetPostUseCase
import com.example.appsample.framework.presentation.profile.di.factories.viewmodels.ViewModelAssistedFactory
import com.example.appsample.framework.presentation.profile.screens.post.PostViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
class PostViewModelFactory @ExperimentalCoroutinesApi
@Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val getCommentListUseCase: GetCommentListUseCase,
    private val getPostUseCase: GetPostUseCase
) : ViewModelAssistedFactory<PostViewModel> {
    override fun create(handle: SavedStateHandle): PostViewModel {
        return PostViewModel(
            mainDispatcher,
            ioDispatcher,
            getCommentListUseCase,
            getPostUseCase,
            handle
        )
    }
}