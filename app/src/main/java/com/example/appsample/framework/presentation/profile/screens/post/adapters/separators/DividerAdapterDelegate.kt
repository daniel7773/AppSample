package com.example.appsample.framework.presentation.profile.screens.post.adapters.separators

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.DividerProfileBinding
import com.example.appsample.framework.presentation.profile.model.post.Divider
import com.example.appsample.framework.presentation.profile.model.post.PostElement
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DividerAdapterDelegate :
    AbsListItemAdapterDelegate<Divider, PostElement, DividerAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val dividerProfileBinding =
            DividerProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(dividerProfileBinding)
    }

    override fun isForViewType(
        item: PostElement,
        items: MutableList<PostElement>,
        position: Int
    ): Boolean {
        return item is Divider
    }

    override fun onBindViewHolder(item: Divider, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind()
    }

    class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: DividerProfileBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {}
    }
}
