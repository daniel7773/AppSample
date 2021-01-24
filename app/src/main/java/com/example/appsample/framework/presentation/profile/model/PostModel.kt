package com.example.appsample.framework.presentation.profile.model

data class PostModel(
    var userId: Int? = 0,
    var id: Int? = 0,
    var commentsSize: Int? = 0,
    var title: String? = null,
    var body: String? = null
)