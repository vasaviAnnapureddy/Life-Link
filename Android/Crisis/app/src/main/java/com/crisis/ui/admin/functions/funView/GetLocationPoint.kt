package com.crisis.ui.admin.functions.funView

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.crisis.databinding.GetLocationBinding
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GetLocationPoint : Fragment() {
    private var _bind: GetLocationBinding? = null
    private val bind get() = _bind
    private val models by activityViewModels<PointerViewModel>()

    private val fused by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _bind = GetLocationBinding.inflate(layoutInflater)
        return bind?.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(bind!!) {
            val maps = childFragmentManager.findFragmentById(map2.id) as SupportMapFragment
            val nav = findNavController()
            var text = ""
            maps.getMapAsync { maps1 ->

                maps1.setOnMapClickListener { latLng ->
                    maps1.clear()
                    maps1.addMarker(
                        MarkerOptions().position(latLng).icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_MAGENTA
                            )
                        )
                    )
                    latLngToAddress(latLng) {
                        resultText.text = it
                        text = "$it&*&*&*${latLng.latitude},${latLng.longitude}"
                    }
                }

                setBack.setOnClickListener {
                    models.textResult.value = text
                    nav.navigateUp()
                }

                fused.lastLocation.addOnSuccessListener {
                    maps1.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 14f)
                    )
                }
            }
        }
    }

    private fun latLngToAddress(it: LatLng, location: (String) -> Unit) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Geocoder(requireContext()).getFromLocation(it.latitude, it.longitude, 1) {
                    location.invoke(
                        it[0].getAddressLine(0)
                    )
                }
            } else {
                val fromLocation =
                    Geocoder(requireContext()).getFromLocation(it.latitude, it.longitude, 1)
                location.invoke(fromLocation?.get(0)?.getAddressLine(0).toString())
            }
        } catch (e: Exception) {
            location.invoke("invalid location")

        }
    }
    @SuppressLint("NewApi")
    private val decimal= DecimalFormat("##.#####")
}