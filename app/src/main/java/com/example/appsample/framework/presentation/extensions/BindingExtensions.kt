package com.example.appsample.framework.presentation.extensions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.framework.presentation.profile.model.AlbumModel
import com.example.appsample.framework.presentation.profile.model.PhotoModel
import com.example.appsample.framework.presentation.profile.model.ProfileElement
import com.example.appsample.framework.presentation.profile.model.post.PostElement
import com.example.appsample.framework.presentation.profile.screens.album.AlbumPhotoListAdapter
import com.example.appsample.framework.presentation.profile.screens.main.ProfileAdapter
import com.example.appsample.framework.presentation.profile.screens.main.adapters.UserAlbumsChildAdapter
import com.example.appsample.framework.presentation.profile.screens.post.PostAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@BindingAdapter("app:items")
fun setRecyclerViewItems(
    recyclerView: RecyclerView,
    items: Sequence<ProfileElement>?
) {
    val adapter = (recyclerView.adapter as? ProfileAdapter)
        ?: throw Exception("Adapter should not be NULL while calling app:items in BindingExtensions")
    adapter.updateData(items.orEmpty())
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

// for post adapter in PostFragment
@FlowPreview
@ExperimentalCoroutinesApi
@BindingAdapter("app:postItems")
fun setPostRecyclerViewItems(
    recyclerView: RecyclerView,
    items: Sequence<PostElement>?
) {
    val adapter = (recyclerView.adapter as? PostAdapter)
        ?: throw Exception("Adapter should not be NULL while calling app:items in BindingExtensions")
    adapter.updateData(items.orEmpty())
}