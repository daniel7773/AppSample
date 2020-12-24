package com.example.appsample.business.domain.model

data class Post(
    var userId: Int? = 0,
    var id: Int? = 0,
    var commentsSize: Int? = 0,
    var title: String? = null,
    var body: String? = null
)