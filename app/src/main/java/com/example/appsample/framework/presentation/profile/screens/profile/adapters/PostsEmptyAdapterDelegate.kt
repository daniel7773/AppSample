package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.util.Log
import com.example.appsample.databinding.BlockPostsEmptyBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.PostLoadingElement

class PostsEmptyAdapterDelegate : ViewBindingDelegateAdapter<PostLoadingElement, BlockPostsEmptyBinding>(
    BlockPostsEmptyBinding::inflate
) {

    override fun isForViewType(item: Any): Boolean = item is PostLoadingElement

    override fun PostLoadingElement.getItemId(): Any = id

    override fun BlockPostsEmptyBinding.onRecycled() {}

    override fun BlockPostsEmptyBinding.onAttachedToWindow() {
        Log.d(UserLoadingAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockPostsEmptyBinding.onDetachedFromWindow() {
        Log.d(UserLoadingAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }

    override fun BlockPostsEmptyBinding.onBind(item: PostLoadingElement) {

    }
}