package com.crisis.dataLayer.responses

import com.google.gson.annotations.SerializedName

data class UserDetailsVideoComplaint (
    @SerializedName("error"   ) var error   : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()

) {
    data class User (

        @SerializedName("name"   ) var name   : String? = null,
        @SerializedName("mobile" ) var mobile : String? = null,
        @SerializedName("mail"   ) var mail   : String? = null

    )

    data class Data (

        @SerializedName("id"   ) var id   : Int?    = null,
        @SerializedName("desc" ) var desc : String? = null,
        @SerializedName("path" ) var path : String? = null,
        @SerializedName("user" ) var user : User?   = User()

    )
}