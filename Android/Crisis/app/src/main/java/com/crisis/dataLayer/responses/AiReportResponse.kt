package com.crisis.dataLayer.responses

import com.google.gson.annotations.SerializedName

data class AiReportResponse(
    @SerializedName("error")   val error: Boolean?  = null,
    @SerializedName("report")  val report: String?  = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("stats")   val stats: Stats?    = null,
) {
    data class Stats(
        @SerializedName("total")     val total: Int     = 0,
        @SerializedName("pending")   val pending: Int   = 0,
        @SerializedName("active")    val active: Int    = 0,
        @SerializedName("completed") val completed: Int = 0,
    )
}
