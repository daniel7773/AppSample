package com.example.appsample.framework.presentation.profile.screens.main.adapters

import android.util.Log
import com.example.appsample.databinding.BlockUserPostBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.UserPostsElement
import com.example.appsample.framework.presentation.profile.model.PostModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserPostAdapterDelegate(
    private val onPostClick: ((PostModel) -> Unit)
) : ViewBindingDelegateAdapter<UserPostsElement, BlockUserPostBinding>(
    BlockUserPostBinding::inflate
) {

    override fun BlockUserPostBinding.onBind(item: UserPostsElement) {
        post = item.postModel

        postTitle.text = item.postModel.title
        descriptionText.text = item.postModel.body
        val commentsSize: String
        if (item.postModel.commentsSize != null && item.postModel.commentsSize!! > 0) {
            commentsSize = item.postModel.commentsSize.toString()
        } else {
            commentsSize = "?"
        }

        commentText.text = commentsSize
        commentText.setOnClickListener { onPostClick(item.postModel) }
        headerLayout.setOnClickListener { onPostClick(item.postModel) }
    }

    override fun isForViewType(item: Any): Boolean = item is UserPostsElement

    override fun UserPostsElement.getItemId(): Any = id

    override fun BlockUserPostBinding.onRecycled() {
        postTitle.text = ""
        descriptionText.text = ""
        commentText.text = ""
        commentText.setOnClickListener(null)
        headerLayout.setOnClickListener(null)
    }

    override fun BlockUserPostBinding.onAttachedToWindow() {
        Log.d(UserPostAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockUserPostBinding.onDetachedFromWindow() {
        Log.d(UserPostAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}