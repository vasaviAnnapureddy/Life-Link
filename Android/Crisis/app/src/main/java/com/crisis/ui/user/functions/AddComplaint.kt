package com.crisis.ui.user.functions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.crisis.databinding.AddComplaintBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.checkAndRequest
import com.crisis.ui.common.requestPermissionPoint
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.ByteArrayOutputStream

class AddComplaint : Fragment() {
    private var complaintBind: AddComplaintBinding? = null
    private val bind get() = complaintBind
    private val models by activityViewModels<PointerViewModel>()

    private val fused by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    private val array by lazy {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }
    private var imageEncoded = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        complaintBind = AddComplaintBinding.inflate(layoutInflater)
        return bind?.root
    }

    @SuppressLint("Recycle", "MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPoint()
        with(bind!!) {
            models.toast.onEach {
                if (it != null) {
                    if (it.contains("Complaint")) {
                        findNavController().navigateUp()
                    }
                }

            }.launchIn(lifecycleScope)

            spinner2333.adapter = ArrayAdapter(
                requireActivity(), android.R.layout.simple_dropdown_item_1line,
                arrayOf("High", "Medium", "Low", "Other")
            )
            val register =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    it.data?.data?.let { uri ->
                        converterToBase64(uri = uri, bitmap = null)
                        bind?.selectImage?.load(uri)
                    }
                }
            val capturePoint =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                    activityResult.data?.let { it1 ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            it1.getParcelableExtra("data", Bitmap::class.java)
                        } else {
                            it1.getParcelableExtra("data")
                        }?.let {
                            converterToBase64(uri = null, bitmap = it)
                            bind?.selectImage?.load(it)
                        }
                    }
                }

            localeGallery.setOnClickListener {
                register.launch(
                    Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
                )
            }
            capture.setOnClickListener {
                capturePoint.launch(
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                )
            }
            sendComplaint.setOnClickListener {
                val title = title.text.toString().trim()
                val description = description.text.toString().trim()

                when {
                    title.isEmpty() -> {
                        models.toast.value = "Please enter the title"
                    }

                    description.isEmpty() -> {
                        models.toast.value = "Please enter the title"
                    }

                    imageEncoded == "" -> {
                        models.toast.value = "Please Select a image from Your Gallery"
                    }

                    else -> {
                        val activity = requireActivity()
                        activity.checkAndRequest(array = array) {
                            if (it) {
                                fused.lastLocation.addOnSuccessListener { location ->
                                    if (location != null) {
                                        locationGet(location) { address ->
                                            models.addComplaint(
                                                title = title,
                                                decriptionPoint = description,
                                                locationsAddress = address,
                                                latLon = "${location.latitude},${location.longitude}",
                                                raisedOn = "${System.currentTimeMillis()}",
                                                status = "Pending",
                                                userid = requireContext().getSharedPreferences(
                                                    "user",
                                                    Context.MODE_PRIVATE
                                                ).getString("id", "").toString(),
                                                image = imageEncoded,
                                                userCheck = spinner2333.selectedItem.toString()
                                            )


                                        }
                                    } else {
                                        models.toast.value = "Location is not Getting"
                                    }
                                }.addOnFailureListener {
                                    models.toast.value = "${it.message}"
                                }


                            } else {
                                activity.requestPermissionPoint(array = array)

                            }
                        }
                    }
                }

            }
        }

    }

    private fun locationGet(lat: Location, location: (String) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Geocoder(requireActivity()).getFromLocation(lat.latitude, lat.longitude, 1) {
                location.invoke(it[0].getAddressLine(0))
            }
        } else {
            val locationPoint =
                Geocoder(requireActivity()).getFromLocation(lat.latitude, lat.longitude, 1)
            locationPoint?.get(0)?.let {
                location.invoke(it.getAddressLine(0))
            }

        }
    }

    private fun converterToBase64(uri: Uri?, bitmap: Bitmap?) {
        if (uri != null) {
            val image = requireActivity().contentResolver.openInputStream(uri)?.readBytes()
            imageEncoded = Base64.encodeToString(image, Base64.DEFAULT)
        } else if (bitmap != null) {
            val out = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            imageEncoded = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT)
        }
    }
}