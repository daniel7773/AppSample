package com.example.appsample.framework.presentation.profile.screens.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.BlockUserActionsBinding
import com.example.appsample.framework.presentation.profile.model.ProfileElement
import com.example.appsample.framework.presentation.profile.model.UserActionsElement
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
class UserActionsAdapterDelegate @ExperimentalCoroutinesApi constructor() :
    AbsListItemAdapterDelegate<UserActionsElement, ProfileElement, UserActionsAdapterDelegate.ViewHolder>() {

    @ExperimentalCoroutinesApi
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val blockUserActionsBinding =
            BlockUserActionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(blockUserActionsBinding)
    }

    override fun isForViewType(
        item: ProfileElement,
        items: MutableList<ProfileElement>,
        position: Int
    ): Boolean {
        return item is UserActionsElement
    }

    override fun onBindViewHolder(
        item: UserActionsElement,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockUserActionsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserActionsElement) {
            binding.user = item.user
        }
    }
}