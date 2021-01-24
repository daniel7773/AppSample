package com.example.appsample.framework.presentation.profile.screens.post.adapters

import android.util.Log
import com.example.appsample.databinding.BlockPostCommentBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.PostCommentElement


class PostCommentAdapterDelegate :
    ViewBindingDelegateAdapter<PostCommentElement, BlockPostCommentBinding>(
        BlockPostCommentBinding::inflate
    ) {

    override fun BlockPostCommentBinding.onBind(item: PostCommentElement) {
        comment = item.comment
    }

    override fun isForViewType(item: Any): Boolean = item is PostCommentElement

    override fun PostCommentElement.getItemId(): Any = id

    override fun BlockPostCommentBinding.onRecycled() {}

    override fun BlockPostCommentBinding.onAttachedToWindow() {
        Log.d(PostCommentAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockPostCommentBinding.onDetachedFromWindow() {
        Log.d(PostCommentAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}