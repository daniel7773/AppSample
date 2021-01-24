package com.example.appsample.framework.presentation.profile.model

data class AddressModel(
    var street: String? = null,
    var suite: String? = null,
    var city: String? = null,
    var zipcode: String? = null,
    var geoModel: GeoModel? = null
)