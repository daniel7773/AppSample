package com.example.appsample.business.data.models

import com.google.gson.annotations.SerializedName

data class AddressData(
    @SerializedName("street")
    var street: String? = null,
    @SerializedName("suite")
    var suite: String? = null,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("zipcode")
    var zipcode: String? = null,
    @SerializedName("geo")
    var geo: GeoData? = null
)