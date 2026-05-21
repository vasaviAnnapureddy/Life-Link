package com.crisis.dataLayer.responses

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = null,
) {
    data class Data(
        @SerializedName("id") var id: Int? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("message") var message: String? = null,
        @SerializedName("city") var city: String? = null,
        @SerializedName("raiseOF") var raise: String? = null,
    )
}
