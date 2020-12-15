package com.example.appsample.framework.presentation.common.model

data class AlbumModel(
    var userId: Int? = 0,
    var id: Int? = 0,
    var title: String? = null,
    var firstPhoto: PhotoModel? = null
)