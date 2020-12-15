package com.example.appsample.framework.presentation.common.model

data class PhotoModel(
    var albumId: Int? = 1,
    var id: Int? = 1,
    var title: String? = null,
    var url: String? = null,
    var thumbnailUrl: String? = null
)