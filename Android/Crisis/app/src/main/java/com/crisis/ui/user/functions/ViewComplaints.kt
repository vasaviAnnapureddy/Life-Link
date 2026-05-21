package com.crisis.ui.user.functions

import android.annotation.SuppressLint
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

class ViewComplaints : Fragment() {
    private var complaintBind: ViewComplaintBinding? = null
    private val bind get() = complaintBind
    private val models by activityViewModels<PointerViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        complaintBind = ViewComplaintBinding.inflate(layoutInflater)
        return bind?.root
    }

    private val shared by lazy {
        requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("id", "")
    }

    @SuppressLint("Recycle", "MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPoint()
        models.viewComplaintsOfUsers(shared ?: "") {
            with(bind!!) {
                val bundle = Bundle()
                cycle4.layoutManager = LinearLayoutManager(requireActivity())

                cycle4.adapter = ComplaintAdapter(requireActivity(), it) {
                    bundle.putParcelable("data", it)
                    findNavController().navigate(
                        R.id.action_viewComplaint_to_centralFunctionsOfComplaint,
                        bundle
                    )

                }
            }
        }

    }


}
















