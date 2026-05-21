package com.crisis.dataLayer.responses

import com.google.gson.annotations.SerializedName

data class CountResponse(
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
) {
    data class Data(
        @SerializedName("count") var count: Int? = null,
        @SerializedName("status") var status: String? = null,
    )
}
