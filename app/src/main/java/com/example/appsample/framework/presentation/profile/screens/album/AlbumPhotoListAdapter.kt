package com.example.appsample.framework.presentation.profile.screens.album

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.R
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.databinding.ItemImageBinding
import com.squareup.picasso.Picasso


class AlbumPhotoListAdapter(
    private val onItemClick: ((ImageView, Photo, Int) -> Unit)? = null,
    stateRestorationPolicy: StateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY,
    private val recycleChildrenOnDetach: Boolean = false
) : ListAdapter<Photo, AlbumPhotoListAdapter.ImageViewHolder>(DiffCallback()) {

    init {
        this.stateRestorationPolicy = stateRestorationPolicy
    }

    internal class DiffCallback : DiffUtil.ItemCallback<Photo>() {

        override fun areItemsTheSame(oldItem: Photo, photo: Photo): Boolean {
            return oldItem == photo
        }

        override fun areContentsTheSame(oldItem: Photo, photo: Photo): Boolean {
            return oldItem.id == photo.id && oldItem.albumId == photo.albumId
                    && oldItem.title == photo.title && oldItem.url == photo.url
                    && oldItem.thumbnailUrl == photo.thumbnailUrl
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recycleChildrenOnDetach) {
            val layoutManager = recyclerView.layoutManager
            (layoutManager as? LinearLayoutManager)?.recycleChildrenOnDetach = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//        println("🔥 SingleViewBinderAdapter onCreateViewHolder() viewType: $viewType")
        val itemImageBinding =
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(itemImageBinding, onItemClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        println("🤔 SingleViewBinderAdapter onBindViewHolder() position: $position, holder: $holder")
        holder.bind(getItem(position))
    }

    inner class ImageViewHolder(
        private val binding: ItemImageBinding,
        private val onItemClick: ((ImageView, Photo, Int) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Photo) {

            val imageView = binding.ivPhoto

            setImageUrl(model.url!!)

            binding.ivPhoto.transitionName = binding.ivPhoto.resources.getString(R.string.photo_transition_name) + model.url

            binding.root.setOnClickListener {
                onItemClick?.invoke(imageView, model, bindingAdapterPosition)
            }
        }

        private fun setImageUrl(url: String) =
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_square_placeholder)
                .error(R.drawable.ic_error)
                .into(binding.ivPhoto)
    }
}
