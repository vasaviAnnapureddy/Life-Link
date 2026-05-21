package com.crisis.ui.commonPath

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.crisis.databinding.LocationPointBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationPoint : Fragment() {
    private var userBind: LocationPointBinding? = null
    private val bind get() = userBind
    private val models by activityViewModels<PointerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        userBind = LocationPointBinding.inflate(layoutInflater)

        return bind?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPoint()
        with(bind!!) {
            val child =
                childFragmentManager.findFragmentById(maps.id) as SupportMapFragment
            child.getMapAsync { mark ->
                when (arguments?.getString("something")) {
                    "nothing" -> {
                        models.getAllLatLon { arrayList ->
                            var num = 0
                            arrayList.forEach { data ->
                                data.latLon?.split(",")?.let {
                                    if (it.size == 2) {
                                        val lat = it[0].toDoubleOrNull()
                                        val lon = it[1].toDoubleOrNull()
                                        if (lat != null && lon != null) {
                                            mark.addMarker(
                                                MarkerOptions().position(LatLng(lat, lon))
                                                    .title("Complaint Location").icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            checkAndColor(data.userCheck)
                                                        )
                                                    )
                                            )

                                            if (num == 0) {
                                                mark.moveCamera(
                                                    CameraUpdateFactory.newLatLngZoom(
                                                        LatLng(lat, lon),
                                                        15f
                                                    )
                                                )
                                            }
                                            num++
                                        }

                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        arguments?.getString("latlon")?.split(",")?.let { latlon ->
                            if (latlon.size == 2) {

                                val latLng = LatLng(latlon[0].toDouble(), latlon[1].toDouble())
                                mark.addMarker(
                                    MarkerOptions().position(latLng).title("Complaint Location")
                                )
                                mark.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                                )


                            }
                        }

                    }
                }
            }
        }


    }

    private fun checkAndColor(userCheck: String?) = when (userCheck) {
        "High" -> {
            BitmapDescriptorFactory.HUE_RED
        }

        "Medium" -> {
            BitmapDescriptorFactory.HUE_ORANGE
        }

        "Low" -> {
            BitmapDescriptorFactory.HUE_YELLOW
        }

        else -> {
            BitmapDescriptorFactory.HUE_BLUE
        }
    }

}
