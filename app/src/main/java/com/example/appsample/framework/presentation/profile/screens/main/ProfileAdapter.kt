package com.example.appsample.framework.presentation.profile.screens.main

import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.example.appsample.framework.presentation.profile.model.AlbumModel
import com.example.appsample.framework.presentation.profile.model.PostModel
import com.example.appsample.framework.presentation.profile.model.ProfileElement
import com.example.appsample.framework.presentation.profile.screens.main.adapters.UserActionsAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.main.adapters.UserAlbumsAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.main.adapters.UserDetailsAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.main.adapters.UserInfoAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.main.adapters.UserPostAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.main.adapters.separators.DividerAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.main.adapters.separators.EmptySpaceAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class ProfileAdapter @ExperimentalCoroutinesApi constructor(
    private val onAlbumClick: ((ImageView, AlbumModel, Int) -> Unit),
    private val onPostClick: ((PostModel) -> Unit)
) :
    AsyncListDifferDelegationAdapter<ProfileElement>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<ProfileElement>() {
        override fun areItemsTheSame(
            oldItem: ProfileElement,
            newItem: ProfileElement
        ): Boolean {
            return oldItem::class == newItem::class && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProfileElement, newItem: ProfileElement): Boolean {
            return oldItem == newItem
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

            .addDelegate(UserAlbumsAdapterDelegate(onAlbumClick))

            .addDelegate(UserPostAdapterDelegate(onPostClick))

            .addDelegate(DividerAdapterDelegate())
            .addDelegate(EmptySpaceAdapterDelegate())
    }

}

