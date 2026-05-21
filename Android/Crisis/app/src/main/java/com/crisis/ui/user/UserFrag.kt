package com.crisis.ui.user

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.crisis.R
import com.crisis.databinding.MessagePointBinding
import com.crisis.databinding.UserMainBinding
import com.crisis.ui.SingleModule
import com.crisis.ui.admin.adapters.AdminDash
import com.crisis.ui.admin.models.Core
import com.crisis.ui.common.checkAndRequest
import com.crisis.ui.common.homePoint
import com.crisis.ui.common.requestPermissionPoint
import com.crisis.ui.common.spanned
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class UserFrag : Fragment() {
    private var userBind: UserMainBinding? = null
    private val bind get() = userBind
    private val models by activityViewModels<PointerViewModel>()
    private val messagePoint by lazy {
        MessagePointBinding.inflate(layoutInflater)
    }
    private val dialog by lazy {
        Dialog(requireActivity()).apply {
            setContentView(messagePoint.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private val fused by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        userBind = UserMainBinding.inflate(layoutInflater)
        return bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homePoint()
        with(bind!!) {
            val nav = findNavController()
            models.emergency.onEach {

                if (it) dialog.show() else dialog.dismiss()

            }.launchIn(lifecycleScope)



            cycle2.layoutManager = GridLayoutManager(requireContext(), 2)
            cycle2.adapter = AdminDash(
                requireActivity(), arrayListOf(
                    Core(name = "Raise Complaint", resId = R.drawable.complain),
                    Core(name = "View Complaint", resId = R.drawable.list),
                    Core(name = "SOS", resId = R.drawable.alarm),
                    Core(name = "Safe Zones", resId = R.drawable.safe),
                    Core(name = "Add Ur Video", resId = R.drawable.shield),
                    Core(name = "Logout", resId = R.drawable.logout)
                )
            ) {
                when (it) {
                    0 -> {
                        nav.navigate(R.id.action_userFrag_to_addComplaint)
                    }

                    1 -> {
                        nav.navigate(R.id.action_userFrag_to_viewComplaint)
                    }

                    2 -> {
                        nav.navigate(R.id.action_userFrag_to_sosComplaints)
                    }

                    3 -> {
                        nav.navigate(R.id.action_userFrag_to_viewSafeZone)
                    }

                    4 -> {
                        nav.navigate(R.id.action_userFrag_to_addVideoComplaint)
                    }

                    5 -> {
                        dialog()
                    }

                    else -> {}
                }
            }
            messagePoint.exitPoint.setOnClickListener {

                dialog.dismiss()


            }
            locationUpdate()
        }


    }

    private fun dialog() {
        MaterialAlertDialogBuilder(requireActivity()).apply {
            setTitle("Do you want to Logout ?")
            setPositiveButton("Yes", { v, _ ->
                requireActivity().apply {
                    getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().apply()
                    finishAffinity()
                    startActivity(Intent(this, SingleModule::class.java))
                }
                v.dismiss()
            })
            setNegativeButton("No") { v, _ ->
                v.dismiss()
            }
            show()
        }
    }

    private val permission by lazy {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
    private val shared by lazy {
        requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        locationUpdate()
    }

    private var id = ""

    // location update
    @SuppressLint("MissingPermission")
    private fun locationUpdate() {
        val activity = requireActivity()
        activity.checkAndRequest(array = permission) {
            if (it) {
                fused.lastLocation.addOnSuccessListener { location ->
                    location?.let { latPoint ->
                        locationCity(latPoint) { city ->
                            models.getNotify(
                                city = city,
                                shared.getString("id", "") ?: "Nan", shared
                            ) { view, i ->
                                id = i
                                messagePoint.textContent.text = spanned(view)
                                shared.edit().putString("preViews", i).apply()
                            }

                        }
                    }
                }.addOnFailureListener {
                    models.toast.value = "Location issue"
                }
            } else {
                activity.requestPermissionPoint(array = permission)
            }
        }
    }

    private fun locationCity(lat: Location, hh: (String) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Geocoder(requireActivity()).getFromLocation(lat.latitude, lat.longitude, 1) {
                hh.invoke(it[0].locality)
            }
        } else {
            Geocoder(requireActivity()).getFromLocation(lat.latitude, lat.longitude, 1)?.let {
                hh.invoke(it[0].locality)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        models.emergency.value = false
    }

}