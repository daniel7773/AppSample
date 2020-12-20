package com.example.appsample.framework.presentation.profile.screens.main.adapters.separators

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.R
import com.example.appsample.framework.presentation.profile.models.EmptySpace
import com.example.appsample.framework.presentation.profile.models.ProfileElement
import com.example.appsample.framework.presentation.profile.screens.main.adapters.separators.EmptySpaceAdapterDelegate.ViewHolder
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer

class EmptySpaceAdapterDelegate :
    AbsListItemAdapterDelegate<EmptySpace, ProfileElement, ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.empty_space, parent, false)
        )
    }

    override fun isForViewType(
        item: ProfileElement,
        items: MutableList<ProfileElement>,
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