package com.example.appsample.framework.presentation.profile.screens.post.adapters

import android.util.Log
import com.example.appsample.business.domain.model.Comment
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.base.presentation.delegateadapter.separators.Divider
import com.example.appsample.framework.base.presentation.delegateadapter.separators.EmptySpace
import com.example.appsample.framework.presentation.profile.adapterelements.PostBodyElement
import com.example.appsample.framework.presentation.profile.adapterelements.PostCommentElement
import com.example.appsample.framework.presentation.profile.adapterelements.PostSourceElement


object PostTransformator {

    private val TAG = "PostTransformator"


    fun transform(
        post: DataState<Post?>,
        comments: DataState<List<Comment>?>
    ) = emptySequence<AdapterElement>()
        .plus(Divider("divider_after_toolbar"))
        .plus(getPost(post))
        .plus(getCommentList(comments))

    fun getPost(post: DataState<Post?>) = when (post) {
        is DataState.Success -> {
            if (post.data == null) emptySequence<AdapterElement>()
            Log.d(TAG, "added success to userAdapterItem")
            emptySequence<AdapterElement>()
                .plus(PostSourceElement(post.data!!))
                .plus(Divider("divider_after_post_source"))
                .plus(PostBodyElement(post.data!!))
                .plus(EmptySpace("empty_space_after_post_body"))
                .plus(Divider("divider_after_post_body"))
        }
        else -> {
            emptySequence()
        }
    }

    fun getCommentList(commentListState: DataState<List<Comment>?>): Sequence<AdapterElement> =
        when (commentListState) {
            is DataState.Success -> {
                if (commentListState.data == null) emptySequence<AdapterElement>()
                emptySequence<AdapterElement>()
                    .plus(
                        commentListState.data!!.flatMap { commentModel ->
                            emptySequence<AdapterElement>().plus(PostCommentElement(commentModel))
                        })
            }
            else -> {
                emptySequence()
            }
        }
}