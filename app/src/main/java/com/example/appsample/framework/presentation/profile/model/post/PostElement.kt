package com.example.appsample.framework.presentation.profile.model.post

import com.example.appsample.framework.presentation.profile.model.CommentModel
import com.example.appsample.framework.presentation.profile.model.PostModel

sealed class PostElement(open val id: String)

data class PostSourceElement(val postModel: PostModel) : PostElement("post_source_block")

data class PostBodyElement(val postModel: PostModel) : PostElement("post_body_block")

data class PostCommentsHeader(val commentListSize: Int) : PostElement("post_comments_header_block")

data class PostCommentElement(val comment: CommentModel) :
    PostElement("${comment.id} ${comment.name} ${comment.postId}")

// id дивайдерам нужен чтобы диффер с ними правильно работал
class Divider(id: String?) : PostElement(id ?: "divider") {

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
class EmptySpace(id: String) : PostElement(id) {

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





