package com.crisis.ui.admin.functions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.crisis.R
import com.crisis.databinding.AddHubsBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AddHubs : Fragment() {
    private var _bind: AddHubsBinding? = null
    private val bind get() = _bind
    private val models by activityViewModels<PointerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _bind = AddHubsBinding.inflate(layoutInflater)
        return bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(bind!!) {
            val nav = findNavController()
            backPoint()

            var latLng: LatLng? = null

            models.textResult.onEach { s ->
                s.split("&*&*&*").let { strings ->
                    if (strings.size == 2) {
                        setLocation.text = strings[0]
                        strings[1].split(",").let {
                            if (it.size == 2) {
                                latLng = try {
                                    LatLng(it[0].toDouble(), it[1].toDouble())
                                } catch (_: Exception) {
                                    null
                                }

                            }
                        }
                    }
                }

            }.launchIn(lifecycleScope)

            addViewOF.setOnClickListener {
                val title = titlePoint.text.toString().trim()
                val descriptionPoint2 = descriptionPoint.text.toString().trim()

                if (latLng == null) {
                    models.toast.value = "Please select a LatLng Value From the Maps"
                } else {
                    if (title.isEmpty()) {
                        models.toast.value = "Pleas enter title for safe zone"
                    } else if (descriptionPoint2.isEmpty()) {
                        models.toast.value = "Pleas enter description for safe zone"
                    } else {
                        models.addSafeZone(
                            "${latLng?.latitude},${latLng?.longitude}", title, descriptionPoint2,
                        ) {
                            if (it.contains("Success")) {
                                nav.navigateUp()
                            }
                        }
                    }
                }
            }




            setLocation.setOnClickListener {

                nav.navigate(R.id.action_addHubs_to_getLocationPoint)
            }
        }
    }
}