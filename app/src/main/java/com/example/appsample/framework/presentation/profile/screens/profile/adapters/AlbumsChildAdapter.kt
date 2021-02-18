package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.R
import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.databinding.BlockAlbumBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AlbumsChildAdapter(private val onAlbumClick: ((ImageView, Album, Int) -> Unit)?) :
    ListAdapter<Album?, AlbumsChildAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Album?>() {
        override fun areItemsTheSame(
            oldItem: Album,
            album: Album
        ): Boolean {
            return oldItem == album
        }

        override fun areContentsTheSame(oldItem: Album, album: Album): Boolean {
            return oldItem.id == album.id && oldItem.userId == album.userId
                    && oldItem.title == album.title && oldItem.firstPhoto == album.firstPhoto
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val blockAlbumBinding =
            BlockAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(blockAlbumBinding, position, onAlbumClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockAlbumBinding,
        private val itemPosition: Int,
        private val onAlbumClick: ((ImageView, Album, Int) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Album?) {
            val isItemNull = item == null
            if (isItemNull) {
                binding.albumTitle.visibility = View.GONE
                binding.albumSize.visibility = View.GONE
                return
            } else {
                binding.albumTitle.visibility = View.VISIBLE
                binding.albumTitle.text = item?.title
                binding.albumSize.visibility = View.VISIBLE
                val photosNumber: String = String.format(
                    binding.postIcon.context.getString(R.string.photos_number),
                    50
                )
                binding.albumSize.text = photosNumber
            }

            if (item?.firstPhoto != null) {
                loadAlbumPicture(item.firstPhoto!!)
            }

            binding.itemLayout.setOnClickListener {
                onAlbumClick?.invoke(binding.postIcon, item!!, itemPosition)
            }
        }

        private fun loadAlbumPicture(photo: Photo) =
            Picasso.get()
                .load(photo.thumbnailUrl)
                .placeholder(R.drawable.shadow)
                .error(R.drawable.ic_error)
                .into(binding.postIcon)
    }
}