package com.example.appsample.framework.presentation.profile.screens.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.BlockUserInfoBinding
import com.example.appsample.framework.presentation.profile.models.ProfileElement
import com.example.appsample.framework.presentation.profile.models.UserInfoElement
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
class UserInfoAdapterDelegate @ExperimentalCoroutinesApi constructor() :
    AbsListItemAdapterDelegate<UserInfoElement, ProfileElement, UserInfoAdapterDelegate.ViewHolder>() {

    @ExperimentalCoroutinesApi
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val viewProductCategoryBinding =
            BlockUserInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewProductCategoryBinding)
    }

    override fun isForViewType(
        item: ProfileElement,
        items: MutableList<ProfileElement>,
        position: Int
    ): Boolean {
        return item is UserInfoElement
    }

    override fun onBindViewHolder(
        item: UserInfoElement,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockUserInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserInfoElement) {
            binding.user = item.user
        }
    }
}