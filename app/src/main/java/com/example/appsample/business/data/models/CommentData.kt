package com.example.appsample.business.data.models

import com.google.gson.annotations.SerializedName

data class CommentData(
    @SerializedName("postId")
    var postId: Int? = 0,
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("body")
    var body: String? = null
)