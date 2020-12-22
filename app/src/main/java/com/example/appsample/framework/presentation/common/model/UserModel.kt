package com.example.appsample.framework.presentation.common.model

import com.example.appsample.framework.presentation.profile.model.AddressModel
import com.example.appsample.framework.presentation.profile.model.CompanyModel

data class UserModel(
    var id: Int? = null,
    var username: String? = null,
    var email: String? = null,
    var address: AddressModel? = null,
    var phone: String? = null,
    var website: String? = null,
    var company: CompanyModel? = null
) {
    var companyName: String = company?.name ?: "Неизвестная комания"

    var streetAndSuit: String = "${address?.street} ${address?.suite}"

    var latAndLang: String = "lat ${address?.geoModel?.lat}, lng ${address?.geoModel?.lng}"

}