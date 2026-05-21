package com.crisis.dataLayer

import com.crisis.dataLayer.responses.AiReportResponse
import com.crisis.dataLayer.responses.CitiesResponse
import com.crisis.dataLayer.responses.CommonResponse
import com.crisis.dataLayer.responses.ComplaintResponse
import com.crisis.dataLayer.responses.DashBoardResponse
import com.crisis.dataLayer.responses.LoginResponse
import com.crisis.dataLayer.responses.MapsResponse
import com.crisis.dataLayer.responses.MessageResponse
import com.crisis.dataLayer.responses.SafeZones
import com.crisis.dataLayer.responses.SosResponse
import com.crisis.dataLayer.responses.UserDetailsVideoComplaint
import com.crisis.dataLayer.responses.VideoComplaintResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface Api {

    @GET("register")
    suspend fun register(
        @Query("mail") mail: String,
        @Query("password") password: String,
        @Query("name") name: String,
        @Query("mobile") mobile: String,
        @Query("type") type: String,
        @Query("location") location: String,
        @Query("city") city: String,
    ): Response<CommonResponse>

    @GET("login")
    suspend fun login(
        @Query("mail") mail: String,
        @Query("password") password: String,
    ): Response<LoginResponse>

    @GET("updateCity")
    suspend fun updateCity(
        @Query("city") city: String,
        @Query("id") id: String,
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("addComplaint/")
    suspend fun addComplaint(
        @Field("title") title: String,
        @Field("decriptionPoint") decriptionPoint: String,
        @Field("locationsAddress") locationsAddress: String,
        @Field("latLon") latLon: String,
        @Field("raisedOn") raisedOn: String,
        @Field("status") status: String,
        @Field("userid") userid: String,
        @Field("image") image: String,
        @Field("userCheck") userCheck: String,
    ): Response<CommonResponse>


    @GET("getCities")
    suspend fun getCities(): Response<CitiesResponse>


    @GET("sendMessage")
    suspend fun sendMessage(
        @Query("title") title: String,
        @Query("message") message: String,
        @Query("city") city: String,
        @Query("raise") raise: String,
    ): Response<CommonResponse>

    @GET("updateCityAndGetMessages")
    suspend fun getNotifications(
        @Query("city") city: String,
        @Query("userid") userid: String,
    ): Response<MessageResponse>


    @GET("viewComplaintOfUsers")
    suspend fun getUserComplaints(
        @Query("user_id") user_id: String,
    ): Response<ComplaintResponse>


    @GET("getUserDetails")
    suspend fun getUserDetails(
        @Query("id") id: String,
    ): Response<LoginResponse>


    @GET("complaintGraphDetails")
    suspend fun getDashBoardCount(
    ): Response<DashBoardResponse>

    @GET("getOurCityComplaints")
    suspend fun cityComplaints(
        @Query("city") city: String,
        @Query("typeOfView") typeOfView: String? = "",
    ): Response<ComplaintResponse>

    @GET("updateComplaint")
    suspend fun updateComplaint(
        @Query("userid") userid: String,
        @Query("complaintID") complaintID: String,
    ): Response<ComplaintResponse>

    @GET("updateComplaintVolunteer")
    suspend fun updateComplaintVolunteer(
        @Query("userid") userid: String,
        @Query("complaintID") complaintID: String,
    ): Response<ComplaintResponse>

    @GET("completedComplaint")
    suspend fun completed(
        @Query("complaintID") complaintID: String,
    ): Response<ComplaintResponse>

    @GET("viewComplaintOfLatLon")
    suspend fun viewComplaintOfLatLon(): Response<MapsResponse>

    @GET("sendSos")
    suspend fun sendSOS(
        @Query("title") title: String,
        @Query("message") message: String,
        @Query("userid") userid: String,
        @Query("locations") locations: String,
        @Query("typeOF") typeOF: String,
    ): Response<CommonResponse>

    @GET("getSosOFUserID")
    suspend fun getSOS(
        @Query("id") id: String,
        @Query("iamWitch") iamWitch: String,
    ): Response<SosResponse>

    @GET("getWithUserDetails")
    suspend fun getWithUserDetails(
        @Query("iamWitch") iamWitch: String = "skjdfhh",
    ): Response<SosResponse>


    @GET("addSafeZone")
    suspend fun addSafeZone(
        @Query("title") title: String,
        @Query("details") details: String,
        @Query("LatLon") LatLon: String,
    ): Response<CommonResponse>


    @GET("getSafeZones")
    suspend fun getSafeZone(): Response<SafeZones>

    @Multipart
    @POST("add_complaint_with_View/")
    suspend fun addVideoComplaint(
        @Part("userid") userid: RequestBody,
        @Part("desc") desc: RequestBody,
        @Part file: MultipartBody.Part?,
    ): Response<CommonResponse>


    @GET("getUploadedVideo/")
    suspend fun getUploadedVideo(
        @Query("userId") userId: String,
    ): Response<VideoComplaintResponse>

    @GET("getUploadedWithUserDetails/")
    suspend fun getAllVideoComplaints(): Response<UserDetailsVideoComplaint>

    @GET("viewOfType/")
    suspend fun viewUsersByType(
        @Query("typeOF") typeOF: String,
    ): Response<LoginResponse>

    @GET("aiCrisisAgent/")
    suspend fun aiCrisisAgent(): Response<AiReportResponse>

}
