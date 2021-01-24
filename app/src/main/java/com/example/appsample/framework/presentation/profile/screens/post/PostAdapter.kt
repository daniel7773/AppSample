package com.example.appsample.framework.presentation.profile.screens.post

import androidx.recyclerview.widget.DiffUtil
import com.example.appsample.framework.presentation.profile.model.post.PostElement
import com.example.appsample.framework.presentation.profile.screens.post.adapters.PostBodyAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.post.adapters.PostCommentAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.post.adapters.PostSourceAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.post.adapters.separators.DividerAdapterDelegate
import com.example.appsample.framework.presentation.profile.screens.post.adapters.separators.EmptySpaceAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class PostAdapter : AsyncListDifferDelegationAdapter<PostElement>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<PostElement>() {
        override fun areItemsTheSame(
            oldItem: PostElement,
            newItem: PostElement
        ): Boolean {
            return oldItem::class == newItem::class && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostElement, newItem: PostElement): Boolean {
            return oldItem == newItem
        }
    }

    fun updateData(items: Sequence<PostElement>?) {
        this.items = items?.toList()
    }

    init {
        delegatesManager
            .addDelegate(PostBodyAdapterDelegate())

            .addDelegate(PostCommentAdapterDelegate())

            .addDelegate(PostSourceAdapterDelegate())


            .addDelegate(DividerAdapterDelegate())
            .addDelegate(EmptySpaceAdapterDelegate())
    }

}

