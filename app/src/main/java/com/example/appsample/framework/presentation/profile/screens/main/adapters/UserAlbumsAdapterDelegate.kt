package com.example.appsample.framework.presentation.profile.screens.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.BlockUserAlbumsBinding
import com.example.appsample.framework.presentation.profile.model.AlbumModel
import com.example.appsample.framework.presentation.profile.model.AlbumsBlockElement
import com.example.appsample.framework.presentation.profile.model.ProfileElement
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserAlbumsAdapterDelegate(
    private val onAlbumClick: ((ImageView, AlbumModel, Int) -> Unit)
) : AbsListItemAdapterDelegate<AlbumsBlockElement, ProfileElement, UserAlbumsAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val blockUserAlbumsBinding =
            BlockUserAlbumsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(blockUserAlbumsBinding, onAlbumClick)
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
        private val binding: BlockUserAlbumsBinding,
        private val onAlbumClick: ((ImageView, AlbumModel, Int) -> Unit)
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(items: List<AlbumModel>) {
            binding.albumsRecyclerView.adapter = UserAlbumsChildAdapter(onAlbumClick)
            binding.albumList = items
            if (items.isNotEmpty()) {
                binding.albumsSize.text = items.size.toString()
            }
        }
    }
}