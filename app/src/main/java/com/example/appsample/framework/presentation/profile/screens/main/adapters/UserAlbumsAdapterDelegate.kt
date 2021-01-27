package com.example.appsample.framework.presentation.profile.screens.main.adapters

import android.util.Log
import android.widget.ImageView
import com.example.appsample.databinding.BlockUserAlbumsBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.AlbumsBlockElement
import com.example.appsample.framework.presentation.profile.model.AlbumModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserAlbumsAdapterDelegate(
    private val onAlbumClick: ((ImageView, AlbumModel, Int) -> Unit)
) : ViewBindingDelegateAdapter<AlbumsBlockElement, BlockUserAlbumsBinding>(
    BlockUserAlbumsBinding::inflate
) {

    override fun BlockUserAlbumsBinding.onBind(item: AlbumsBlockElement) {
        albumList = item.albumList
        albumsRecyclerView.adapter = UserAlbumsChildAdapter(onAlbumClick)
        if (item.albumList.isNotEmpty()) {
            albumsSize.text = item.albumList.size.toString()
        }
    }

    override fun isForViewType(item: Any): Boolean = item is AlbumsBlockElement

    override fun AlbumsBlockElement.getItemId(): Any = id

    override fun BlockUserAlbumsBinding.onRecycled() {
        albumsSize.text = ""
        albumsRecyclerView.adapter = null
    }

    override fun BlockUserAlbumsBinding.onAttachedToWindow() {
        Log.d(UserAlbumsAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockUserAlbumsBinding.onDetachedFromWindow() {
        Log.d(UserAlbumsAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}