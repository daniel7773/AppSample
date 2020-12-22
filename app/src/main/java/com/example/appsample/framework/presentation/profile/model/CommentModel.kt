package com.example.appsample.framework.presentation.profile.model

data class CommentModel(
    var postId: Int? = 0,
    var id: Int? = 0,
    var name: String? = null,
    var email: String? = null,
    var body: String? = null
)