package com.example.appsample.framework.presentation.profile.screens.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.BlockUserPostBinding
import com.example.appsample.framework.presentation.profile.model.PostModel
import com.example.appsample.framework.presentation.profile.model.ProfileElement
import com.example.appsample.framework.presentation.profile.model.UserPostsElement
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserPostAdapterDelegate(
    private val onPostClick: ((PostModel) -> Unit)
) :
    AbsListItemAdapterDelegate<UserPostsElement, ProfileElement, UserPostAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val blockUserPostBinding =
            BlockUserPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(blockUserPostBinding, onPostClick)
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
        private val binding: BlockUserPostBinding,
        private val onPostClick: ((PostModel) -> Unit)
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostModel?) {
            binding.post = item
            val isItemNull = item == null
            if (isItemNull) {
                binding.postTitle.visibility = View.GONE
            } else {
                binding.postTitle.text = item!!.title
                binding.descriptionText.text = item.body
                val commentsSize: String
                if (item.commentsSize != null && item.commentsSize!! > 0) {
                    commentsSize = item.commentsSize.toString()
                } else {
                    commentsSize = "?"
                }

                binding.commentText.text = commentsSize
                binding.commentText.setOnClickListener { onPostClick(item) }
                binding.headerLayout.setOnClickListener { onPostClick(item) }
            }
        }
    }
}