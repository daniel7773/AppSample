package com.example.appsample.business.domain.model

data class Photo(
    var albumId: Int? = 1,
    var id: Int? = 1,
    var title: String? = null,
    var url: String? = null,
    var thumbnailUrl: String? = null
)