package com.example.appsample.framework.presentation.profile.screens.post.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.BlockPostCommentBinding
import com.example.appsample.framework.presentation.profile.model.post.PostCommentElement
import com.example.appsample.framework.presentation.profile.model.post.PostElement
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
class PostCommentAdapterDelegate @ExperimentalCoroutinesApi constructor() :
    AbsListItemAdapterDelegate<PostCommentElement, PostElement, PostCommentAdapterDelegate.ViewHolder>() {

    @ExperimentalCoroutinesApi
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val blockPostCommentBinding =
            BlockPostCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(blockPostCommentBinding)
    }

    override fun isForViewType(
        item: PostElement,
        items: MutableList<PostElement>,
        position: Int
    ): Boolean {
        return item is PostCommentElement
    }

    override fun onBindViewHolder(
        item: PostCommentElement,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockPostCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostCommentElement) {
            binding.comment = item.comment
        }
    }
}