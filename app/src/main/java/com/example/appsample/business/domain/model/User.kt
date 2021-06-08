package com.example.appsample.business.domain.model

data class User(
    var id: Int = 0,
    var username: String? = null,
    var email: String? = null,
    var address: Address? = null,
    var phone: String? = null,
    var website: String? = null,
    var company: Company? = null
) {

    fun streetAndSuit(): String = "${address?.street} ${address?.suite}"

    fun latAndLang(): String = "lat ${address?.geo?.lat} lng ${address?.geo?.lng}"

}