package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.util.Log
import com.example.appsample.databinding.BlockUserLoadingBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.UserErrorElement


class UserErrorAdapterDelegate : ViewBindingDelegateAdapter<UserErrorElement, BlockUserLoadingBinding>(
    BlockUserLoadingBinding::inflate
) {

    override fun isForViewType(item: Any): Boolean = item is UserErrorElement

    override fun UserErrorElement.getItemId(): Any = id

    override fun BlockUserLoadingBinding.onRecycled() {}

    override fun BlockUserLoadingBinding.onAttachedToWindow() {
        Log.d(UserErrorAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockUserLoadingBinding.onDetachedFromWindow() {
        Log.d(UserErrorAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }

    override fun BlockUserLoadingBinding.onBind(item: UserErrorElement) {

    }
}