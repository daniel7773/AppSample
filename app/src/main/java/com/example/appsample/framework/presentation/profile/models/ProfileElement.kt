package com.example.appsample.framework.presentation.profile.models

import com.example.appsample.business.domain.model.Album
import com.example.appsample.framework.presentation.common.model.UserModel

sealed class ProfileElement(open val id: String)

data class UserInfoElement(val user: UserModel) : ProfileElement("user_main_info_block")

data class UserActionsElement(val user: UserModel) : ProfileElement("user_action_block")

data class UserDetailsElement(val user: UserModel) : ProfileElement("user_details_block")

data class UserPostsElement(val post: PostModel) : ProfileElement("${post.id}_${post.title}_${post.userId}")

data class AlbumsBlockElement(val postList: List<AlbumModel>) : ProfileElement("user_albums_block")

// id дивайдерам нужен чтобы диффер с ними правильно работал
class Divider(id: String?) : ProfileElement(id ?: "divider") {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Divider
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

// id пробелам нужен чтобы диффер с ними правильно работал
class EmptySpace(id: String) : ProfileElement(id) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EmptySpace
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}





