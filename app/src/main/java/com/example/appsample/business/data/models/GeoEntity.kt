package com.example.appsample.business.data.models

import com.google.gson.annotations.SerializedName

data class GeoEntity(
    @SerializedName("lat")
    var lat: Double? = null,
    @SerializedName("lng")
    var lng: Double? = null
)