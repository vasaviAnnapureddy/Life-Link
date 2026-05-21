package com.crisis.dataLayer.responses

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Users> = arrayListOf(),
) {
    data class  Users(
        @SerializedName("id") var id: Int? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("mobile") var mobile: String? = null,
        @SerializedName("mail") var mail: String? = null,
        @SerializedName("type") var type: String? = null,
        @SerializedName("city") var city: String? = null,
    )
}
