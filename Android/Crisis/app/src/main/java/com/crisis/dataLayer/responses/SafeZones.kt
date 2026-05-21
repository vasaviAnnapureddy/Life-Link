package com.crisis.dataLayer.responses

import com.google.gson.annotations.SerializedName

data class SafeZones (

    @SerializedName("error"   ) var error   : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()
){
    data class Data(
        @SerializedName("id"          ) var id          : Int?    = null,
        @SerializedName("title"       ) var title       : String? = null,
        @SerializedName("description" ) var description : String? = null,
        @SerializedName("location"    ) var location    : String? = null
    )
}