package com.example.appsample.framework.presentation.extensions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.CompositeDelegateAdapter
import com.example.appsample.framework.presentation.profile.model.AlbumModel
import com.example.appsample.framework.presentation.profile.model.PhotoModel
import com.example.appsample.framework.presentation.profile.screens.album.AlbumPhotoListAdapter
import com.example.appsample.framework.presentation.profile.screens.main.adapters.UserAlbumsChildAdapter
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
    items: List<AlbumModel>?
) {
    val adapter = (recyclerView.adapter as? UserAlbumsChildAdapter)
        ?: throw Exception("Adapter should not be NULL while calling app:albumItems in BindingExtensions")
    adapter.submitList(items.orEmpty())
}

// for album adapter in AlbumFragment
@FlowPreview
@ExperimentalCoroutinesApi
@BindingAdapter("app:albumPhotos")
fun setAlbumFragmentRecyclerView(
    recyclerView: RecyclerView,
    items: List<PhotoModel>?
) {
    val adapter = (recyclerView.adapter as? AlbumPhotoListAdapter)
        ?: throw Exception("Adapter should not be NULL while calling app:albumPhotos in BindingExtensions")
    adapter.submitList(items.orEmpty())
}