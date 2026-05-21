package com.crisis.dataLayer.responses

import com.google.gson.annotations.SerializedName

data class MapsResponse (

    @SerializedName("error"   ) var error   : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()
){
    data class Data (
        @SerializedName("id"               ) var id              : Int?    = null,
        @SerializedName("title"            ) var title           : String? = null,
        @SerializedName("description"      ) var description     : String? = null,
        @SerializedName("location_address" ) var locationAddress : String? = null,
        @SerializedName("lat_lon"          ) var latLon          : String? = null,
        @SerializedName("raised_on"        ) var raisedOn        : String? = null,
        @SerializedName("ngoid"            ) var ngoid           : String? = null,
        @SerializedName("volunteer_id"     ) var volunteerId     : String? = null,
        @SerializedName("user_id"          ) var userId          : String? = null,
        @SerializedName("status"           ) var status          : String? = null,
        @SerializedName("volunterWrite"    ) var volunterWrite   : String? = null,
        @SerializedName("volunteerCheck"   ) var volunteerCheck  : String? = null,
        @SerializedName("userCheck"        ) var userCheck       : String? = null,
        @SerializedName("imagePath"        ) var imagePath       : String? = null
    )

}