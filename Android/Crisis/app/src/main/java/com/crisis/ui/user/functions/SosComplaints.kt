package com.crisis.ui.user.functions

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.crisis.databinding.SosDialogBinding
import com.crisis.databinding.SosPointBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.toast
import com.crisis.ui.common.viewModels.PointerViewModel
import com.crisis.ui.user.adapter.MessageAdapter
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SosComplaints : Fragment() {
    private var complaintBind: SosPointBinding? = null
    private val bind get() = complaintBind
    private val models by activityViewModels<PointerViewModel>()

    private val fused by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val sosBind by lazy {
        SosDialogBinding.inflate(layoutInflater)
    }
    private val sosDialog by lazy {
        Dialog(requireActivity()).apply {
            setContentView(sosBind.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private val id by lazy {
        requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("id", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        complaintBind = SosPointBinding.inflate(layoutInflater)
        return bind?.root
    }

    @SuppressLint("Recycle", "MissingPermission", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPoint()
        val string = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
            .getString("typeOf", "")
        with(bind!!) {
            models.toast.onEach {
                if (it == "Success") {
                    sosDialog.dismiss()
                    getData()
                }
            }.launchIn(lifecycleScope)

            addPoint.setOnClickListener {
                sosDialog.show()
            }
            if (string == "Volunteer") {
                textView8.text = "Add Requirements"
            }
            sosBind.sendBtn.setOnClickListener {
                val sos = sosBind.descriptionPoint.text.toString().trim()
                if (sos.isEmpty()) {
                    toast("Please enter your Description")
                } else {
                    fused.lastLocation.addOnSuccessListener {
                        if (it != null) {
                            models.sendSos(
                                message = sos,
                                userid = id ?: "",
                                latLon = "${it.latitude},${it.longitude}", user = string?:""
                            )
                        }
                    }
                }
            }
        }
        getData()
    }

    private fun getData() {
        models.getComplaintsOFMine(
            id = "$id",
            array = {
                bind?.cycle7?.layoutManager = LinearLayoutManager(requireActivity())
                bind?.cycle7?.adapter = MessageAdapter(requireActivity(), it)
            }, type = "User"
        )
    }


}