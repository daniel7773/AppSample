package com.example.appsample.framework.presentation.profile.screens.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.model.Comment
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.business.domain.state.DataState.Error
import com.example.appsample.business.domain.state.DataState.Idle
import com.example.appsample.business.domain.state.DataState.Loading
import com.example.appsample.business.domain.state.DataState.Success
import com.example.appsample.business.interactors.profile.GetCommentListUseCase
import com.example.appsample.business.interactors.profile.GetPostUseCase
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.presentation.profile.screens.post.adapters.PostTransformator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Named

private const val POST_ID_KEY = "userId"

@ExperimentalCoroutinesApi
class PostViewModel constructor( // I suppose it is better to use database instead of SavedStateHandle for complex data
    private val mainDispatcher: CoroutineDispatcher,
    @Named("DispatcherIO") private val ioDispatcher: CoroutineDispatcher,
    private val getCommentListUseCase: GetCommentListUseCase,
    private val getPostUseCase: GetPostUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _items: MutableLiveData<Sequence<AdapterElement>?> =
        MutableLiveData(emptySequence())
    val items: LiveData<Sequence<AdapterElement>?> by lazy { _items }

    private val _postId: MutableLiveData<Int> = savedStateHandle.getLiveData(POST_ID_KEY)
    val postId: LiveData<Int> = _postId

    var post: DataState<Post?> = Success(Post(), "Holder, remove later")

    var commentList: DataState<List<Comment>?> = Idle()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: MutableLiveData<Boolean> by lazy { _isLoading }

    init {
        if (_postId.value != null) { // it means viewModel is recreating and will not be called from fragment
            startSearch()
        }
    }

    fun isPostIdNull() = _postId.value == null

    fun setPostId(postId: Int) = savedStateHandle.set(POST_ID_KEY, postId)

    fun startSearch() {
        viewModelScope.launch(ioDispatcher) { launchSearch() }
    }

    private suspend fun launchSearch() {
        if (isPostIdNull()) throw Exception("postId should not be NULL when starting search")
        val albumId = postId.value!!

        supervisorScope {
            launch(CoroutineExceptionHandler { _, throwable ->
                commentList = getError(throwable)
            }) {
                searchCommentList(albumId)
            }

            launch(CoroutineExceptionHandler { _, throwable -> post = getError(throwable) }) {
                searchPost(albumId)
            }
        }
    }


    private suspend fun searchCommentList(postId: Int) {
        commentList = Loading(null, "Loading")
        getCommentListUseCase.getCommentList(postId).collect { comments ->
            commentList = comments
            refreshData()
        }
    }

    private suspend fun searchPost(postId: Int) {
        post = Loading(null, "Loading")
        getPostUseCase.getPost(postId).collect { post ->
            this.post = post
            refreshData()
        }
    }

    private fun refreshData() = viewModelScope.launch(mainDispatcher) {
        _items.value = PostTransformator.transform(post, commentList)
        isLoading.value = commentList is Loading || post is Loading
    }

    private fun <T> getError(throwable: Throwable) = Error<T>(null, "Error launching coroutine in ViewModel", Exception(throwable))

    companion object {
        private const val TAG = "PostViewModel"
    }

}