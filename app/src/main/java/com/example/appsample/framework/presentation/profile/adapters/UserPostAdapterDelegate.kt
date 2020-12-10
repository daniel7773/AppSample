package com.example.appsample.framework.presentation.profile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.BlockUserActionsBinding
import com.example.appsample.databinding.BlockUserPostBinding
import com.example.appsample.framework.presentation.profile.models.PostModel
import com.example.appsample.framework.presentation.profile.models.ProfileElement
import com.example.appsample.framework.presentation.profile.models.UserActionsElement
import com.example.appsample.framework.presentation.profile.models.UserPostsElement
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserPostAdapterDelegate 
    : AbsListItemAdapterDelegate<UserPostsElement, ProfileElement, UserPostAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val blockUserPostBinding =
            BlockUserPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(blockUserPostBinding)
    }

    override fun onBindViewHolder(
        item: UserPostsElement,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item.post)
    }

    override fun isForViewType(
        item: ProfileElement,
        items: MutableList<ProfileElement>,
        position: Int
    ): Boolean {
        return item is UserPostsElement
    }

    class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockUserPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostModel?) {
            binding.post = item
            val isItemNull = item == null
            if (isItemNull) {
                binding.postTitle.visibility = View.GONE
            } else {
                binding.postTitle.text = item!!.title
                binding.descriptionText.text = item.body
            }
        }
    }
}