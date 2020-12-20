package com.example.appsample.framework.presentation.profile.screens.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.R
import com.example.appsample.databinding.BlockUserAlbumBinding
import com.example.appsample.framework.presentation.profile.models.AlbumModel
import com.example.appsample.framework.presentation.profile.models.PhotoModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserAlbumsChildAdapter(private val onAlbumClick: ((ImageView, AlbumModel, Int) -> Unit)?) :
    ListAdapter<AlbumModel?, UserAlbumsChildAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<AlbumModel?>() {
        override fun areItemsTheSame(
            oldItem: AlbumModel,
            albumModel: AlbumModel
        ): Boolean {
            return oldItem == albumModel
        }

        override fun areContentsTheSame(oldItem: AlbumModel, albumModel: AlbumModel): Boolean {
            return oldItem.id == albumModel.id && oldItem.userId == albumModel.userId
                    && oldItem.title == albumModel.title && oldItem.firstPhoto == albumModel.firstPhoto
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val blockUserAlbumBinding =
            BlockUserAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(blockUserAlbumBinding, position, onAlbumClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder @ExperimentalCoroutinesApi constructor(
        private val binding: BlockUserAlbumBinding,
        private val itemPosition: Int,
        private val onAlbumClick: ((ImageView, AlbumModel, Int) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AlbumModel?) {
            binding.album = item
            val isItemNull = item == null
            if (isItemNull) {
                binding.albumTitle.visibility = View.GONE
                return
            }
            val photosNumber: String = String.format(
                binding.postIcon.context.getString(R.string.photos_number),
                50
            )
            binding.albumSize.text = photosNumber

            if (item?.firstPhoto != null) {
                loadAlbumPicture(item.firstPhoto!!)
            }

            binding.itemLayout.setOnClickListener {
                onAlbumClick?.invoke(binding.postIcon, item!!, itemPosition)
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