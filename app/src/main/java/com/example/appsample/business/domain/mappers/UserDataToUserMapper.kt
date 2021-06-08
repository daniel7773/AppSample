package com.example.appsample.business.domain.mappers

import com.example.appsample.business.data.models.AddressData
import com.example.appsample.business.data.models.CompanyData
import com.example.appsample.business.data.models.GeoData
import com.example.appsample.business.data.models.UserData
import com.example.appsample.business.domain.model.Address
import com.example.appsample.business.domain.model.Company
import com.example.appsample.business.domain.model.Geo
import com.example.appsample.business.domain.model.User

object UserDataToUserMapper {

    fun map(userData: UserData) = User(
        id = userData.id,
        username = userData.username,
        email = userData.email,
        website = userData.website,
        address = mapAddressEntityToAddress(userData.address),
        phone = userData.phone,
        company = mapCompanyEntityToCompany(userData.company)
    )

    private fun mapAddressEntityToAddress(address: AddressData?) = address?.run {
        Address(
            street = street,
            suite = suite,
            city = city,
            zipcode = zipcode,
            geo = mapGeoEntityToGeo(geo)
        )
    }

    private fun mapCompanyEntityToCompany(company: CompanyData?) = company?.run {
        Company(
            name = name,
            catchPhrase = catchPhrase,
            bs = bs
        )
    }

    private fun mapGeoEntityToGeo(geo: GeoData?) = geo?.run {
        Geo(
            lat = lat,
            lng = lng
        )
    }
}