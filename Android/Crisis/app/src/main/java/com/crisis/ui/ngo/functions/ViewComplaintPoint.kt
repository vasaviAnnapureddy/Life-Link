package com.crisis.ui.ngo.functions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.crisis.R
import com.crisis.databinding.ViewComplaintBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.viewModels.PointerViewModel
import com.crisis.ui.ngo.adapter.ComplaintAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ViewComplaintPoint : Fragment() {
    private var userBind: ViewComplaintBinding? = null
    private val bind get() = userBind

    private val models by activityViewModels<PointerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        userBind = ViewComplaintBinding.inflate(layoutInflater)
        return bind?.root
    }

    private val city by lazy {
        requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("city", "")
            ?: ""
    }

    private val shared by lazy {
        requireActivity().getSharedPreferences(
            "user",
            Context.MODE_PRIVATE
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(bind!!) {
            backPoint()
            val nav = findNavController()
            cycle4.layoutManager = LinearLayoutManager(requireContext())

            when (shared.getString("typeOf", "")) {

                "NGO" -> {
                    models.getCityComplaints(city = city, {
                        cycle4.adapter = ComplaintAdapter(
                            context = requireContext(), array = it,
                            updateOfPoint = { it1 ->
                                val bundle = Bundle()
                                bundle.putParcelable("data", it1)
                                bundle.putString("acceptPoint", "kjhjh")

                                nav.navigate(
                                    R.id.action_viewComplaintPoint_to_centralFunctionsOfComplaint,
                                    bundle
                                )
                            }
                        )
                    })
                }

                "Volunteer" -> {
                    models.viewComplaintsOfUsers(shared.getString("id", "") ?: "") {
                        cycle4.adapter = ComplaintAdapter(
                            context = requireContext(), array = it,
                            updateOfPoint = { it1 ->
                                val bundle = Bundle()
                                bundle.putParcelable("data", it1)
                                bundle.putString("acceptPoint", "skdfjkfd")
                                nav.navigate(
                                    R.id.action_viewAcceptedComplaints_to_centralFunctionsOfComplaint,
                                    bundle
                                )
                            },
                        )
                    }
                }

                else -> {}
            }
            models.complaints.onEach {
                cycle4.adapter = ComplaintAdapter(
                    context = requireContext(), array = it,
                    updateOfPoint = { it1 ->
                        val bundle = Bundle()
                        bundle.putParcelable("data", it1)
                        nav.navigate(
                            R.id.action_viewComplaintPoint_to_centralFunctionsOfComplaint,
                            bundle
                        )
                    },
                )
            }.launchIn(lifecycleScope)
        }
    }
}