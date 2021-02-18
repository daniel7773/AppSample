package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.util.Log
import android.view.View
import com.example.appsample.databinding.BlockUserDetailsBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.UserDetailsElement


class UserDetailsAdapterDelegate :
    ViewBindingDelegateAdapter<UserDetailsElement, BlockUserDetailsBinding>(
        BlockUserDetailsBinding::inflate
    ) {

    override fun BlockUserDetailsBinding.onBind(item: UserDetailsElement) {

        fun onClickAddressDetails() {
            if (moreAddressDetails.visibility == View.VISIBLE) {
                moreAddressDetails.visibility = View.GONE
                addressDetailsLayout.visibility = View.VISIBLE
                hideAddressDetails.visibility = View.VISIBLE
            } else {
                moreAddressDetails.visibility = View.VISIBLE
                addressDetailsLayout.visibility = View.GONE
                hideAddressDetails.visibility = View.GONE
            }
        }

        user = item.user
        moreAddressDetails.setOnClickListener { onClickAddressDetails() }
        hideAddressDetails.setOnClickListener { onClickAddressDetails() }
    }

    override fun isForViewType(item: Any): Boolean = item is UserDetailsElement

    override fun UserDetailsElement.getItemId(): Any = id

    override fun BlockUserDetailsBinding.onRecycled() {
        moreAddressDetails.setOnClickListener(null)
        hideAddressDetails.setOnClickListener(null)
    }

    override fun BlockUserDetailsBinding.onAttachedToWindow() {
        Log.d(UserDetailsAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockUserDetailsBinding.onDetachedFromWindow() {
        Log.d(UserDetailsAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}