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
import com.example.appsample.business.interactors.profile.GetCommentListUseCase
import com.example.appsample.business.interactors.profile.GetPhotoUseCase
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
import com.example.appsample.framework.presentation.profile.mappers.PhotoToPhotoModelMapper
import com.example.appsample.framework.presentation.profile.mappers.PostToPostModelMapper
import com.example.appsample.framework.presentation.profile.model.AlbumModel
import com.example.appsample.framework.presentation.profile.model.PhotoModel
import com.example.appsample.framework.presentation.profile.model.PostModel
import com.example.appsample.framework.presentation.profile.model.ProfileElement
import com.example.appsample.framework.presentation.profile.screens.main.adapters.ProfileTransformator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

private const val USER_ID_KEY = "userId"

@ExperimentalCoroutinesApi
class ProfileViewModel constructor( // I suppose it is better to use database instead of SavedStateHandle for complex data
    private val mainDispatcher: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val getPostListUseCase: GetPostListUseCase,
    private val getCommentListUseCase: GetCommentListUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAlbumListUseCase: GetAlbumListUseCase,
    private val getPhotoUseCase: GetPhotoUseCase,
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
        savedStateHandle.set(USER_ID_KEY, sessionManager.user.id!!) // TODO: add back when find out how make tests not to crash here
        startSearch()
    }

    fun startSearch() {
        viewModelScope.launch(mainDispatcher) {
            supervisorScope {
                launch(CoroutineExceptionHandler { _, throwable ->
                    user = getError(throwable)
                }) { searchUser() }

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
        user = Loading("init Loading")
        if (userId.value == null) {
            throw java.lang.Exception("User id cant be NULL")
        }
        user = when (val response = getUserUseCase.getUser(userId.value!!)) {
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

    private suspend fun searchPostList() {
        postList = Loading("init Loading")
        if (userId.value == null) {
            throw java.lang.Exception("User id cant be NULL")
        }
        when (val response = getPostListUseCase.getPostList(userId.value!!)) {
            is Resource.Success -> {
                Log.d(TAG, "postList came, size: ${response.data!!.size}")
                // force unwrap because null values must be handled earlier
                val postList = PostToPostModelMapper.mapList(response.data!!)
                startLoadingCommentsOfPost(postList)
            }
            is Resource.Error -> {
                Log.d(TAG, "getPostList error ${response.exception.localizedMessage}")
                postList = Error(response.message.toString(), response.exception)
            }
        }
    }

    private suspend fun startLoadingCommentsOfPost(postModelList: List<PostModel>) {

        postModelList.map { it.commentsSize = getCommentsSize(it) } // adding comments size to posts

        joinAll()
        postList = Success(postModelList, "With comments size")
        refreshData()
    }

    private suspend fun getCommentsSize(postModel: PostModel): Int? {
        if (postModel.id == null) {
            return null
        }
        // passing hardcoded photoId because we using hack in PhotoRepositoryImpl
        val postCommentListSize =
            when (val response = getCommentListUseCase.getCommentList(postModel.id!!)) {
                is Resource.Success -> {
                    Log.d(
                        TAG,
                        "comments size of post with id ${postModel.id!!} is  ${response.data?.size}"
                    )

                    return response.data?.size
                }
                is Resource.Error -> {
                    Log.d(TAG, "getAlbumFirstPhoto error ${response.exception.localizedMessage}")
                    return null
                }
            }

        return postCommentListSize
    }

    private suspend fun searchAlbumList() {
        albumList = Loading("init Loading")
        if (userId.value == null) {
            throw java.lang.Exception("User id cant be NULL")
        }
        when (val response = getAlbumListUseCase.getAlbumList(userId.value!!)) {
            is Resource.Success -> {
                Log.d(TAG, "albumList ${response.data}")
                // force unwrap because null values must be handled earlier
                val albums = AlbumToAlbumModelMapper.map(response.data!!)

                startLoadingFirstPhotosOfAlbums(albums)
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
        } // adding first photos to album

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

                return PhotoToPhotoModelMapper.mapPhoto(response.data)
            }
            is Resource.Error -> {
                Log.d(TAG, "getAlbumFirstPhoto error ${response.exception.localizedMessage}")
                return null
            }
        }

        return photoModel
    }

    private fun refreshData() = viewModelScope.launch(mainDispatcher) {
        _adapterItems.value = ProfileTransformator.transform(user, albumList, postList)
        _isLoading.value = user is Loading || albumList is Loading || postList is Loading
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}

