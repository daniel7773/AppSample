package com.example.appsample.framework.presentation.profile.screens.post.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.BlockPostSourceBinding
import com.example.appsample.framework.presentation.profile.model.post.PostElement
import com.example.appsample.framework.presentation.profile.model.post.PostSourceElement
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
class PostSourceAdapterDelegate @ExperimentalCoroutinesApi constructor() :
    AbsListItemAdapterDelegate<PostSourceElement, PostElement, PostSourceAdapterDelegate.ViewHolder>() {

    @ExperimentalCoroutinesApi
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val blockPostSourceBinding =
            BlockPostSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(blockPostSourceBinding)
    }

    override fun isForViewType(
        item: PostElement,
        items: MutableList<PostElement>,
        position: Int
    ): Boolean {
        return item is PostSourceElement
    }

    override fun onBindViewHolder(
        item: PostSourceElement,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockPostSourceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostSourceElement) {
            if (item.postModel.title != null && item.postModel.id != null) {
                binding.post = item.postModel
            }
        }
    }
}