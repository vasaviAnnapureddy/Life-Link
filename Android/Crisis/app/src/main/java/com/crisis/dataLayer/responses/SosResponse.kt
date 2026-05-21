package com.crisis.dataLayer.responses

import com.google.gson.annotations.SerializedName

data class SosResponse(
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
) {
    data class Data(
        @SerializedName("title") var title: String? = null,
        @SerializedName("message") var message: String? = null,
        @SerializedName("locations") var locations: String? = null,
        @SerializedName("typeOF") var typeOF: String? = null,
        @SerializedName("user_details") var userDetails: UserDetails? = UserDetails(),
    )

    data class UserDetails(

        @SerializedName("id") var id: Int? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("mobile") var mobile: String? = null,
        @SerializedName("mail") var mail: String? = null,
        @SerializedName("user_type") var user_type: String? = null,
        @SerializedName("location") var location: String? = null,
        @SerializedName("city") var city: String? = null,

        )
}