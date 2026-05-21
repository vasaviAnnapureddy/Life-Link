package com.crisis.ui.admin.functions

import android.Manifest
import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.crisis.databinding.SignupPartBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.checkAndRequest
import com.crisis.ui.common.requestPermissionPoint
import com.crisis.ui.common.spanned
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AddNgo : Fragment() {
    private var loginBind: SignupPartBinding? = null
    private val bind get() = loginBind
    private val models by activityViewModels<PointerViewModel>()
    private val fused by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        loginBind = SignupPartBinding.inflate(layoutInflater)
        return bind?.root
    }

    private val permission by lazy {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    @SuppressLint("MissingPermission", "NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPoint()
        with(bind!!) {
            textView2.text = spanned("Add NGO")
            val layout = (imageView2.layoutParams as ViewGroup.MarginLayoutParams).apply {
                topMargin = 100
            }
            imageView2.layoutParams = layout
            cardView91.isVisible = true

            imageView2.load(
                "https://cdn-icons-png.flaticon.com/512/3360/3360587.png"
            )
            appCompatButton2.text = spanned("Add")
            layout11.isVisible = false
            models.toast.onEach {
                if (it != null) {
                    if (it.contains("success")) {
                        findNavController().navigateUp()
                    }
                }
            }.launchIn(lifecycleScope)

            appCompatButton2.setOnClickListener {
                val name = sName.text.toString().trim()
                val password = sPassword.text.toString().trim()
                val mobile = sMobile.text.toString().trim()
                val mail = sEmail.text.toString().trim()
                val city = city.text.toString().trim()
                if (name.isEmpty()) {
                    models.toast.value = "Please enter your name"
                } else if (password.isEmpty()) {
                    models.toast.value = "Please enter your password"
                } else if (mobile.isEmpty()) {
                    models.toast.value = "Please enter your mobile"
                } else if (mail.isEmpty()) {
                    models.toast.value = "Please enter your mail"
                } else if (mobile.length != 10) {
                    models.toast.value = "Please enter a valid mobile number"
                } else if (city.isEmpty()) {
                    models.toast.value = "Please enter a valid mobile number"
                } else {
                    requireActivity().checkAndRequest(array = permission) {
                        if (!it) {
                            requireActivity().requestPermissionPoint(permission)
                        } else {

                            fused.lastLocation.addOnSuccessListener { location1 ->
                                if (location1 != null) {
                                    models.signUp(
                                        name,
                                        mail,
                                        password,
                                        mobile,
                                        type = "NGO",
                                        location = "${decimal.format(location1.latitude)}," + decimal.format(
                                            location1.longitude
                                        ),
                                        city = city
                                    )
                                } else {
                                    models.signUp(
                                        name,
                                        mail,
                                        password,
                                        mobile,
                                        type = "NGO",
                                        location = "13.6288,79.4192",
                                        city

                                    )
                                }

                            }.addOnFailureListener {
                                models.toast.value = "Something Error in location Getting"
                                Log.i("TTTTTTTT", "${it.message}")
                            }
                        }
                    }

                }
            }
        }
    }

    @SuppressLint("NewApi")
    private val decimal = DecimalFormat("##.#######")


}