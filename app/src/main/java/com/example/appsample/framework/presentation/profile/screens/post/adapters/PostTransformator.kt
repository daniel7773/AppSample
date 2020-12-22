package com.example.appsample.framework.presentation.profile.screens.post.adapters

import android.util.Log
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.profile.model.CommentModel
import com.example.appsample.framework.presentation.profile.model.PostModel
import com.example.appsample.framework.presentation.profile.model.post.Divider
import com.example.appsample.framework.presentation.profile.model.post.PostBodyElement
import com.example.appsample.framework.presentation.profile.model.post.PostCommentElement
import com.example.appsample.framework.presentation.profile.model.post.PostElement
import com.example.appsample.framework.presentation.profile.model.post.PostSourceElement

object PostTransformator {

    private val TAG = "PostTransformator"


    fun transform(
        post: State<PostModel?>,
        comments: State<List<CommentModel>?>
    ) = emptySequence<PostElement>()
        .plus(getPost(post))
        .plus(getCommentList(comments))

    fun getPost(postModel: State<PostModel?>) = when (postModel) {
        is State.Success -> {
            if (postModel.data == null) emptySequence<PostElement>()
            Log.d(TAG, "added success to userAdapterItem")
            emptySequence<PostElement>().plus(
                PostSourceElement(postModel.data!!)
            )
                .plus(Divider("divider_after_post_source"))
                .plus(PostBodyElement(postModel.data!!))
                .plus(Divider("divider_after_post_body"))
        }
        else -> {
            emptySequence<PostElement>()
        }
    }

    fun getCommentList(commentListState: State<List<CommentModel>?>): Sequence<PostElement> =
        when (commentListState) {
            is State.Success -> {
                if (commentListState.data == null) emptySequence<PostElement>()
                emptySequence<PostElement>()
                    .plus(
                        commentListState.data!!.flatMap { commentModel ->
                            emptySequence<PostElement>().plus(PostCommentElement(commentModel))
                                .plus(Divider("divider_after_comment_${commentModel.id}_for_post_${commentModel.postId}"))
                        })
            }
            else -> {
                emptySequence<PostElement>()
            }
        }
}