package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.util.Log
import com.example.appsample.business.domain.model.Post
import com.example.appsample.databinding.BlockPostBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.PostElement
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class PostAdapterDelegate(
    private val onPostClick: ((Post) -> Unit)
) : ViewBindingDelegateAdapter<PostElement, BlockPostBinding>(
    BlockPostBinding::inflate
) {

    override fun BlockPostBinding.onBind(item: PostElement) {
        post = item.post

        postTitle.text = item.post.title
        descriptionText.text = item.post.body
        val commentsSize: String
        if (item.post.commentsSize != null && item.post.commentsSize!! > 0) {
            commentsSize = item.post.commentsSize.toString()
        } else {
            commentsSize = "?"
        }

        commentText.text = commentsSize
        commentText.setOnClickListener { onPostClick(item.post) }
        headerLayout.setOnClickListener { onPostClick(item.post) }
    }

    override fun isForViewType(item: Any): Boolean = item is PostElement

    override fun PostElement.getItemId(): Any = id

    override fun BlockPostBinding.onRecycled() {
        postTitle.text = ""
        descriptionText.text = ""
        commentText.text = ""
        commentText.setOnClickListener(null)
        headerLayout.setOnClickListener(null)
    }

    override fun BlockPostBinding.onAttachedToWindow() {
        Log.d(PostAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockPostBinding.onDetachedFromWindow() {
        Log.d(PostAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}