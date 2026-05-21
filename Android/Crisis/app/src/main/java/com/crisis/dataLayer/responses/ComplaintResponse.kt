package com.crisis.dataLayer.responses

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ComplaintResponse(
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
) {
    data class Data(
        @SerializedName("id") var id: String? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("description") var description: String? = null,
        @SerializedName("location_address") var locationAddress: String? = null,
        @SerializedName("lat_lon") var latLon: String? = null,
        @SerializedName("raised_on") var raisedOn: String? = null,
        @SerializedName("ngoid") var ngoid: String? = null,
        @SerializedName("volunteer_id") var volunteerId: String? = null,
        @SerializedName("user_id") var user_id: String? = null,
        @SerializedName("status") var status: String? = null,
        @SerializedName("imagePath") var imagePath: String? = null,
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(title)
            parcel.writeString(description)
            parcel.writeString(locationAddress)
            parcel.writeString(latLon)
            parcel.writeString(raisedOn)
            parcel.writeString(ngoid)
            parcel.writeString(volunteerId)
            parcel.writeString(user_id)
            parcel.writeString(status)
            parcel.writeString(imagePath)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Data> {
            override fun createFromParcel(parcel: Parcel): Data {
                return Data(parcel)
            }

            override fun newArray(size: Int): Array<Data?> {
                return arrayOfNulls(size)
            }
        }

    }
}
