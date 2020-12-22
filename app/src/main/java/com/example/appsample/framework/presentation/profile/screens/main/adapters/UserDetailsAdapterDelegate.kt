package com.example.appsample.framework.presentation.profile.screens.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.BlockUserDetailsBinding
import com.example.appsample.framework.presentation.profile.model.ProfileElement
import com.example.appsample.framework.presentation.profile.model.UserDetailsElement
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
class UserDetailsAdapterDelegate @ExperimentalCoroutinesApi constructor() :
    AbsListItemAdapterDelegate<UserDetailsElement, ProfileElement, UserDetailsAdapterDelegate.ViewHolder>() {

    @ExperimentalCoroutinesApi
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val viewProductCategoryBinding =
            BlockUserDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewProductCategoryBinding)
    }

    override fun isForViewType(
        item: ProfileElement,
        items: MutableList<ProfileElement>,
        position: Int
    ): Boolean {
        return item is UserDetailsElement
    }

    override fun onBindViewHolder(
        item: UserDetailsElement,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockUserDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserDetailsElement) {
            binding.user = item.user
            binding.moreAddressDetails.setOnClickListener { onClickAddressDetails() }
            binding.hideAddressDetails.setOnClickListener { onClickAddressDetails() }
        }

        private fun onClickAddressDetails() {
            if (binding.moreAddressDetails.visibility == View.VISIBLE) {
                binding.moreAddressDetails.visibility = View.GONE
                binding.addressDetailsLayout.visibility = View.VISIBLE
                binding.hideAddressDetails.visibility = View.VISIBLE
            } else {
                binding.moreAddressDetails.visibility = View.VISIBLE
                binding.addressDetailsLayout.visibility = View.GONE
                binding.hideAddressDetails.visibility = View.GONE
            }
        }
    }
}