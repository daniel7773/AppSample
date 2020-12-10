package com.example.appsample.business.data.models

import com.google.gson.annotations.SerializedName

data class PostEntity(
    @SerializedName("userId")
    var userId: Int? = 0,
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("body")
    var body: String? = null
)