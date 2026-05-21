package com.crisis.ui.ngo

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
import com.crisis.databinding.NgoLayoutBinding
import com.crisis.ui.SingleModule
import com.crisis.ui.admin.adapters.AdminDash
import com.crisis.ui.admin.models.Core
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NgoFrag : Fragment() {
    private var userBind: NgoLayoutBinding? = null
    private val bind get() = userBind
    private val models by activityViewModels<PointerViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        userBind = NgoLayoutBinding.inflate(layoutInflater)
        return bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(bind!!) {
            val nav = findNavController()
            cycle3.layoutManager = GridLayoutManager(requireContext(), 2)
            cycle3.adapter = AdminDash(
                requireActivity(), arrayListOf(
                    Core(name = "View Complaints", resId = R.drawable.list),
                    Core(name = "View Request", resId = R.drawable.accepted),
                    Core(name = "Add Volunteer", resId = R.drawable.volunteer),
                    Core(name = "View Resources", resId = R.drawable.complain),
                    Core(name = "View Safe\nZones", resId = R.drawable.safe),
                    Core(name = "View\nVolunteer's", resId = R.drawable.volunteer),
                    Core(name = "Logout", resId = R.drawable.logout)
                )
            ) {
                when (it) {
                    0 -> {
                        nav.navigate(R.id.action_ngoFrag_to_viewComplaintPoint)
                    }

                    1 -> {
                        nav.navigate(R.id.action_ngoFrag_to_viewAcceptedComplaints)
                    }

                    2 -> {
                        nav.navigate(R.id.action_ngoFrag_to_addVolunteer)
                    }

                    3 -> {
                        nav.navigate(R.id.action_ngoFrag_to_sos)
                    }

                    4 -> {
                        nav.navigate(R.id.action_ngoFrag_to_viewSafeZone)
                    }

                    5 -> {
                        val bundle = Bundle()
                        bundle.putString("typeOF", "Volunteer")
                        nav.navigate(R.id.action_ngoFrag_to_viewUsersList, bundle)
                    }

                    6 -> {
                        dialog()
                    }

                    else -> {}
                }
            }
            models.getDashBoardDetails { data ->

                cartesian2.animation.duration = 1000
                val array = ArrayList<Pair<String, Float>>()
                data.forEach {
                    array.add(Pair(it.status + "\n${it.count}", it.count?.toFloat() ?: 0f))
                }
                if (array.size == 1) {
                    array.add(Pair("", 0f))
                }
                cartesian2.animate(
                    array.toList()
                )
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