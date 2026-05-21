package com.crisis.ui.volunteer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.crisis.R
import com.crisis.databinding.VolunteerMainBinding
import com.crisis.ui.SingleModule
import com.crisis.ui.admin.adapters.AdminDash
import com.crisis.ui.admin.models.Core
import com.crisis.ui.common.homePoint
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Volunteer : Fragment() {
    private var userBind: VolunteerMainBinding? = null
    private val bind get() = userBind
    private val models by activityViewModels<PointerViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        userBind = VolunteerMainBinding.inflate(layoutInflater)
        return bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homePoint()

        with(bind!!) {
            val nav = findNavController()
            cycle6.layoutManager = GridLayoutManager(requireContext(), 2)
            cycle6.adapter = AdminDash(
                requireActivity(), arrayListOf(
                    Core(name = "View Complaints", resId = R.drawable.list),
                    Core(name = "View Request", resId = R.drawable.accepted),
                    Core(name = "Add Need", resId = R.drawable.add_task),
                    Core(name = "View Safe\nZones", resId = R.drawable.safe),
                    Core(name = "Logout", resId = R.drawable.logout)
                )
            ) {
                when (it) {
                    0 -> {
                        nav.navigate(R.id.action_volunteer_to_viewComplaintsWithOut)
                    }

                    1 -> {
                        nav.navigate(R.id.action_volunteer_to_viewAcceptedComplaints)
                    }
                    2 -> {
                        nav.navigate(R.id.action_volunteer_to_sosComplaints)
                    }

                    3 -> {
                        nav.navigate(R.id.action_volunteer_to_viewSafeZone)
                    }

                    4 -> {
                        dialog()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun dialog() {
        MaterialAlertDialogBuilder(requireActivity()).apply {
            setTitle("Do you want to Logout ?")
            setPositiveButton("Yes") { v, _ ->
                requireActivity().apply {
                    getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().apply()
                    finishAffinity()
                    startActivity(Intent(this, SingleModule::class.java))
                }
                v.dismiss()
            }
            setNegativeButton("No", { v, _ ->
                v.dismiss()
            })
            show()
        }
    }
}