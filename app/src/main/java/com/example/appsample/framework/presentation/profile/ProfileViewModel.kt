package com.example.appsample.framework.presentation.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.repository.abstraction.Resource
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.business.interactors.profile.GetAlbumsUseCase
import com.example.appsample.business.interactors.profile.GetPostsUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.presentation.common.mappers.UserToUserModelMapper
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.common.model.State.Error
import com.example.appsample.framework.presentation.common.model.State.Success
import com.example.appsample.framework.presentation.common.model.UserModel
import com.example.appsample.framework.presentation.profile.adapters.ProfileTransformator
import com.example.appsample.framework.presentation.profile.mappers.AlbumToAlbumModelMapper
import com.example.appsample.framework.presentation.profile.mappers.PostToPostModelMapper
import com.example.appsample.framework.presentation.profile.models.AlbumModel
import com.example.appsample.framework.presentation.profile.models.PostModel
import com.example.appsample.framework.presentation.profile.models.ProfileElement
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ProfileViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val getPostsUseCase: GetPostsUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    var posts: State<List<PostModel>?> = State.Unknown()
    var user: State<UserModel?> = State.Unknown()
    var albums: State<List<AlbumModel>?> = State.Unknown()

    private var _adapterItems: MutableLiveData<Sequence<ProfileElement>> =
        MutableLiveData(emptySequence())
    var items: LiveData<Sequence<ProfileElement>> = _adapterItems

    init { // TODO: add checking if user logged in

    }

    fun startSearch() {
        viewModelScope.launch(mainDispatcher) { //  + exceptionHandler
            supervisorScope {

                launch(CoroutineExceptionHandler { _, throwable ->
                    user =
                        Error(throwable.message.toString(), Exception("Error launching coroutine"))
                }) {
                    getUser()
                }

                launch(CoroutineExceptionHandler { _, throwable ->
                    posts =
                        Error(throwable.message.toString(), Exception("Error launching coroutine"))
                }) {
                    getPosts()
                }

                launch(CoroutineExceptionHandler { _, throwable ->
                    albums =
                        Error(throwable.message.toString(), Exception("Error launching coroutine"))
                }) {
                    getAlbums()
                }
            }
        }
    }

    private suspend fun getUser() {
        user = State.Loading("init Loading")
        user = when (val response = getUserUseCase.getUser(sessionManager.user.id)) {
            is Resource.Success -> {
                Log.d(TAG, "posts ${response.data}")
                val user =
                    UserToUserModelMapper.map(response.data!!) // force unwrap because null values must be handled earlier
                Success(user, response.message ?: "")
            }
            is Resource.Error -> {
                Log.d(TAG, "error ${response.exception.localizedMessage}")
                Error(response.message.toString(), response.exception)
            }
        }
        refreshData()
    }

    private suspend fun getPosts() {
        posts = State.Loading("init Loading")
        posts = when (val response = getPostsUseCase.getPosts(sessionManager.user.id)) {
            is Resource.Success -> {
                Log.d(
                    TAG,
                    "posts came, size: ${response.data!!.size}"
                ) // force unwrap because null values must be handled earlier
                val postList = PostToPostModelMapper.map(response.data!!)
                Success(postList, "")
            }
            is Resource.Error -> {
                Log.d(TAG, "error ${response.exception.localizedMessage}")
                Error(response.message.toString(), response.exception)
            }
        }
        refreshData()
    }

    private suspend fun getAlbums() {
        albums = State.Loading("init Loading")
        albums = when (val response = getAlbumsUseCase.getAlbums(sessionManager.user.id)) {
            is Resource.Success -> {
                Log.d(TAG, "posts ${response.data}")
                val albums =
                    AlbumToAlbumModelMapper.map(response.data!!)  // force unwrap because null values must be handled earlier
                Success(albums, "")
            }
            is Resource.Error -> {
                Log.d(TAG, "error ${response.exception.localizedMessage}")
                Error(response.message.toString(), response.exception)
            }
        }
        refreshData()
    }

    fun refreshData() = viewModelScope.launch(mainDispatcher) {
        _adapterItems.value = ProfileTransformator.transform(user, albums, posts)
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}

