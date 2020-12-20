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
import com.example.appsample.framework.presentation.common.model.State.Loading
import com.example.appsample.framework.presentation.common.model.State.Success
import com.example.appsample.framework.presentation.common.model.State.Unknown
import com.example.appsample.framework.presentation.common.model.UserModel
import com.example.appsample.framework.presentation.profile.adapters.ProfileTransformator
import com.example.appsample.framework.presentation.profile.mappers.PostToPostModelMapper
import com.example.appsample.framework.presentation.profile.models.PostModel
import com.example.appsample.framework.presentation.profile.models.ProfileElement
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.joinAll
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

    var postList: State<List<PostModel>?> = Unknown()
    var user: State<UserModel?> = Unknown()
    var albumList: State<List<AlbumModel>?> = Unknown()

    private val _adapterItems: MutableLiveData<Sequence<ProfileElement>> by lazy {
        MutableLiveData(emptySequence())
    }
    val items: LiveData<Sequence<ProfileElement>> by lazy { _adapterItems }

    private val _isLoading by lazy { MutableLiveData(true) }
    val isLoading: LiveData<Boolean> by lazy { _isLoading }


    fun startSearch() {
        viewModelScope.launch(mainDispatcher) {
            supervisorScope {

                launch(CoroutineExceptionHandler { _, throwable ->
                    user =
                        Error(
                            throwable.message.toString(),
                            Exception("Error launching coroutine in ViewModel")
                        )
                }) {
                    getUser()
                }

                launch(CoroutineExceptionHandler { _, throwable ->
                    postList =
                        Error(
                            throwable.message.toString(),
                            Exception("Error launching coroutine in ViewModel")
                        )
                }) {
                    getPostList()
                }

                launch(CoroutineExceptionHandler { _, throwable ->
                    albumList =
                        Error(
                            throwable.message.toString(),
                            Exception("Error launching coroutine in ViewModel")
                        )
                }) {
                    getAlbumList()
                }
            }
        }
    }

    private suspend fun getUser() {
        user = Loading("init Loading")
        user = when (val response = getUserUseCase.getUser(sessionManager.user.id)) {
            is Resource.Success -> {
                Log.d(TAG, "user ${response.data}")
                // force unwrap because null values must be handled earlier
                val user = UserToUserModelMapper.map(response.data!!)
                Success(user, response.message ?: "getUser Success in ViewModel")
            }
            is Resource.Error -> {
                Log.d(TAG, "getUser error ${response.exception.localizedMessage}")
                Error(response.message.toString(), response.exception)
            }
        }
        refreshData()
    }

    private suspend fun getPostList() {
        postList = Loading("init Loading")
        postList = when (val response = getPostListUseCase.getPostList(sessionManager.user.id)) {
            is Resource.Success -> {
                Log.d(TAG, "postList came, size: ${response.data!!.size}")
                // force unwrap because null values must be handled earlier
                val postList = PostToPostModelMapper.map(response.data!!)
                Success(postList, "getPostList Success in ViewModel")
            }
            is Resource.Error -> {
                Log.d(TAG, "getPostList error ${response.exception.localizedMessage}")
                Error(response.message.toString(), response.exception)
            }
        }
        refreshData()
    }

    private suspend fun getAlbumList() {
        albumList = Loading("init Loading")
        when (val response = getAlbumListUseCase.getAlbumList(sessionManager.user.id)) {
            is Resource.Success -> {
                Log.d(TAG, "albumList ${response.data}")
                // force unwrap because null values must be handled earlier
                val albums = AlbumToAlbumModelMapper.map(response.data!!)

                startLoadingFirstPhotosOfAlbums(albums)
                Success(albums, "Without first photos")
            }
            is Resource.Error -> {
                Log.d(TAG, "getAlbumList error ${response.exception.localizedMessage}")
                albumList = Error(response.message.toString(), response.exception)
            }
        }
    }

    private suspend fun startLoadingFirstPhotosOfAlbums(localAlbumList: List<AlbumModel>) {
        localAlbumList.map {
            it.firstPhoto = getAlbumFirstPhoto(it)
        }

        joinAll()
        albumList = Success(localAlbumList, "With first photos")
        refreshData()
    }

    private suspend fun getAlbumFirstPhoto(albumModel: AlbumModel): PhotoModel? {
        if (albumModel.id == null) {
            return null
        }
        // passing hardcoded photoId because we using hack in PhotoRepositoryImpl
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
                Log.d(TAG, "getAlbumFirstPhoto error ${response.exception.localizedMessage}")
                return null
            }
        }

        return photoModel
    }

    fun refreshData() = viewModelScope.launch(mainDispatcher) {
        _adapterItems.value = ProfileTransformator.transform(user, albumList, postList)
        _isLoading.value = user is Loading || albumList is Loading || postList is Loading
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}

