package com.example.appsample.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "street")
    var street: String,

    @ColumnInfo(name = "suite")
    var suite: String,

    @ColumnInfo(name = "city")
    var city: String,

    @ColumnInfo(name = "zipcode")
    var zipcode: String,

    @ColumnInfo(name = "lat")
    var lat: Double,

    @ColumnInfo(name = "lng")
    var lng: Double,

    @ColumnInfo(name = "phone")
    var phone: String,

    @ColumnInfo(name = "website")
    var website: String,

    @ColumnInfo(name = "company_name")
    var company_name: String,

    @ColumnInfo(name = "catch_phrase")
    var catch_phrase: String,

    @ColumnInfo(name = "bs")
    var bs: String,

    @ColumnInfo(name = "updated_at")
    var updated_at: String,

    @ColumnInfo(name = "created_at")
    var created_at: String
) {


    companion object {

        fun nullTitleError(): String {
            return "You must enter a name."
        }

        fun nullIdError(): String {
            return "UserCacheEntity object has a null id. This should not be possible. Check local database."
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserCacheEntity

        if (id != other.id) return false
        if (username != other.username) return false
        if (email != other.email) return false
        if (street != other.street) return false
        if (suite != other.suite) return false
        if (city != other.city) return false
        if (zipcode != other.zipcode) return false
        if (lat != other.lat) return false
        if (lng != other.lng) return false
        if (phone != other.phone) return false
        if (website != other.website) return false
        if (company_name != other.company_name) return false
        if (catch_phrase != other.catch_phrase) return false
        if (bs != other.bs) return false
//        if (updated_at != other.updated_at) return false // ignore this
        if (created_at != other.created_at) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + street.hashCode()
        result = 31 * result + suite.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + zipcode.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + lng.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + website.hashCode()
        result = 31 * result + company_name.hashCode()
        result = 31 * result + catch_phrase.hashCode()
        result = 31 * result + bs.hashCode()
        result = 31 * result + updated_at.hashCode()
        result = 31 * result + created_at.hashCode()
        return result
    }
}



