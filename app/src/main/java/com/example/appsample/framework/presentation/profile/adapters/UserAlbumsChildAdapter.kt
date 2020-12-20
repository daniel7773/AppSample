package com.example.appsample.framework.presentation.profile.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.R
import com.example.appsample.databinding.BlockUserAlbumBinding
import com.example.appsample.framework.presentation.common.model.AlbumModel
import com.example.appsample.framework.presentation.common.model.PhotoModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserAlbumsChildAdapter :
    ListAdapter<AlbumModel?, UserAlbumsChildAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<AlbumModel?>() {
        override fun areItemsTheSame(
            oldItem: AlbumModel,
            albumModel: AlbumModel
        ): Boolean {
            return oldItem == albumModel
        }

        override fun areContentsTheSame(oldItem: AlbumModel, albumModel: AlbumModel): Boolean {
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val blockUserAlbumBinding =
            BlockUserAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(blockUserAlbumBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockUserAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AlbumModel?) {
            binding.album = item
            val isItemNull = item == null
            if (isItemNull) {
                binding.albumTitle.visibility = View.GONE
            }
            if (item?.firstPhoto != null) {
                loadAlbumPicture(item.firstPhoto!!)
            }
        }

        private fun loadAlbumPicture(photo: PhotoModel) =
            Picasso.get()
                .load(photo.thumbnailUrl)
                .placeholder(R.drawable.ic_square_placeholder)
                .error(R.drawable.ic_error)
                .into(binding.postIcon)
    }
}