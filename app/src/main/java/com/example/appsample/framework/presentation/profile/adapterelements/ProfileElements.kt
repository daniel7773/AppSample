package com.example.appsample.framework.presentation.profile.adapterelements

import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.presentation.common.model.UserModel
import com.example.appsample.framework.presentation.profile.model.AlbumModel
import com.example.appsample.framework.presentation.profile.model.PostModel

data class UserInfoElement(val user: UserModel) : AdapterElement("user_main_info_block")

data class UserActionsElement(val user: UserModel) : AdapterElement("user_action_block")

data class UserDetailsElement(val user: UserModel) : AdapterElement("user_details_block")

data class UserPostsElement(val postModel: PostModel) :
    AdapterElement("${postModel.id}_${postModel.title}_${postModel.userId}")

data class AlbumsBlockElement(val albumList: List<AlbumModel>) : AdapterElement("user_albums_block")




