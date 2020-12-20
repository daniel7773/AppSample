package com.example.appsample.framework.presentation.profile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.BlockUserAlbumsBinding
import com.example.appsample.framework.presentation.common.model.AlbumModel
import com.example.appsample.framework.presentation.profile.models.AlbumsBlockElement
import com.example.appsample.framework.presentation.profile.models.ProfileElement
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserAlbumsAdapterDelegate :
    AbsListItemAdapterDelegate<AlbumsBlockElement, ProfileElement, UserAlbumsAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val blockUserAlbumsBinding =
            BlockUserAlbumsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(blockUserAlbumsBinding)
    }

    override fun isForViewType(
        item: ProfileElement,
        items: MutableList<ProfileElement>,
        position: Int
    ): Boolean {
        return item is AlbumsBlockElement
    }

    override fun onBindViewHolder(
        item: AlbumsBlockElement,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item.postList)
    }

    class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockUserAlbumsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(items: List<AlbumModel>) {
            binding.albumsRecyclerView.adapter = UserAlbumsChildAdapter()
            binding.albumList = items
            if (items.isNotEmpty()) {
                binding.albumsSize.text = items.size.toString()
            }
        }
    }
}