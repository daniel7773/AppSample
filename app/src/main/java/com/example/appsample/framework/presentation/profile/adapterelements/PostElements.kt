package com.example.appsample.framework.presentation.profile.adapterelements

import com.example.appsample.business.domain.model.Comment
import com.example.appsample.business.domain.model.Post
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement

data class PostSourceElement(val post: Post) : AdapterElement("post_source_block")

data class PostBodyElement(val post: Post) : AdapterElement("post_body_block")

data class PostCommentsHeader(val commentListSize: Int) :
    AdapterElement("post_comments_header_block")

data class PostCommentElement(val comment: Comment) :
    AdapterElement("${comment.id} ${comment.name} ${comment.postId}")





