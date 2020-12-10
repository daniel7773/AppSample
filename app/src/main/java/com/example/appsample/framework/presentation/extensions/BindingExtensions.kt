package com.example.appsample.framework.presentation.extensions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appsample.framework.presentation.profile.ProfileAdapter
import com.example.appsample.framework.presentation.profile.adapters.UserAlbumsChildAdapter
import com.example.appsample.framework.presentation.profile.adapters.UserPostAdapterDelegate
import com.example.appsample.framework.presentation.profile.models.AlbumModel
import com.example.appsample.framework.presentation.profile.models.PostModel
import com.example.appsample.framework.presentation.profile.models.ProfileElement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@BindingAdapter("app:items")
fun setRecyclerViewItems(
    recyclerView: RecyclerView,
    items: Sequence<ProfileElement>?
) {
    var adapter = (recyclerView.adapter as? ProfileAdapter)
    if (adapter == null) {
        adapter = ProfileAdapter()
        recyclerView.adapter = adapter
    }
    println("sequence size: ${items?.toList()?.size}")
    adapter.updateData(items.orEmpty())
}


// for child adapter
@FlowPreview
@ExperimentalCoroutinesApi
@BindingAdapter("app:items")
fun setRecyclerViewAlbumItems(
    recyclerView: RecyclerView,
    items: List<AlbumModel>?
) {
    var adapter = (recyclerView.adapter as? UserAlbumsChildAdapter)
    if (adapter == null) {
        adapter = UserAlbumsChildAdapter()
        recyclerView.adapter = adapter
    }
    println("sequence size: ${items?.toList()?.size}")
    adapter.updateData(items.orEmpty())
}