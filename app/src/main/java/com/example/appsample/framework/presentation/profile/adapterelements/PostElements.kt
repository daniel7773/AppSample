package com.example.appsample.framework.presentation.profile.adapterelements

import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.presentation.profile.model.CommentModel
import com.example.appsample.framework.presentation.profile.model.PostModel

data class PostSourceElement(val postModel: PostModel) : AdapterElement("post_source_block")

data class PostBodyElement(val postModel: PostModel) : AdapterElement("post_body_block")

data class PostCommentsHeader(val commentListSize: Int) :
    AdapterElement("post_comments_header_block")

data class PostCommentElement(val comment: CommentModel) :
    AdapterElement("${comment.id} ${comment.name} ${comment.postId}")





