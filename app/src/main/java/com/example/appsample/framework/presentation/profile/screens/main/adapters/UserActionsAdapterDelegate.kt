package com.example.appsample.framework.presentation.profile.screens.main.adapters

import android.util.Log
import com.example.appsample.databinding.BlockUserActionsBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.UserActionsElement


class UserActionsAdapterDelegate :
    ViewBindingDelegateAdapter<UserActionsElement, BlockUserActionsBinding>(BlockUserActionsBinding::inflate) {

    override fun BlockUserActionsBinding.onBind(item: UserActionsElement) {
        user = item.user
    }

    override fun isForViewType(item: Any): Boolean = item is UserActionsElement

    override fun UserActionsElement.getItemId(): Any = id

    override fun BlockUserActionsBinding.onRecycled() {}

    override fun BlockUserActionsBinding.onAttachedToWindow() {
        Log.d(UserActionsAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockUserActionsBinding.onDetachedFromWindow() {
        Log.d(UserActionsAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}