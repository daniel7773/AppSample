package com.example.appsample.business.data.models

import com.google.gson.annotations.SerializedName

data class UserEntity(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("username")
    var username: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("address")
    var address: AddressEntity? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("website")
    var website: String? = null,
    @SerializedName("company")
    var company: CompanyEntity? = null
)

