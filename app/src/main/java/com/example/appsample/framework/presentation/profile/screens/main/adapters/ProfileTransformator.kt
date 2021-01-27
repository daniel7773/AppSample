package com.example.appsample.framework.presentation.profile.screens.main.adapters

import android.util.Log
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.base.presentation.delegateadapter.separators.Divider
import com.example.appsample.framework.base.presentation.delegateadapter.separators.EmptySpace
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.common.model.UserModel
import com.example.appsample.framework.presentation.profile.adapterelements.AlbumsBlockElement
import com.example.appsample.framework.presentation.profile.adapterelements.UserActionsElement
import com.example.appsample.framework.presentation.profile.adapterelements.UserDetailsElement
import com.example.appsample.framework.presentation.profile.adapterelements.UserInfoElement
import com.example.appsample.framework.presentation.profile.adapterelements.UserPostsElement
import com.example.appsample.framework.presentation.profile.model.AlbumModel
import com.example.appsample.framework.presentation.profile.model.PostModel

object ProfileTransformator {

    private val TAG = "ProfileTransformator"


    fun transform(
        user: State<UserModel?>,
        albums: State<List<AlbumModel>?>,
        posts: State<List<PostModel>?>
    ) = emptySequence<AdapterElement>()
        .plus(getUser(user))
        .plus(getAlbumList(albums))
        .plus(getPostList(posts))

    fun getUser(userState: State<UserModel?>) = when (userState) {
        is State.Success -> {
            if (userState.data == null) emptySequence<AdapterElement>()
            Log.d(TAG, "added success to userAdapterItem")
            sequenceOf<AdapterElement>(UserInfoElement(userState.data!!))
                .plus(UserActionsElement(userState.data!!))
                .plus(Divider("divider_after_user_actions"))
                .plus(UserDetailsElement(userState.data!!))
                .plus(Divider("user_details_divider"))
        }
        else -> {
            emptySequence()
        }
    }

    fun getPostList(postListState: State<List<PostModel>?>) = when (postListState) {
        is State.Success -> {
            if (postListState.data.isNullOrEmpty()) emptySequence<AdapterElement>()
            Log.d(TAG, "added success to postListItem with size: ${postListState.data!!.size}")
            sequenceOf(EmptySpace("user_posts_empty_space"))
                .plus(postListState.data!!.flatMap {
                    sequenceOf(UserPostsElement(it))
                        .plus(EmptySpace("album_element_empty_space_${it.id}"))
                })
        }
        else -> {
            emptySequence()
        }
    }

    fun getAlbumList(albumListState: State<List<AlbumModel>?>) = when (albumListState) {
        is State.Success -> {
            Log.d(TAG, "added success to albumListItem")
            if (albumListState.data.isNullOrEmpty()) emptySequence<AdapterElement>()
            emptySequence<AdapterElement>()
                .plus(EmptySpace("album_block_empty_space"))
                .plus(AlbumsBlockElement(albumListState.data!!))
        }
        else -> {
            emptySequence()
        }
    }
}