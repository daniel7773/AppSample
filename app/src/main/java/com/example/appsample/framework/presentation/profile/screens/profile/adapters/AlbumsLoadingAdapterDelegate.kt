package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.util.Log
import com.example.appsample.databinding.BlockAlbumsLoadingBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.AlbumsLoadingElement


class AlbumsLoadingAdapterDelegate : ViewBindingDelegateAdapter<AlbumsLoadingElement, BlockAlbumsLoadingBinding>(
    BlockAlbumsLoadingBinding::inflate
) {

    override fun isForViewType(item: Any): Boolean = item is AlbumsLoadingElement

    override fun AlbumsLoadingElement.getItemId(): Any = id

    override fun BlockAlbumsLoadingBinding.onRecycled() {}

    override fun BlockAlbumsLoadingBinding.onAttachedToWindow() {
        Log.d(AlbumsLoadingAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockAlbumsLoadingBinding.onDetachedFromWindow() {
        Log.d(AlbumsLoadingAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }

    override fun BlockAlbumsLoadingBinding.onBind(item: AlbumsLoadingElement) {

    }
}