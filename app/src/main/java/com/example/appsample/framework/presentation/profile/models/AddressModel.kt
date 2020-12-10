package com.example.appsample.framework.presentation.profile.models

data class AddressModel(
    var street: String? = null,
    var suite: String? = null,
    var city: String? = null,
    var zipcode: String? = null,
    var geoModel: GeoModel? = null
)