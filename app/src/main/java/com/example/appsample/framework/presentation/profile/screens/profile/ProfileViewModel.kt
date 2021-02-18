package com.example.appsample.framework.presentation.profile.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.business.domain.state.DataState.Error
import com.example.appsample.business.domain.state.DataState.Idle
import com.example.appsample.business.domain.state.DataState.Loading
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.business.interactors.profile.GetAlbumListUseCase
import com.example.appsample.business.interactors.profile.GetPostListUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.presentation.profile.screens.profile.adapters.ProfileTransformator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
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

    var user: DataState<User?> = Idle()
    var postList: DataState<List<Post>?> = Idle()
    var albumList: DataState<List<Album>?> = Idle()

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
            sessionManager.user.id
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
        null,
        throwable.message.toString(),
        Exception("Error launching coroutine in ViewModel")
    )

    private suspend fun searchUser() {
        user = Loading(null, "init Loading")

        getUserUseCase.getUser(userId.value!!).collect { user ->
            this.user = user
            refreshData()
        }
    }

    private suspend fun searchPostList() {
        postList = Loading(null, "init Loading")

        getPostListUseCase.getPostList(userId.value!!).collect { postList ->
            this.postList = postList
            refreshData()
        }
    }

    private suspend fun searchAlbumList() {
        albumList = Loading(null, "init Loading")

        getAlbumListUseCase.getAlbumList(userId.value!!).collect { albumList ->
            this.albumList = albumList
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

