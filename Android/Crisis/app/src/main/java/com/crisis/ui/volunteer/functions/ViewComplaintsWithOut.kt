package com.crisis.ui.volunteer.functions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.crisis.R
import com.crisis.databinding.ViewComplaintBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.viewModels.PointerViewModel
import com.crisis.ui.ngo.adapter.ComplaintAdapter

class ViewComplaintsWithOut : Fragment() {
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
                "Volunteer" -> {
                    models.getCityComplaints(city = city, {
                        cycle4.adapter = ComplaintAdapter(
                            context = requireContext(), array = it,
                            updateOfPoint = { it1 ->
                                val bundle = Bundle()
                                bundle.putParcelable("data", it1)
                                bundle.putString("acceptPoint", "ksjdhfjkdf")
                                nav.navigate(
                                    R.id.action_viewComplaintsWithOut_to_centralFunctionsOfComplaint,
                                    bundle
                                )
                            }
                        )
                    }, "jfkljfdg")
                }

                else -> {}
            }

        }
    }
}