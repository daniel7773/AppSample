package com.example.appsample.framework.presentation.profile.screens.post.adapters.separators

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.R
import com.example.appsample.framework.presentation.profile.model.post.EmptySpace
import com.example.appsample.framework.presentation.profile.model.post.PostElement
import com.example.appsample.framework.presentation.profile.screens.post.adapters.separators.EmptySpaceAdapterDelegate.ViewHolder
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer

class EmptySpaceAdapterDelegate :
    AbsListItemAdapterDelegate<EmptySpace, PostElement, ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.empty_space_post, parent, false)
        )
    }

    override fun isForViewType(
        item: PostElement,
        items: MutableList<PostElement>,
        position: Int
    ): Boolean {
        return item is EmptySpace
    }

    override fun onBindViewHolder(
        item: EmptySpace,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) = Unit


    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer
}