package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.util.Log
import com.example.appsample.databinding.BlockUserLoadingBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.UserLoadingElement


class UserLoadingAdapterDelegate : ViewBindingDelegateAdapter<UserLoadingElement, BlockUserLoadingBinding>(
    BlockUserLoadingBinding::inflate
) {

    override fun isForViewType(item: Any): Boolean = item is UserLoadingElement

    override fun UserLoadingElement.getItemId(): Any = id

    override fun BlockUserLoadingBinding.onRecycled() {}

    override fun BlockUserLoadingBinding.onAttachedToWindow() {
        Log.d(UserLoadingAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockUserLoadingBinding.onDetachedFromWindow() {
        Log.d(UserLoadingAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }

    override fun BlockUserLoadingBinding.onBind(item: UserLoadingElement) {

    }
}