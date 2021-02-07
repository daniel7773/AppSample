package com.example.appsample.framework.presentation.profile.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.business.interactors.profile.GetAlbumListUseCase
import com.example.appsample.business.interactors.profile.GetPostListUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.presentation.common.mappers.UserToUserModelMapper
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.common.model.State.Error
import com.example.appsample.framework.presentation.common.model.State.Loading
import com.example.appsample.framework.presentation.common.model.State.Success
import com.example.appsample.framework.presentation.common.model.State.Unknown
import com.example.appsample.framework.presentation.common.model.UserModel
import com.example.appsample.framework.presentation.profile.mappers.AlbumToAlbumModelMapper
import com.example.appsample.framework.presentation.profile.mappers.PostToPostModelMapper
import com.example.appsample.framework.presentation.profile.model.AlbumModel
import com.example.appsample.framework.presentation.profile.model.PostModel
import com.example.appsample.framework.presentation.profile.screens.main.adapters.ProfileTransformator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Named

private const val USER_ID_KEY = "userId"

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class ProfileViewModel constructor( // I suppose it is better to use database instead of SavedStateHandle for complex data
    private val mainDispatcher: CoroutineDispatcher,
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val getPostListUseCase: GetPostListUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAlbumListUseCase: GetAlbumListUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var user: State<UserModel?> = Unknown()
    var postList: State<List<PostModel>?> = Unknown()
    var albumList: State<List<AlbumModel>?> = Unknown()

    private val _adapterItems: MutableLiveData<Sequence<AdapterElement>> by lazy {
        MutableLiveData(emptySequence())
    }
    val items: LiveData<Sequence<AdapterElement>> by lazy { _adapterItems }

    private val _isLoading by lazy { MutableLiveData(true) }
    val isLoading: LiveData<Boolean> by lazy { _isLoading }

    private val _userId: MutableLiveData<Int> = savedStateHandle.getLiveData(USER_ID_KEY)

    val userId: LiveData<Int> = _userId

    init {
        savedStateHandle.set(
            USER_ID_KEY,
            sessionManager.user.id ?: 0
        )
        startSearch()
    }

    fun startSearch() {
        viewModelScope.launch(ioDispatcher) { launchSearch() }
    }

    suspend fun launchSearch() {
        supervisorScope {
            launch(CoroutineExceptionHandler { _, throwable -> user = getError(throwable) }) {
                searchUser()
            }

            launch(CoroutineExceptionHandler { _, throwable -> postList = getError(throwable) }) {
                searchPostList()
            }

            launch(CoroutineExceptionHandler { _, throwable -> albumList = getError(throwable) }) {
                searchAlbumList()
            }
        }
    }

    private fun <T> getError(throwable: Throwable) = Error<T>(
        throwable.message.toString(),
        Exception("Error launching coroutine in ViewModel")
    )

    private suspend fun searchUser() {
        user = Loading("init Loading")

        getUserUseCase.getUser(userId.value!!).collect { user ->
            if (user == null) return@collect
            this.user = Success(UserToUserModelMapper.map(user), "SUCCESS")
            refreshData()
        }
    }

    private suspend fun searchPostList() {
        postList = Loading("init Loading")

        getPostListUseCase.getPostList(userId.value!!).collect { postList ->
            if (postList == null) return@collect
            this.postList = Success(PostToPostModelMapper.mapList(postList), "SUCCESS")
            refreshData()
        }
    }

    private suspend fun searchAlbumList() {
        albumList = Loading("init Loading")

        getAlbumListUseCase.getAlbumList(userId.value!!).collect { albumList ->
            if (albumList == null) return@collect
            this.albumList = Success(AlbumToAlbumModelMapper.map(albumList), "SUCCESS")
            refreshData()
        }
    }


    private fun refreshData() = viewModelScope.launch(mainDispatcher) {
        _adapterItems.value = ProfileTransformator.transform(user, albumList, postList)
        _isLoading.value = user is Loading || albumList is Loading || postList is Loading
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}

