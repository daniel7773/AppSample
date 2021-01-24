package com.example.appsample.framework.presentation.profile.screens.post.adapters

import android.util.Log
import com.example.appsample.databinding.BlockPostSourceBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.PostSourceElement


class PostSourceAdapterDelegate :
    ViewBindingDelegateAdapter<PostSourceElement, BlockPostSourceBinding>(
        BlockPostSourceBinding::inflate
    ) {

    override fun BlockPostSourceBinding.onBind(item: PostSourceElement) {
        if (item.postModel.title != null && item.postModel.id != null) {
            post = item.postModel
        }
    }

    override fun isForViewType(item: Any): Boolean = item is PostSourceElement

    override fun PostSourceElement.getItemId(): Any = id

    override fun BlockPostSourceBinding.onRecycled() {}

    override fun BlockPostSourceBinding.onAttachedToWindow() {
        Log.d(PostSourceAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockPostSourceBinding.onDetachedFromWindow() {
        Log.d(PostSourceAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}