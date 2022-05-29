package com.example.iot_project

class User {
    var email: String? = null
    var password: String? = null
    var latitude: String? = null
    var longitude: String? = null
    var flag: Boolean? = null

    constructor() {}
    constructor(email: String?, password: String?,latitude: String?,longitude: String?) {
        this.email = email
        this.password = password
        this.latitude = latitude
        this.longitude = longitude
    }

    constructor(flag: Boolean){
        this.flag = flag
    }

}