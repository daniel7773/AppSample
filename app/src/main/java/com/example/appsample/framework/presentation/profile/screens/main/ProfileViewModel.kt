package com.example.appsample.framework.presentation.profile.screens.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.business.interactors.profile.GetAlbumListUseCase
import com.example.appsample.business.interactors.profile.GetPostListUseCase
import com.example.appsample.framework.base.presentation.SessionManager
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
import com.example.appsample.framework.presentation.profile.model.ProfileElement
import com.example.appsample.framework.presentation.profile.screens.main.adapters.ProfileTransformator
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

    var user: State<UserModel?> = Unknown()
    var postList: State<List<PostModel>?> = Unknown()
    var albumList: State<List<AlbumModel>?> = Unknown()

    private val _adapterItems: MutableLiveData<Sequence<ProfileElement>> by lazy {
        MutableLiveData(emptySequence())
    }
    val items: LiveData<Sequence<ProfileElement>> by lazy { _adapterItems }

    private val _isLoading by lazy { MutableLiveData(true) }
    val isLoading: LiveData<Boolean> by lazy { _isLoading }

    private val _userId: MutableLiveData<Int> = savedStateHandle.getLiveData(USER_ID_KEY)

    val userId: LiveData<Int> = _userId

    init {
        Log.d("ProfileViewModel", "init called")
        savedStateHandle.set(
            USER_ID_KEY,
            sessionManager.user.id!!
        )
        startSearch()
    }

    fun startSearch() {
        Log.d("ProfileViewModel", "startSearch called")
        viewModelScope.launch(mainDispatcher) {
            supervisorScope {
                launch(CoroutineExceptionHandler { _, throwable ->
                    user = getError(throwable)
                }) {
                    searchUser()
                }

                launch(CoroutineExceptionHandler { _, throwable ->
                    postList = getError(throwable)
                }) { searchPostList() }

                launch(CoroutineExceptionHandler { _, throwable ->
                    albumList = getError(throwable)
                }) { searchAlbumList() }
            }
        }
    }

    private fun <T> getError(throwable: Throwable) = Error<T>(
        throwable.message.toString(),
        Exception("Error launching coroutine in ViewModel")
    )

    private suspend fun searchUser() {
        Log.d("DISPATCHER_DEBUG", "searchUser called : ${Thread.currentThread()}")
        user = Loading("init Loading")
        if (userId.value == null) {
            throw java.lang.Exception("User id cant be NULL")
        }
        getUserUseCase.getUser(userId.value!!).collect { response ->
            user = when (response) {
                is Resource.Success -> {
                    Log.d(TAG, "user ${response.data}")
                    // force unwrap because null values must be handled earlier
                    val userLocal = UserToUserModelMapper.map(response.data!!)
                    Success(userLocal, response.message ?: "getUser Success in ViewModel")
                }
                is Resource.Error -> {
                    Log.e(TAG, "getUser error ${response.exception.localizedMessage}")
                    Error(response.message.toString(), response.exception)
                }
                else -> {
                    Loading("init Loading")
                }
            }
            refreshData()
        }
    }

    private suspend fun searchPostList() {
        Log.d("ProfileViewModel", "searchPostList called")
        Log.d("DISPATCHER_DEBUG", "searchPostList called : ${Thread.currentThread()}")
        postList = Loading("init Loading")
        if (userId.value == null) {
            throw java.lang.Exception("User id cant be NULL")
        }

        getPostListUseCase.getPostList(userId.value!!).collect { response ->

            when (response) {
                is Resource.Success -> {
                    Log.d(TAG, "postList came, size: ${response.data!!.size}")
                    // force unwrap because null values must be handled earlier
                    val postModelList = PostToPostModelMapper.mapList(response.data)

                    this.postList = Success(postModelList, "Successfully got data in VM")
                }
                is Resource.Error -> {
                    Log.e(TAG, "getPostList error ${response.exception.localizedMessage}")
                    postList = Error(response.message.toString(), response.exception)
                }
                else -> {
                    postList = Loading("init Loading")
                }
            }
        }

        refreshData()
    }

    private suspend fun searchAlbumList() {
        albumList = Loading("init Loading")
        if (userId.value == null) {
            throw java.lang.Exception("User id cant be NULL")
        }
        Log.d("DISPATCHER_DEBUG", "searchAlbumList called : ${Thread.currentThread()}")
        getAlbumListUseCase.getAlbumList(userId.value!!).collect { response ->
            when (response) {
                is Resource.Success -> {
                    Log.d(TAG, "albumList size ${response.data?.size}")
                    // force unwrap because null values must be handled earlier
                    val albums = AlbumToAlbumModelMapper.map(response.data!!)

                    albumList = Success(albums, "Successfully got data in VM")
                }
                is Resource.Error -> {
                    Log.e(TAG, "getAlbumList error ${response.exception.localizedMessage}")
                    albumList = Error(response.message.toString(), response.exception)
                }
                else -> {
                    albumList = Loading("init Loading")
                }
            }
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

