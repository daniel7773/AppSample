package com.example.appsample.framework.presentation.profile.screens.post.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.BlockPostBodyBinding
import com.example.appsample.framework.presentation.profile.model.post.PostBodyElement
import com.example.appsample.framework.presentation.profile.model.post.PostElement
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
class PostBodyAdapterDelegate @ExperimentalCoroutinesApi constructor() :
    AbsListItemAdapterDelegate<PostBodyElement, PostElement, PostBodyAdapterDelegate.ViewHolder>() {

    @ExperimentalCoroutinesApi
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val blockPostBodyBinding =
            BlockPostBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(blockPostBodyBinding)
    }

    override fun isForViewType(
        item: PostElement,
        items: MutableList<PostElement>,
        position: Int
    ): Boolean {
        return item is PostBodyElement
    }

    override fun onBindViewHolder(
        item: PostBodyElement,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockPostBodyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostBodyElement) {
            binding.post = item.postModel
        }
    }
}