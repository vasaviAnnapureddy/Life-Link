package com.crisis.dataLayer.responses

import com.google.gson.annotations.SerializedName

data class CitiesResponse(
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("cities") var cities: ArrayList<Data> = arrayListOf(),
) {
    data class Data(
        @SerializedName("city") var city: String? = null,
    )
}
