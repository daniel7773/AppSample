package com.example.appsample.framework.presentation.profile.screens.post.adapters

import android.util.Log
import com.example.appsample.databinding.BlockPostBodyBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.PostBodyElement

class PostBodyAdapterDelegate : ViewBindingDelegateAdapter<PostBodyElement, BlockPostBodyBinding>(
    BlockPostBodyBinding::inflate
) {

    override fun BlockPostBodyBinding.onBind(item: PostBodyElement) {
        post = item.post
    }

    override fun isForViewType(item: Any): Boolean = item is PostBodyElement

    override fun PostBodyElement.getItemId(): Any = id

    override fun BlockPostBodyBinding.onRecycled() {}

    override fun BlockPostBodyBinding.onAttachedToWindow() {
        Log.d(PostBodyAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockPostBodyBinding.onDetachedFromWindow() {
        Log.d(PostBodyAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}