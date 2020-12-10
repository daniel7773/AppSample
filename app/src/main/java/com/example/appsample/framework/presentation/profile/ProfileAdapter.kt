package com.example.appsample.framework.presentation.profile

import androidx.recyclerview.widget.DiffUtil
import com.example.appsample.framework.presentation.profile.adapters.UserActionsAdapterDelegate
import com.example.appsample.framework.presentation.profile.adapters.UserAlbumsAdapterDelegate
import com.example.appsample.framework.presentation.profile.adapters.UserDetailsAdapterDelegate
import com.example.appsample.framework.presentation.profile.adapters.UserInfoAdapterDelegate
import com.example.appsample.framework.presentation.profile.adapters.UserPostAdapterDelegate
import com.example.appsample.framework.presentation.profile.adapters.separators.DividerAdapterDelegate
import com.example.appsample.framework.presentation.profile.adapters.separators.EmptySpaceAdapterDelegate
import com.example.appsample.framework.presentation.profile.models.ProfileElement
import com.example.appsample.framework.presentation.profile.models.UserInfoElement
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class ProfileAdapter @ExperimentalCoroutinesApi constructor() :
    AsyncListDifferDelegationAdapter<ProfileElement>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<ProfileElement>() {
        override fun areItemsTheSame(
            oldItem: ProfileElement,
            newItem: ProfileElement
        ): Boolean {
            return oldItem::class == newItem::class && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProfileElement, newItem: ProfileElement): Boolean {
            return if (oldItem is UserInfoElement && newItem is UserInfoElement) {
                oldItem.id == newItem.id &&
                        oldItem.user == newItem.user
            } else {
                oldItem == newItem
            }
        }
    }

    fun updateData(items: Sequence<ProfileElement>?) {
        this.items = items?.toList()
    }

    init {
        delegatesManager
            .addDelegate(UserInfoAdapterDelegate())

            .addDelegate(UserActionsAdapterDelegate())

            .addDelegate(UserDetailsAdapterDelegate())

            .addDelegate(UserAlbumsAdapterDelegate())

            .addDelegate(UserPostAdapterDelegate())

            .addDelegate(DividerAdapterDelegate())
            .addDelegate(EmptySpaceAdapterDelegate())
    }

}

