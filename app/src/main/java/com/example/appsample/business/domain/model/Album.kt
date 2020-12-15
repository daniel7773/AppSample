package com.example.appsample.business.domain.model

data class Album(
    var userId: Int? = 0,
    var id: Int? = 0,
    var title: String? = null,
    var firstPhoto: Photo? = null
)