package com.example.appsample.framework.presentation.profile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.databinding.BlockUserAlbumBinding
import com.example.appsample.framework.presentation.profile.models.AlbumModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserAlbumsChildAdapter : ListAdapter<AlbumModel?, UserAlbumsChildAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<AlbumModel?>() {
        override fun areItemsTheSame(
            oldItem: AlbumModel,
            albumModel: AlbumModel
        ): Boolean {
            return oldItem == albumModel
        }

        override fun areContentsTheSame(oldItem: AlbumModel, albumModel: AlbumModel): Boolean {
            return oldItem.id == albumModel.id && oldItem.title == albumModel.title &&
                    oldItem.userId == albumModel.userId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val BlockUserAlbumBinding =
            BlockUserAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(BlockUserAlbumBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun updateData(items: List<AlbumModel>?) {
        this.submitList(items?.toList())
    }

    inner class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockUserAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AlbumModel?) {
            binding.album = item
            val isItemNull = item == null
            if (isItemNull) {
                binding.albumTitle.visibility = View.GONE
            } else {
                binding.skeletonTextLine1.visibility = View.GONE
                binding.skeletonTextLine2.visibility = View.GONE
            }
        }
    }
}