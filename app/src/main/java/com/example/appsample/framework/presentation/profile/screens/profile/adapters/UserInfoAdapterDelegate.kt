package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.util.Log
import com.example.appsample.databinding.BlockUserInfoBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.UserInfoElement


class UserInfoAdapterDelegate : ViewBindingDelegateAdapter<UserInfoElement, BlockUserInfoBinding>(
    BlockUserInfoBinding::inflate
) {

    override fun BlockUserInfoBinding.onBind(item: UserInfoElement) {
        user = item.user
    }

    override fun isForViewType(item: Any): Boolean = item is UserInfoElement

    override fun UserInfoElement.getItemId(): Any = id

    override fun BlockUserInfoBinding.onRecycled() {}

    override fun BlockUserInfoBinding.onAttachedToWindow() {
        Log.d(UserInfoAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockUserInfoBinding.onDetachedFromWindow() {
        Log.d(UserInfoAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}