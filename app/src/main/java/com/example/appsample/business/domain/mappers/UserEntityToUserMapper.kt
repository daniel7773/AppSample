package com.example.appsample.business.domain.mappers

import com.example.appsample.business.data.models.AddressEntity
import com.example.appsample.business.data.models.CompanyEntity
import com.example.appsample.business.data.models.GeoEntity
import com.example.appsample.business.data.models.UserEntity
import com.example.appsample.business.domain.model.Address
import com.example.appsample.business.domain.model.Company
import com.example.appsample.business.domain.model.Geo
import com.example.appsample.business.domain.model.User

object UserEntityToUserMapper {

    fun map(userEntity: UserEntity) = User(
        id = userEntity.id,
        username = userEntity.username,
        email = userEntity.email,
        website = userEntity.website,
        address = mapAddressEntityToAddress(userEntity.address),
        phone = userEntity.phone,
        company = mapCompanyEntityToCompany(userEntity.company)
    )

    private fun mapAddressEntityToAddress(address: AddressEntity?) = address?.run {
        Address(
            street = street,
            suite = suite,
            city = city,
            zipcode = zipcode,
            geo = mapGeoEntityToGeo(geo)
        )
    }

    private fun mapCompanyEntityToCompany(company: CompanyEntity?) = company?.run {
        Company(
            name = name,
            catchPhrase = catchPhrase,
            bs = catchPhrase
        )
    }

    private fun mapGeoEntityToGeo(geo: GeoEntity?) = geo?.run {
        Geo(
            lat = lat,
            lng = lng
        )
    }
}