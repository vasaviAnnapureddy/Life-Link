package com.crisis.ui.common.viewModels

import android.content.SharedPreferences
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crisis.dataLayer.ApiCall
import com.crisis.dataLayer.responses.AiReportResponse
import com.crisis.dataLayer.responses.ComplaintResponse
import com.crisis.dataLayer.responses.DashBoardResponse
import com.crisis.dataLayer.responses.LoginResponse
import com.crisis.dataLayer.responses.MapsResponse
import com.crisis.dataLayer.responses.SafeZones
import com.crisis.dataLayer.responses.SosResponse
import com.crisis.dataLayer.responses.UserDetailsVideoComplaint
import com.crisis.dataLayer.responses.VideoComplaintResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PointerViewModel : ViewModel() {
    private var _dialog = MutableStateFlow(false)
    val dialog get() = _dialog

    private val _toast = MutableStateFlow<String?>(null)
    val toast get() = _toast
    private val _emergency = MutableStateFlow<Boolean>(false)
    val emergency get() = _emergency
    private var _user = MutableStateFlow<LoginResponse.Users?>(null)
    val user get() = _user
    private val _complaints = MutableStateFlow<ArrayList<ComplaintResponse.Data>>(arrayListOf())
    val complaints get() = _complaints

    val textResult = MutableStateFlow("")


    fun getVideoComplaints(data: (ArrayList<UserDetailsVideoComplaint.Data>) -> Unit) {
        viewModelScope.async {
            async {
                try {
                    retrofit.getAllVideoComplaints()
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                response?.body()?.data?.let {
                    data(it)
                }
            }
        }.start()
    }

    fun addVideo(
        userid: RequestBody,
        desc: RequestBody,
        multi: MultipartBody.Part,
        state: (String) -> Unit,
    ) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.addVideoComplaint(userid = userid, desc = desc, file = multi)
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                response?.body()?.message?.let {
                    _toast.value = it
                    state(it)
                }

                dialog.value = false
            }
        }.start()
    }

    fun getVideos(userid: String, data: (ArrayList<VideoComplaintResponse.Data>) -> Unit) {
        viewModelScope.async {
            async {
                try {
                    retrofit.getUploadedVideo(userId = userid)
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                response?.body()?.data?.let {
                    data(it)
                }
            }
        }.start()

    }


    private val retrofit = ApiCall.retrofit

    fun signUp(
        name: String,
        email: String,
        password: String,
        mobile: String,
        type: String,
        location: String,
        city: String,
    ) {
        _dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.register(
                        mail = email,
                        password = password,
                        name = name,
                        mobile = mobile,
                        type = type,
                        location = location,
                        city = city,
                    )
                } catch (e: Exception) {
                    Log.i("skdjfkdsjf", "${e.message}")
                    null
                }
            }.await().let { response ->
                response?.body()?.message?.let { _toast.value = it }
                _dialog.value = false
            }
        }.start()
    }

    fun login(mail: String, password: String) {
        _dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.login(mail, password)
                } catch (e: Exception) {
                    Log.i("lkjdflkdf", "${e.message}")
                    null
                }
            }.await().let { response ->

                response?.body()?.data?.let {
                    if (it.isEmpty()) {
                        _user.value = null
                    } else {
                        _user.value = it[0]
                    }
                }
                response?.body()?.message?.let { _toast.value = it }
                _dialog.value = false
            }
        }.start()

    }

    fun setDialog() {
        viewModelScope.async {
            delay(1000)
            _toast.value = null
        }.start()
    }

    fun updateCity(city: String, id: String, status: (String) -> Unit) {
        viewModelScope.async {
            async {
                try {
                    retrofit.updateCity(city, id)
                } catch (e: Exception) {
                    Log.i("PointerPoint", "${e.message}")
                    null
                }
            }.await().let {
                status.invoke(it?.body()?.message ?: "Nan")
                Log.i("PointerPoint", "${it?.body()}")
            }
        }.start()
    }

    fun addComplaint(
        title: String,
        decriptionPoint: String,
        locationsAddress: String,
        latLon: String,
        raisedOn: String,
        status: String,
        userid: String,
        image: String,
        userCheck: String,
    ) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.addComplaint(
                        title = title,
                        decriptionPoint = decriptionPoint,
                        locationsAddress = locationsAddress,
                        latLon = latLon,
                        raisedOn = raisedOn,
                        status = status,
                        userid = userid,
                        image = image,
                        userCheck = userCheck
                    )
                } catch (e: Exception) {
                    null
                }
            }.await().let {
                val message = it?.body()?.message ?: "Complaint Failed to Add"
                toast.value = message

                dialog.value = false
            }
        }.start()
    }


    fun getCities(list: (ArrayList<String>) -> Unit) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.getCities()
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                response?.body()?.cities?.let {
                    val k = arrayListOf<String>()
                    it.forEachIndexed { _, data ->
                        data.city?.let {
                            if (!it.isDigitsOnly()) {
                                k.add("${k.size + 1}) ${it}")
                            }
                        }
                    }
                    list.invoke(k)
                }
                dialog.value = false

            }
        }.start()
    }

    fun sendMessage(title: String, message: String, city: String, raise: String) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.sendMessage(
                        title = title,
                        message = message,
                        city = city,
                        raise = raise
                    )
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                response?.body()?.message?.let {
                    toast.value = it
                }
                dialog.value = false
            }
        }.start()
    }

    fun getNotify(
        city: String,
        userid: String,
        shared: SharedPreferences,
        content: (String, String) -> Unit,
    ) {

        viewModelScope.async {
            async {
                try {
                    retrofit.getNotifications(city = city, userid)
                } catch (e: Exception) {
                    Log.i("PointerPointer", "${e.message}")
                    null
                }
            }.await().let { response ->
                response?.body()?.data?.let { data ->

                    val preViews = shared.getString("preViews", "")
                    if (preViews != data.id.toString()) {
                        val string = "<b>${data.title}</b><br>${data.message}"

                        val id: String = data.id.toString()

                        Log.i("PointerPointer", "$data $preViews")

                        emergency.value = true
                        content.invoke(string, id)
                    }


                }
            }
        }.start()
    }


    fun viewComplaintsOfUsers(userid: String, array: (ArrayList<ComplaintResponse.Data>) -> Unit) {

        viewModelScope.async {
            async {
                try {
                    retrofit.getUserComplaints(user_id = userid)
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                response?.body()?.data?.let {
                    array.invoke(it)
                }
                dialog.value = false
            }
        }.start()
    }

    fun getUserDetails(id: String, user: (LoginResponse.Users?) -> Unit) {
        dialog.value = true
        var point: LoginResponse.Users? = null
        viewModelScope.async {
            async {
                try {
                    retrofit.getUserDetails(id = id)
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                user.invoke(point)
                response?.body()?.data?.let {
                    if (it.isNotEmpty()) {
                        point = it[0]
                        user.invoke(point)
                    }
                }
                dialog.value = false
            }
        }.start()

    }

    fun getDashBoardDetails(pointerView: (ArrayList<DashBoardResponse.Data>) -> Unit) {
        viewModelScope.async {
            async {
                try {
                    retrofit.getDashBoardCount()
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                response?.body()?.data?.let {
                    pointerView(it)
                }

            }
        }.start()
    }

    fun getCityComplaints(
        city: String,
        data: (ArrayList<ComplaintResponse.Data>) -> Unit,
        type: String? = "",
    ) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.cityComplaints(city = city, typeOfView = type)
                } catch (e: Exception) {
                    Log.i("slkjskldfdf", "${e.message}")
                    null
                }
            }.await().let { response ->
                response?.body()?.data?.let {
                    data.invoke(it)
                }
                dialog.value = false
            }
        }.start()
    }


    fun getUpdateComplaint(userid: String, complaintID: String) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.updateComplaint(userid = userid, complaintID = complaintID)
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                response?.body()?.message?.let {
                    _toast.value = it
                }
                dialog.value = false

            }
        }.start()
    }

    fun getUpdateComplaintVolunteer(userid: String, complaintID: String) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.updateComplaintVolunteer(userid = userid, complaintID = complaintID)
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                response?.body()?.message?.let {
                    _toast.value = it
                }
                dialog.value = false

            }
        }.start()
    }

    fun completedComplaint(id: String) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.completed(complaintID = id)
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                response?.body()?.message?.let {
                    _toast.value = it
                }
                dialog.value = false

            }
        }.start()
    }

    fun getAllLatLon(function: (ArrayList<MapsResponse.Data>) -> Unit) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.viewComplaintOfLatLon()
                } catch (e: Exception) {
                    Log.i("slkjskldfdf", "${e.message}")
                    null
                }
            }.await().let { response ->
                response?.body()?.data?.let {
                    function.invoke(it)
                }
                dialog.value = false
            }
        }.start()
    }

    fun sendSos(message: String, userid: String, latLon: String, user: String) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.sendSOS(
                        title = "SOS",
                        message = message,
                        userid = userid,
                        locations = latLon,
                        typeOF = user
                    )
                } catch (e: Exception) {
                    Log.i("slkjskldfdf", "${e.message}")
                    null
                }
            }.await().let { response ->
                response?.body()?.message?.let {
                    toast.value = it
                }
                dialog.value = false
            }
        }.start()

    }

    fun getComplaintsOFMine(
        id: String,
        array: (ArrayList<SosResponse.Data>) -> Unit,
        viewPort: String? = "", type: String,
    ) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    if (type == "User") {
                        retrofit.getSOS(id = id, iamWitch = viewPort ?: "")
                    } else {
                        retrofit.getWithUserDetails()

                    }
                } catch (e: Exception) {
                    Log.i("slkjskldfdf", "${e.message}")
                    null
                }
            }.await().let { response ->
                response?.body()?.data?.let {
                    array.invoke(it)
                }
                dialog.value = false

            }
        }.start()
    }

    fun addSafeZone(
        lat: String,
        title: String,
        descriptionPoint2: String,
        result: (String) -> Unit,
    ) {

        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.addSafeZone(title = title, details = descriptionPoint2, LatLon = lat)
                } catch (e: Exception) {
                    null
                }
            }.await().let { response ->
                response?.body()?.message?.let { s ->
                    result.invoke(s)
                    _toast.value = s
                }
                dialog.value = false
            }
        }.start()
    }

    fun viewUsersByType(typeOF: String, result: (ArrayList<LoginResponse.Users>) -> Unit) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.viewUsersByType(typeOF = typeOF)
                } catch (e: Exception) {
                    Log.i("ViewUsers", "${e.message}")
                    null
                }
            }.await().let { response ->
                response?.body()?.data?.let(result)
                dialog.value = false
            }
        }.start()
    }

    fun getAiCrisisReport(result: (AiReportResponse) -> Unit) {
        dialog.value = true
        viewModelScope.async {
            async {
                try { retrofit.aiCrisisAgent() } catch (e: Exception) {
                    Log.i("AiAgent", "${e.message}"); null
                }
            }.await().let { response ->
                result(response?.body() ?: AiReportResponse(error = true, message = "No response from server"))
                dialog.value = false
            }
        }.start()
    }

    fun getSafeZone(result: (ArrayList<SafeZones.Data>) -> Unit) {
        dialog.value = true
        viewModelScope.async {
            async {
                try {
                    retrofit.getSafeZone()
                } catch (e: Exception) {
                    null
                }
            }.await().let {
                it?.body()?.data?.let(result)

                dialog.value = false

            }
        }.start()
    }



}