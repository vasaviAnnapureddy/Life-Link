package com.crisis.ui.admin

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
import com.crisis.databinding.AdminPartBinding
import com.crisis.ui.SingleModule
import com.crisis.ui.admin.adapters.AdminDash
import com.crisis.ui.admin.models.Core
import com.crisis.ui.common.viewModels.PointerViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Admin : Fragment() {
    private var loginBind: AdminPartBinding? = null
    private val bind get() = loginBind
    private val models by activityViewModels<PointerViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        loginBind = AdminPartBinding.inflate(layoutInflater)
        return bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nav = findNavController()
        with(bind!!) {
            arrayListOf<Core>().let {
                it.add(Core(name = "Add Ngo", resId = R.drawable.user_2))
                it.add(Core(name = "Send Emergency", resId = R.drawable.send_svg))
                it.add(Core(name = "SOS", resId = R.drawable.alarm))
                it.add(Core(name = "Add Safe\nhubs", resId = R.drawable.safe))
                it.add(Core(name = "AI Crisis\nAgent", resId = R.drawable.ai_bot))
                it.add(Core(name = "View\nCompensation\nRequests", resId = R.drawable.complain))
                it.add(Core(name = "View\nVolunteer's", resId = R.drawable.volunteer))
                it.add(Core(name = "View NGO's", resId = R.drawable.ngo))
                it.add(Core(name = "View Safe\nZones", resId = R.drawable.safe))
                it.add(Core(name = "Logout", resId = R.drawable.logout))

                val grid = GridLayoutManager(requireContext(), 2)

                recyclerView.layoutManager = grid

                recyclerView.adapter = AdminDash(
                    requireContext(), it
                ) { it1 ->
                    when (it1) {
                        0 -> {
                            nav.navigate(R.id.action_admin_to_addNgo)
                        }

                        1 -> {
                            nav.navigate(R.id.action_admin_to_sendMessages)
                        }

                        2 -> {
                            nav.navigate(R.id.action_admin_to_sos)
                        }

                        3 -> {
                            nav.navigate(R.id.action_admin_to_addHubs2)
                        }

                        4 -> {
                            nav.navigate(R.id.action_admin_to_aiReport)
                        }

                        5 -> {
                            nav.navigate(R.id.action_admin_to_videoComplaints)
                        }

                        6 -> {
                            val bundle = Bundle()
                            bundle.putString("typeOF", "Volunteer")
                            nav.navigate(R.id.action_admin_to_viewUsersList, bundle)
                        }

                        7 -> {
                            val bundle = Bundle()
                            bundle.putString("typeOF", "NGO")
                            nav.navigate(R.id.action_admin_to_viewUsersList, bundle)
                        }

                        8 -> {
                            nav.navigate(R.id.action_admin_to_viewSafeZone)
                        }

                        9 -> {
                            dialog()
                        }

                        else -> {
                            return@AdminDash
                        }
                    }

                }

            }

            models.getDashBoardDetails { data ->

                cartesian.animation.duration = 1000
                val array = ArrayList<Pair<String, Float>>()
                data.forEach {
                    array.add(Pair(it.status + "\n${it.count}", it.count?.toFloat() ?: 0f))
                }
                if (array.size == 1) {
                    array.add(Pair("", 0f))
                }
                cartesian.animate(
                    array.toList()
                )
                if (data.size != 0) {
                    val bundle = Bundle()
                    ima.setOnClickListener { _ ->
                        bundle.putString("something", "nothing")
                        nav.navigate(R.id.action_admin_to_locationPoint, bundle)
                    }
                }
            }


        }
    }

    private val shared by lazy {
        requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
    }


    private fun dialog() {
        MaterialAlertDialogBuilder(requireActivity()).apply {
            setTitle("Do you want to Logout ?")
            setPositiveButton("Yes", { v, _ ->
                requireActivity().apply {
                    shared.edit().clear().apply()
                    finishAffinity()
                    startActivity(Intent(this, SingleModule::class.java))
                }
                v.dismiss()
            })
            setNegativeButton("No", { v, _ ->
                v.dismiss()
            })
            show()
        }
    }

}