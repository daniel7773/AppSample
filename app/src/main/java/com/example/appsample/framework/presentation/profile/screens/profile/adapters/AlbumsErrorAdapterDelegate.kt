package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.util.Log
import com.example.appsample.databinding.BlockAlbumsErrorBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.AlbumsErrorElement


class AlbumsErrorAdapterDelegate : ViewBindingDelegateAdapter<AlbumsErrorElement, BlockAlbumsErrorBinding>(
    BlockAlbumsErrorBinding::inflate
) {

    override fun isForViewType(item: Any): Boolean = item is AlbumsErrorElement

    override fun AlbumsErrorElement.getItemId(): Any = id

    override fun BlockAlbumsErrorBinding.onRecycled() {}

    override fun BlockAlbumsErrorBinding.onAttachedToWindow() {
        Log.d(AlbumsErrorAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockAlbumsErrorBinding.onDetachedFromWindow() {
        Log.d(AlbumsErrorAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }

    override fun BlockAlbumsErrorBinding.onBind(item: AlbumsErrorElement) {

    }
}