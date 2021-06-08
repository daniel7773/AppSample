package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.util.Log
import com.example.appsample.databinding.BlockPostLoadingBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.PostLoadingElement

class PostLoadingAdapterDelegate : ViewBindingDelegateAdapter<PostLoadingElement, BlockPostLoadingBinding>(
    BlockPostLoadingBinding::inflate
) {

    override fun isForViewType(item: Any): Boolean = item is PostLoadingElement

    override fun PostLoadingElement.getItemId(): Any = id

    override fun BlockPostLoadingBinding.onRecycled() {}

    override fun BlockPostLoadingBinding.onAttachedToWindow() {
        Log.d(UserLoadingAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockPostLoadingBinding.onDetachedFromWindow() {
        Log.d(UserLoadingAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }

    override fun BlockPostLoadingBinding.onBind(item: PostLoadingElement) {

    }
}