package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.appsample.business.domain.model.Album
import com.example.appsample.databinding.BlockAlbumsBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.example.appsample.framework.presentation.profile.adapterelements.AlbumsBlockElement
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AlbumsAdapterDelegate(
    private val onAlbumClick: ((ImageView, Album, Int) -> Unit)
) : ViewBindingDelegateAdapter<AlbumsBlockElement, BlockAlbumsBinding>(
    BlockAlbumsBinding::inflate
) {

    override fun BlockAlbumsBinding.onBind(item: AlbumsBlockElement) {
        if (item.albumList != null) {
            var bindingList: List<Album?> = item.albumList
            if (item.albumList.isNotEmpty()) {
                albumsSize.text = item.albumList.size.toString()
                addPhotoTextView.visibility = View.GONE
            } else {
                bindingList = listOf<Album?>(null, null, null)
                addPhotoTextView.visibility = View.VISIBLE
            }
            albumList = bindingList
            albumsRecyclerView.adapter = AlbumsChildAdapter(onAlbumClick)
        } else {
            albumsRecyclerView.visibility = View.GONE
            addPhotoTextView.visibility = View.VISIBLE
        }
    }

    override fun isForViewType(item: Any): Boolean = item is AlbumsBlockElement

    override fun AlbumsBlockElement.getItemId(): Any = id

    override fun BlockAlbumsBinding.onRecycled() {
        albumsSize.text = ""
        albumsRecyclerView.adapter = null
        addPhotoTextView.visibility = View.GONE
    }

    override fun BlockAlbumsBinding.onAttachedToWindow() {
        Log.d(AlbumsAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun BlockAlbumsBinding.onDetachedFromWindow() {
        Log.d(AlbumsAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}