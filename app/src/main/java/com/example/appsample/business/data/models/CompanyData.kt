package com.example.appsample.business.data.models

import com.google.gson.annotations.SerializedName

data class CompanyData(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("catchPhrase")
    var catchPhrase: String? = null,
    @SerializedName("bs")
    var bs: String? = null
)