package com.example.appsample.framework.presentation.profile.screens.profile.adapters

import android.util.Log
import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.model.User
import com.example.appsample.business.domain.state.DataState
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.base.presentation.delegateadapter.separators.Divider
import com.example.appsample.framework.base.presentation.delegateadapter.separators.EmptySpace
import com.example.appsample.framework.presentation.profile.adapterelements.AlbumsBlockElement
import com.example.appsample.framework.presentation.profile.adapterelements.AlbumsErrorElement
import com.example.appsample.framework.presentation.profile.adapterelements.AlbumsLoadingElement
import com.example.appsample.framework.presentation.profile.adapterelements.PostElement
import com.example.appsample.framework.presentation.profile.adapterelements.PostLoadingElement
import com.example.appsample.framework.presentation.profile.adapterelements.PostsEmptyElement
import com.example.appsample.framework.presentation.profile.adapterelements.UserActionsElement
import com.example.appsample.framework.presentation.profile.adapterelements.UserDetailsElement
import com.example.appsample.framework.presentation.profile.adapterelements.UserErrorElement
import com.example.appsample.framework.presentation.profile.adapterelements.UserInfoElement
import com.example.appsample.framework.presentation.profile.adapterelements.UserLoadingElement

object ProfileTransformator {

    private val TAG = "ProfileTransformator"


    fun transform(
        user: DataState<User?>,
        albums: DataState<List<Album>?>,
        posts: DataState<List<Post>?>
    ) = emptySequence<AdapterElement>()
        .plus(getUser(user))
        .plus(getAlbumList(albums))
        .plus(getPostList(posts))

    fun getUser(userState: DataState<User?>) = when (userState) {
        is DataState.Success -> {
            if (userState.data == null) getNullUserSequence()
            Log.d(TAG, "userState.data == null: ${userState.data == null}")
            Log.d(TAG, "added success to userAdapterItem")
            sequenceOf<AdapterElement>(UserInfoElement(userState.data!!))
                .plus(UserActionsElement(userState.data!!))
                .plus(Divider("divider_after_user_actions"))
                .plus(UserDetailsElement(userState.data!!))
                .plus(Divider("user_details_divider"))
        }
        is DataState.Loading -> {
            if (userState.data == null) {
                sequenceOf<AdapterElement>(UserLoadingElement())
            } else {
                sequenceOf<AdapterElement>(UserInfoElement(userState.data!!))
                    .plus(UserActionsElement(userState.data!!))
                    .plus(Divider("divider_after_user_actions"))
                    .plus(UserDetailsElement(userState.data!!))
                    .plus(Divider("user_details_divider"))
            }
        }
        is DataState.Error -> sequenceOf(UserErrorElement())
        is DataState.Idle -> sequenceOf<AdapterElement>(UserLoadingElement())
    }

    private fun getNullUserSequence(): Sequence<AdapterElement> {
        val userModel = User()
        return sequenceOf<AdapterElement>(UserInfoElement(userModel))
            .plus(UserActionsElement(userModel))
            .plus(Divider("divider_after_user_actions"))
            .plus(UserDetailsElement(userModel))
            .plus(Divider("user_details_divider"))
    }

    fun getPostList(postListState: DataState<List<Post>?>) = when (postListState) {
        is DataState.Success -> {
            Log.d(TAG, "added success to postListItem with size: ${postListState.data!!.size}")
            if (postListState.data.isNullOrEmpty()) sequenceOf(PostsEmptyElement())
            sequenceOf(EmptySpace("user_posts_empty_space"))
                .plus(postListState.data!!.flatMap {
                    sequenceOf(PostElement(it))
                        .plus(EmptySpace("album_element_empty_space_${it.id}"))
                })
        }
        is DataState.Loading -> {
            if (postListState.data == null) {
                sequenceOf<AdapterElement>(PostLoadingElement())
            } else {
                sequenceOf(EmptySpace("user_posts_empty_space"))
                    .plus(postListState.data!!.flatMap {
                        sequenceOf(PostElement(it))
                            .plus(EmptySpace("album_element_empty_space_${it.id}"))
                    })
            }
        }
        is DataState.Error -> emptySequence()
        is DataState.Idle -> sequenceOf<AdapterElement>(PostLoadingElement())

    }

    private fun getAlbumList(listState: DataState<List<Album>?>) = when (listState) {
        is DataState.Success -> {
            Log.d(TAG, "added success to albumListItem : $listState")
            emptySequence<AdapterElement>()
                .plus(EmptySpace("album_block_empty_space"))
                .plus(AlbumsBlockElement(listState.data))
        }
        is DataState.Loading -> {
            if (listState.data == null) {
                sequenceOf(AlbumsLoadingElement())
            } else {
                emptySequence<AdapterElement>()
                    .plus(EmptySpace("album_block_empty_space"))
                    .plus(AlbumsBlockElement(listState.data!!))
            }
        }
        is DataState.Error -> sequenceOf(AlbumsErrorElement())
        is DataState.Idle -> sequenceOf(AlbumsLoadingElement())
    }

}