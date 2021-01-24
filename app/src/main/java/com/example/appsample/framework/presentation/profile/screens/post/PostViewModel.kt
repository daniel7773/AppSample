package com.example.appsample.framework.presentation.profile.screens.post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.interactors.profile.GetCommentListUseCase
import com.example.appsample.business.interactors.profile.GetPostUseCase
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.profile.mappers.CommentToCommentModelMapper
import com.example.appsample.framework.presentation.profile.mappers.PostToPostModelMapper
import com.example.appsample.framework.presentation.profile.model.CommentModel
import com.example.appsample.framework.presentation.profile.model.PostModel
import com.example.appsample.framework.presentation.profile.screens.post.adapters.PostTransformator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val POST_ID_KEY = "userId"

@ExperimentalCoroutinesApi
class PostViewModel constructor( // I suppose it is better to use database instead of SavedStateHandle for complex data
    private val mainDispatcher: CoroutineDispatcher,
    private val getCommentListUseCase: GetCommentListUseCase,
    private val getPostUseCase: GetPostUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _items: MutableLiveData<Sequence<AdapterElement>?> =
        MutableLiveData(emptySequence())
    val items: LiveData<Sequence<AdapterElement>?> by lazy { _items }

    private val _postId: MutableLiveData<Int> = savedStateHandle.getLiveData(POST_ID_KEY)
    val postId: LiveData<Int> = _postId

    var post: State<PostModel?> = State.Success(PostModel(), "Holder, remove later")

    var commentList: State<List<CommentModel>?> = State.Unknown()

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
        if (isPostIdNull()) throw Exception("postId should not be NULL when starting search")
        val albumId = postId.value!!
        albumId.run {
            searchCommentList(this)
            searchPost(this)
        }
    }


    fun searchCommentList(postId: Int) {
        viewModelScope.launch(mainDispatcher) {
            getCommentListUseCase.getCommentList(postId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        Log.d(TAG, "comment list size ${response.data?.size}")
                        // force unwrap because null values must be handled earlier
                        val commentModelList = CommentToCommentModelMapper.map(response.data!!)
                        commentList = State.Success(
                            commentModelList,
                            response.message ?: "searchCommentList Success in ViewModel"
                        )
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "searchCommentList error ${response.exception.localizedMessage}")
                        commentList = State.Error(response.message.toString(), response.exception)
                    }
                    else -> {
                        commentList = State.Loading("Loading")
                    }
                }
                refreshData()
            }
        }
    }

    fun searchPost(postId: Int) {
        viewModelScope.launch(mainDispatcher) {
            getPostUseCase.getPost(postId).collect { response ->
                post = when (response) {
                    is Resource.Success -> {
                        Log.d(TAG, "getPost SUCCESS")
                        // force unwrap because null values must be handled earlier
                        val postModel = PostToPostModelMapper.map(response.data!!)
                        State.Success(postModel, response.message ?: "getPost Success in ViewModel")
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "searchPost error ${response.exception.localizedMessage}")
                        State.Error(response.message.toString(), response.exception)
                    }
                    else -> {
                        State.Loading("Loading")
                    }
                }
                refreshData()
            }
        }
    }

    private fun refreshData() = viewModelScope.launch(mainDispatcher) {
        _items.value = PostTransformator.transform(post, commentList)
        isLoading.value = commentList is State.Loading || post is State.Loading
    }

    companion object {
        private const val TAG = "PostViewModel"
    }

}