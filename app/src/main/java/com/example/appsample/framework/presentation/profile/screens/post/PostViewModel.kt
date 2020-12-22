package com.example.appsample.framework.presentation.profile.screens.post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsample.business.domain.repository.Resource
import com.example.appsample.business.interactors.profile.GetCommentListUseCase
import com.example.appsample.business.interactors.profile.GetPostUseCase
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.profile.mappers.CommentToCommentModelMapper
import com.example.appsample.framework.presentation.profile.mappers.PostToPostModelMapper
import com.example.appsample.framework.presentation.profile.model.CommentModel
import com.example.appsample.framework.presentation.profile.model.PostModel
import com.example.appsample.framework.presentation.profile.model.post.PostElement
import com.example.appsample.framework.presentation.profile.screens.post.adapters.PostTransformator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class PostViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val getCommentListUseCase: GetCommentListUseCase,
    private val getPostUseCase: GetPostUseCase
) : ViewModel() {

    private val _items: MutableLiveData<Sequence<PostElement>?> by lazy {
        MutableLiveData(emptySequence())
    }
    val items: LiveData<Sequence<PostElement>?> by lazy {
        _items
    }

    var post: State<PostModel?> = State.Success(
        PostModel(1, 1, 5, "title", "body"),
        "Holder, remove later"
    )
    var commentList: State<List<CommentModel>?> = State.Unknown()

    private val _isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData(true)
    }
    val isLoading: MutableLiveData<Boolean> by lazy {
        _isLoading
    }

    fun searchCommentList(postId: Int) {
        viewModelScope.launch(mainDispatcher) {
            commentList = when (val response = getCommentListUseCase.getCommentList(postId)) {
                is Resource.Success -> {
                    Log.d(TAG, "comment list size ${response.data?.size}")
                    // force unwrap because null values must be handled earlier
                    val commentModelList = CommentToCommentModelMapper.map(response.data!!)
                    State.Success(
                        commentModelList,
                        response.message ?: "searchCommentList Success in ViewModel"
                    )
                }
                is Resource.Error -> {
                    Log.d(TAG, "searchCommentList error ${response.exception.localizedMessage}")
                    State.Error(response.message.toString(), response.exception)
                }
            }
            refreshData()
        }
    }

    fun searchPost(postId: Int) {
        viewModelScope.launch(mainDispatcher) {
            post = when (val response = getPostUseCase.getPost(postId)) {
                is Resource.Success -> {
                    Log.d(TAG, "got post SUCCESS")
                    // force unwrap because null values must be handled earlier
                    val postModel = PostToPostModelMapper.map(response.data!!)
                    State.Success(
                        postModel,
                        response.message ?: "searchPost Success in ViewModel"
                    )
                }
                is Resource.Error -> {
                    Log.d(TAG, "searchPost error ${response.exception.localizedMessage}")
                    State.Error(response.message.toString(), response.exception)
                }
            }
            refreshData()
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