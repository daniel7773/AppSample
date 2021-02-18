package com.example.appsample.framework.presentation.profile.adapterelements

import com.example.appsample.business.domain.model.Album
import com.example.appsample.business.domain.model.Post
import com.example.appsample.business.domain.model.User
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement


data class UserInfoElement(val user: User) : AdapterElement("user_main_info_block")

class UserLoadingElement : AdapterElement("user_loading_block") {

    override fun equals(other: Any?): Boolean {
        if (other !is UserLoadingElement) return false
        return other.id == id
    }

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + id.hashCode()
        return result
    }
}

class UserErrorElement : AdapterElement("user_error_block") {

    override fun equals(other: Any?): Boolean {
        if (other !is UserErrorElement) return false
        return other.id == id
    }

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + id.hashCode()
        return result
    }
}

data class UserActionsElement(val user: User) : AdapterElement("user_action_block")

data class UserDetailsElement(val user: User) : AdapterElement("user_details_block")

data class PostElement(val post: Post) :
    AdapterElement("${post.id}_${post.title}_${post.userId}")

class PostLoadingElement : AdapterElement("user_post_loading_block") {

    override fun equals(other: Any?): Boolean {
        if (other !is PostLoadingElement) return false
        return other.id == id
    }

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + id.hashCode()
        return result
    }
}

class PostsEmptyElement : AdapterElement("user_posts_empty_block") {

    override fun equals(other: Any?): Boolean {
        if (other !is PostsEmptyElement) return false
        return other.id == id
    }

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + id.hashCode()
        return result
    }
}

data class AlbumsBlockElement(val albumList: List<Album>?) : AdapterElement("user_albums_block")

class AlbumsLoadingElement : AdapterElement("user_albums_loading_block") {

    override fun equals(other: Any?): Boolean {
        if (other !is AlbumsLoadingElement) return false
        return other.id == id
    }

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + id.hashCode()
        return result
    }
}

class AlbumsErrorElement : AdapterElement("user_albums_error_block") {

    override fun equals(other: Any?): Boolean {
        if (other !is AlbumsErrorElement) return false
        return other.id == id
    }

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + id.hashCode()
        return result
    }
}




