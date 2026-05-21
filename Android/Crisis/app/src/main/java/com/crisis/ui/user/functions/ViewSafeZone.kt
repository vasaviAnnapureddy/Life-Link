package com.crisis.ui.user.functions

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.crisis.dataLayer.responses.SafeZones
import com.crisis.databinding.SafeZonesBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ViewSafeZone : Fragment() {
    private var _bind: SafeZonesBinding? = null
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
        _bind = SafeZonesBinding.inflate(layoutInflater)
        return bind?.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(bind!!) {
            backPoint()

            val frag = childFragmentManager.findFragmentById(maps22.id) as SupportMapFragment
            frag.getMapAsync { maps ->

                fused.lastLocation.addOnSuccessListener {
                    val current = LatLng(it.latitude, it.longitude)
                    maps.addMarker(
                        MarkerOptions().position(current)
                    )
                    maps.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(current, 10f)
                    )
                }
                var data = arrayListOf<SafeZones.Data>()
                models.getSafeZone { array ->
                    data = array
                    for (viewPosition in array) {

                        viewPosition.location?.split(",")?.let {
                            if (it.size == 2) {
                                val location = LatLng(
                                    it[0].toDouble(),
                                    it[1].toDouble()
                                )
                                maps.addCircle(
                                    CircleOptions().center(
                                        location
                                    ).fillColor(Color.BLUE).strokeWidth(10f)
                                )
                                maps.addMarker(
                                    MarkerOptions().position(location).title(viewPosition.title)
                                )?.tag = viewPosition.id

                            }
                        }
                    }
                }
                maps.setOnMarkerClickListener { mark ->
                    data.forEach {
                        if (it.id == mark.tag.toString().toInt()) {
                            MaterialAlertDialogBuilder(requireActivity()).apply {
                                setTitle(it.title)
                                setMessage(it.description)
                                show()
                            }
                        }
                    }
                    true
                }
            }
        }
    }
}