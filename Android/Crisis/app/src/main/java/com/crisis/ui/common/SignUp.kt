package com.crisis.ui.common

import android.Manifest
import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.crisis.databinding.SignupPartBinding
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SignUp : Fragment() {
    private var loginBind: SignupPartBinding? = null
    private val bind get() = loginBind
    private val models by activityViewModels<PointerViewModel>()

    private val location by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val permission by lazy {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        loginBind = SignupPartBinding.inflate(layoutInflater)
        return bind?.root
    }

    @SuppressLint("MissingPermission", "NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPoint()

        with(bind!!) {
            val nav = findNavController()
            models.toast.onEach {
                if (it.toString().contains("success")) {
                    nav.navigateUp()
                }
            }.launchIn(lifecycleScope)



            appCompatButton2.setOnClickListener {
                val name = sName.text.toString().trim()
                val email = sEmail.text.toString().trim()
                val password = sPassword.text.toString().trim()
                val mobile = sMobile.text.toString().trim()
                if (name.isEmpty()) {
                    toast("Please Enter your name")
                } else if (email.isEmpty()) {
                    toast("Please Enter your email")
                } else if (password.isEmpty()) {
                    toast("Please Enter your password")
                } else if (mobile.isEmpty()) {
                    toast("Please Enter your mobile")
                } else if (!email.contains("@gmail.com")) {
                    toast("Please Enter a valid email")
                } else if (mobile.length != 10) {
                    toast("Please a valid Mother Number")
                } else {
                    requireActivity().checkAndRequest(array = permission) {
                        if (!it) {
                            requireActivity().requestPermissionPoint(permission)
                        } else {

                            location.lastLocation.addOnSuccessListener { location1 ->
                                if (location1 != null) {
                                    models.signUp(
                                        name,
                                        email,
                                        password,
                                        mobile,
                                        type = "user",
                                        location = "${decimal.format(location1.latitude)},${
                                            decimal.format(location1.longitude)
                                        }",
                                        city = ""
                                    )
                                } else {
                                    models.signUp(
                                        name,
                                        email,
                                        password,
                                        mobile,
                                        type = "user",
                                        location = "13.6288,79.4192",
                                        city = ""

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
            createAc.setOnClickListener {
                nav.navigateUp()
            }
        }
    }

    @SuppressLint("NewApi")
    private val decimal = DecimalFormat("##.#######")

}