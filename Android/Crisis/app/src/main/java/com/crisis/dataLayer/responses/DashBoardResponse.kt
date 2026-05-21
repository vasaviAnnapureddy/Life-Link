package com.crisis.dataLayer.responses

import com.google.gson.annotations.SerializedName

data class DashBoardResponse(
    @SerializedName("error"   ) var error   : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()
) {
    data class Data(
        @SerializedName("status" ) var status : String? = null,
        @SerializedName("count"  ) var count  : Int?    = null
    )
}