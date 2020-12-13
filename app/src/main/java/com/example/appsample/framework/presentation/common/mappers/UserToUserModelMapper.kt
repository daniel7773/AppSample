package com.example.appsample.framework.presentation.common.mappers

import com.example.appsample.business.domain.model.Address
import com.example.appsample.business.domain.model.Company
import com.example.appsample.business.domain.model.Geo
import com.example.appsample.business.domain.model.User
import com.example.appsample.framework.presentation.common.model.UserModel
import com.example.appsample.framework.presentation.profile.models.AddressModel
import com.example.appsample.framework.presentation.profile.models.CompanyModel
import com.example.appsample.framework.presentation.profile.models.GeoModel

object UserToUserModelMapper {

    fun map(user: User) = UserModel(
        id = user.id,
        username = user.username,
        email = user.email,
        website = user.website,
        address = mapAddressToAddressModel(user.address),
        phone = user.phone,
        company = mapCompanyToCompanyModel(user.company)
    )

    private fun mapAddressToAddressModel(address: Address?) = address?.run {
        AddressModel(
            street = street,
            suite = suite,
            city = city,
            zipcode = zipcode,
            geoModel = mapGeoToGeoModel(geo)
        )
    }

    private fun mapCompanyToCompanyModel(company: Company?) = company?.run {
        CompanyModel(
            name = name,
            catchPhrase = catchPhrase,
            bs = bs
        )
    }

    private fun mapGeoToGeoModel(geo: Geo?) = geo?.run {
        GeoModel(
            lat = lat,
            lng = lng
        )
    }
}