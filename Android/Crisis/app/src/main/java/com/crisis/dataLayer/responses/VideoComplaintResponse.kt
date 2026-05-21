package com.crisis.dataLayer.responses

import com.google.gson.annotations.SerializedName

data class VideoComplaintResponse(
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
) {
    data class Data (
        @SerializedName("id"   ) var id   : Int?    = null,
        @SerializedName("desc" ) var desc : String? = null,
        @SerializedName("path" ) var path : String? = null
    )
}


