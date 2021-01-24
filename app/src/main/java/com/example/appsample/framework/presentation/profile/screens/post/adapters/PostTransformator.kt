package com.example.appsample.framework.presentation.profile.screens.post.adapters

import android.util.Log
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.base.presentation.delegateadapter.separators.Divider
import com.example.appsample.framework.base.presentation.delegateadapter.separators.EmptySpace
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.profile.adapterelements.PostBodyElement
import com.example.appsample.framework.presentation.profile.adapterelements.PostCommentElement
import com.example.appsample.framework.presentation.profile.adapterelements.PostSourceElement
import com.example.appsample.framework.presentation.profile.model.CommentModel
import com.example.appsample.framework.presentation.profile.model.PostModel


object PostTransformator {

    private val TAG = "PostTransformator"


    fun transform(
        post: State<PostModel?>,
        comments: State<List<CommentModel>?>
    ) = emptySequence<AdapterElement>()
        .plus(Divider("divider_after_toolbar"))
        .plus(getPost(post))
        .plus(getCommentList(comments))

    fun getPost(postModel: State<PostModel?>) = when (postModel) {
        is State.Success -> {
            if (postModel.data == null) emptySequence<AdapterElement>()
            Log.d(TAG, "added success to userAdapterItem")
            emptySequence<AdapterElement>()
                .plus(PostSourceElement(postModel.data!!))
                .plus(Divider("divider_after_post_source"))
                .plus(PostBodyElement(postModel.data!!))
                .plus(EmptySpace("empty_space_after_post_body"))
                .plus(Divider("divider_after_post_body"))
        }
        else -> {
            emptySequence()
        }
    }

    fun getCommentList(commentListState: State<List<CommentModel>?>): Sequence<AdapterElement> =
        when (commentListState) {
            is State.Success -> {
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