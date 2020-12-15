package com.example.appsample.framework.presentation.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.repository.abstraction.Resource
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.business.interactors.profile.GetAlbumListUseCase
import com.example.appsample.business.interactors.profile.GetPhotoUseCase
import com.example.appsample.business.interactors.profile.GetPostListUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.presentation.common.mappers.AlbumToAlbumModelMapper
import com.example.appsample.framework.presentation.common.mappers.PhotoToPhotoModelMapper
import com.example.appsample.framework.presentation.common.mappers.UserToUserModelMapper
import com.example.appsample.framework.presentation.common.model.AlbumModel
import com.example.appsample.framework.presentation.common.model.PhotoModel
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.common.model.State.Error
import com.example.appsample.framework.presentation.common.model.State.Success
import com.example.appsample.framework.presentation.common.model.UserModel
import com.example.appsample.framework.presentation.profile.adapters.ProfileTransformator
import com.example.appsample.framework.presentation.profile.mappers.PostToPostModelMapper
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
    private val getPostListUseCase: GetPostListUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAlbumListUseCase: GetAlbumListUseCase,
    private val getPhotoUseCase: GetPhotoUseCase
) : ViewModel() {

    var postList: State<List<PostModel>?> = State.Unknown()
    var user: State<UserModel?> = State.Unknown()
    var albumList: State<List<AlbumModel>?> = State.Unknown()

    private var _adapterItems: MutableLiveData<Sequence<ProfileElement>> =
        MutableLiveData(emptySequence())
    var items: LiveData<Sequence<ProfileElement>> = _adapterItems

    init { // TODO: add checking if user logged in

    }

    fun startSearch() {
        viewModelScope.launch(mainDispatcher) {
            supervisorScope {

                launch(CoroutineExceptionHandler { _, throwable ->
                    user =
                        Error(throwable.message.toString(), Exception("Error launching coroutine"))
                }) {
                    getUser()
                }

                launch(CoroutineExceptionHandler { _, throwable ->
                    postList =
                        Error(throwable.message.toString(), Exception("Error launching coroutine"))
                }) {
                    getPostList()
                }

                launch(CoroutineExceptionHandler { _, throwable ->
                    postList =
                        Error(throwable.message.toString(), Exception("Error launching coroutine"))
                }) {
                    getAlbumList()
                }
            }
        }
    }

    private suspend fun getUser() {
        user = State.Loading("init Loading")
        user = when (val response = getUserUseCase.getUser(sessionManager.user.id)) {
            is Resource.Success -> {
                Log.d(TAG, "user ${response.data}")
                // force unwrap because null values must be handled earlier
                val user = UserToUserModelMapper.map(response.data!!)
                Success(user, response.message ?: "")
            }
            is Resource.Error -> {
                Log.d(TAG, "error ${response.exception.localizedMessage}")
                Error(response.message.toString(), response.exception)
            }
        }
        refreshData()
    }

    private suspend fun getPostList() {
        postList = State.Loading("init Loading")
        postList = when (val response = getPostListUseCase.getPostList(sessionManager.user.id)) {
            is Resource.Success -> {
                Log.d(TAG, "postList came, size: ${response.data!!.size}")
                // force unwrap because null values must be handled earlier
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

    private suspend fun getAlbumList() {
        albumList = State.Loading("init Loading")
        albumList = when (val response = getAlbumListUseCase.getAlbumList(sessionManager.user.id)) {
            is Resource.Success -> {
                Log.d(TAG, "albumList ${response.data}")
                // force unwrap because null values must be handled earlier
                val albums = AlbumToAlbumModelMapper.map(response.data!!)

                Success(albums, "Without first photos")
            }
            is Resource.Error -> {
                Log.d(TAG, "error ${response.exception.localizedMessage}")
                Error(response.message.toString(), response.exception)
            }
        }
        refreshData()
        startLoadingFirstPhotosOfAlbums()
    }

    private suspend fun startLoadingFirstPhotosOfAlbums() { //TODO: remove parameter
        when (albumList) {
            is State.Success -> {
                Log.d(TAG, "start loading first photos for albums")
                val localAlbumList = albumList.data

                localAlbumList?.map {
                    it.firstPhoto = getAlbumFirstPhoto(it)
                }

                albumList = Success(localAlbumList, "With first photos")
                refreshData()
            }
            else -> {
                Log.d(TAG, "You CANT start loading photos for albums if it's state not SUCCESS")
            }
        }
    }

    private suspend fun getAlbumFirstPhoto(albumModel: AlbumModel): PhotoModel? {
        if (albumModel.id == null) {
            return null
        }
        // passing hardcoded one because we using hack in PhotoRepositoryImpl
        val photoModel = when (val response = getPhotoUseCase.getPhoto(albumModel.id!!, 1)) {
            is Resource.Success -> {
                Log.d(
                    TAG,
                    "first photo of album with id ${albumModel.id!!} is  ${response.data?.id}"
                )

                val photoModel = PhotoToPhotoModelMapper.mapPhoto(response.data)
                return photoModel
            }
            is Resource.Error -> {
                Log.d(TAG, "error ${response.exception.localizedMessage}")
                return null
            }
        }

        return photoModel
    }

    fun refreshData() = viewModelScope.launch(mainDispatcher) {
        _adapterItems.value = ProfileTransformator.transform(user, albumList, postList)
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}

