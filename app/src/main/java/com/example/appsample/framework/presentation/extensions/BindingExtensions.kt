package com.example.appsample.framework.presentation.extensions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.model.Photo
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.CompositeDelegateAdapter
import com.example.appsample.framework.presentation.profile.screens.album.AlbumPhotoListAdapter
import com.example.appsample.framework.presentation.profile.screens.profile.adapters.AlbumsChildAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@BindingAdapter("app:items")
fun setRecyclerViewItems(
    recyclerView: RecyclerView,
    items: Sequence<AdapterElement>?
) {
    val adapter = (recyclerView.adapter as? CompositeDelegateAdapter)
        ?: throw Exception("Adapter should not be NULL while calling app:items in BindingExtensions")
    adapter.swapData(items?.toList().orEmpty())
}


// for child adapter
@FlowPreview
@ExperimentalCoroutinesApi
@BindingAdapter("app:albumItems")
fun setRecyclerViewAlbums(
    recyclerView: RecyclerView,
    items: List<Album>?
) {
    val adapter = (recyclerView.adapter as? AlbumsChildAdapter)
        ?: throw Exception("Adapter should not be NULL while calling app:albumItems in BindingExtensions")
    adapter.submitList(items.orEmpty())
}

// for album adapter in AlbumFragment
@FlowPreview
@ExperimentalCoroutinesApi
@BindingAdapter("app:albumPhotos")
fun setAlbumFragmentRecyclerView(
    recyclerView: RecyclerView,
    items: List<Photo>?
) {
    val adapter = (recyclerView.adapter as? AlbumPhotoListAdapter)
        ?: throw Exception("Adapter should not be NULL while calling app:albumPhotos in BindingExtensions")
    adapter.submitList(items.orEmpty())
}