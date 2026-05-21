package com.crisis.ui.commonPath

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.crisis.R
import com.crisis.dataLayer.ApiCall
import com.crisis.dataLayer.responses.ComplaintResponse
import com.crisis.databinding.CentralFunctionsBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.toast
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CentralFunctionsOfComplaint : Fragment() {
    private var userBind: CentralFunctionsBinding? = null
    private val bind get() = userBind
    private val models by activityViewModels<PointerViewModel>()


    private val fused by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        userBind = CentralFunctionsBinding.inflate(layoutInflater)
        return bind?.root
    }

    private val url = ApiCall.BASEURL

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPoint()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("data", ComplaintResponse.Data::class.java)
        } else {
            arguments?.getParcelable("data")
        }?.let {
            val nav = findNavController()
            with(bind!!) {
                imageHead.load(url + it.imagePath)
                val bundle = Bundle()
                locationPointView.setOnClickListener { _ ->
                    bundle.putString("latlon", it.latLon)
                    nav.navigate(R.id.action_centralFunctionsOfComplaint_to_locationPoint, bundle)
                }
                statusOF.text = HtmlCompat.fromHtml(
                    "Complaint Status :" + it.status, HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
                )

                when (shared.getString("typeOf", "")) {
                    "user" -> {
                        userLayout.isVisible = false
                        vLayout.isVisible = true
                        ngoLayout.isVisible = true
                        models.getUserDetails(id = it.volunteerId ?: "", user = { arrow ->
                            val name = "<big>Volunteer Details</big><br>" +
                                    "${arrow?.name}<br>" +
                                    "${arrow?.mail}"

                            vDetails.text = HtmlCompat.fromHtml(
                                name,
                                HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
                            )
                            vCall.isVisible = true
                            vCall.setOnClickListener { _ ->
                                requireActivity().startActivity(
                                    Intent(
                                        Intent.ACTION_DIAL,
                                        Uri.parse("tel:${arrow?.mobile}")
                                    )
                                )
                            }
                        })
                        models.getUserDetails(id = it.ngoid ?: "", user = { arrow ->
                            val name = "<big>NGO Details</big><br>" +
                                    "${arrow?.name}<br>" +
                                    "${arrow?.mail}"

                            ngoDetails.text = HtmlCompat.fromHtml(
                                name,
                                HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
                            )
                            ngoCall.isVisible = false
                            ngoCall.setOnClickListener { _ ->
                                requireActivity().startActivity(
                                    Intent(
                                        Intent.ACTION_DIAL,
                                        Uri.parse("tel:${arrow?.mobile}")
                                    )
                                )
                            }
                        })

                    }

                    "NGO" -> {
                        userLayout.isVisible = true
                        vLayout.isVisible = true
                        ngoLayout.isVisible = false


                        models.getUserDetails(id = it.volunteerId ?: "", user = { arrow ->
                            if (arrow != null) {
                                val name = "<big>Volunteer Details</big><br>" +
                                        "${arrow.name}<br>" +
                                        "${arrow.mail}"

                                vDetails.text = HtmlCompat.fromHtml(
                                    name,
                                    HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
                                )
                                vCall.isVisible = true
                                vCall.setOnClickListener { _ ->
                                    requireActivity().startActivity(
                                        Intent(
                                            Intent.ACTION_DIAL,
                                            Uri.parse("tel:${arrow?.mobile}")
                                        )
                                    )
                                }
                            }

                        })
                        toast(it.user_id)
                        models.getUserDetails(id = it.user_id ?: "", user = { arrow ->
                            if (arrow != null) {

                                val name = "<big>User Details</big><br>" +
                                        "${arrow.name}<br>" +
                                        "${arrow.mail}"

                                uDetails.text = HtmlCompat.fromHtml(
                                    name,
                                    HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
                                )
                                uCall.isVisible = true
                                uCall.setOnClickListener { _ ->
                                    requireActivity().startActivity(
                                        Intent(
                                            Intent.ACTION_DIAL,
                                            Uri.parse("tel:${arrow?.mobile}")
                                        )
                                    )
                                }
                            }

                        })

                        arguments?.getString("acceptPoint", null)?.let { dfjk ->

                            acceptPoint.isVisible = true

                            acceptPoint.setOnClickListener { _ ->

                                models.getUpdateComplaint(
                                    userid = shared.getString(
                                        "id",
                                        ""
                                    ) ?: "", complaintID = it.id.toString()
                                )
                            }
                        }


                    }

                    "Volunteer" -> {
                        userLayout.isVisible = true
                        ngoLayout.isVisible = true
                        vLayout.isVisible = false

                        models.getUserDetails(id = it.ngoid ?: "", user = { arrow ->
                            if (arrow != null) {
                                val name = "<big>NGO Details</big><br>" +
                                        "${arrow.name}<br>" +
                                        "${arrow.mail}"

                                ngoDetails.text = HtmlCompat.fromHtml(
                                    name,
                                    HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
                                )
                                ngoCall.isVisible = true
                                ngoCall.setOnClickListener { _ ->
                                    requireActivity().startActivity(
                                        Intent(
                                            Intent.ACTION_DIAL,
                                            Uri.parse("tel:${arrow?.mobile}")
                                        )
                                    )
                                }
                            }

                        })
                        models.getUserDetails(id = it.user_id ?: "", user = { arrow ->
                            if (arrow != null) {
                                val name = "<big>User Details</big><br>" +
                                        "${arrow.name}<br>" +
                                        "${arrow.mail}"

                                uDetails.text = HtmlCompat.fromHtml(
                                    name,
                                    HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
                                )
                                uCall.isVisible = true
                                uCall.setOnClickListener { _ ->
                                    requireActivity().startActivity(
                                        Intent(
                                            Intent.ACTION_DIAL,
                                            Uri.parse("tel:${arrow.mobile}")
                                        )
                                    )
                                }
                            }
                        })

                        arguments?.getString("acceptPoint", null).let { sdf ->
                            acceptPoint2.isVisible = true
                            arguments?.getString("something")?.let { _ ->
                                acceptPoint2.isVisible = false
                                completed.isVisible = it.status != "Completed"

                                completed.setOnClickListener { _ ->

                                    MaterialAlertDialogBuilder(requireActivity()).apply {
                                        setMessage("Are you Completed this Task ?")
                                        setPositiveButton("Yes") { p, _ ->
                                            p.dismiss()

                                            models.completedComplaint(it.id ?: "")
                                        }
                                        setNegativeButton("No") { v, _ ->
                                            v.dismiss()
                                        }
                                        show()
                                    }
                                }

                            }

                            acceptPoint2.setOnClickListener { _ ->
                                models.getUpdateComplaintVolunteer(
                                    userid = shared.getString(
                                        "id",
                                        ""
                                    ) ?: "",
                                    complaintID = it.id.toString()
                                )
                            }
                        }
                    }
                    else -> {}
                }


            }

        }


    }

    private val shared by lazy {
        requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
    }
}