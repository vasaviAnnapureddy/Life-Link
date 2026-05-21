package com.crisis.ui.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.crisis.R
import com.crisis.databinding.SlpashPointBinding

class SlashPoint : Fragment() {
    private val bind by lazy {
        SlpashPointBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
            .getString("typeOf", "")
        with(bind) {
            root.alpha = 0f
            val nav = findNavController()
            root.animate().setDuration(1000).alpha(1f).withEndAction {
                when (type) {
                    "user" -> {
                        nav.navigate(R.id.action_slashPoint_to_userFrag)
                    }

                    "NGO" -> {
                        nav.navigate(R.id.action_slashPoint_to_ngoFrag)
                    }

                    "Volunteer" -> {
                        nav.navigate(R.id.action_slashPoint_to_volunteer)
                    }

                    "admin" -> {
                        nav.navigate(R.id.action_slashPoint_to_admin)

                    }

                    else -> {
                        nav.navigate(R.id.action_slashPoint_to_loginPart)
                    }
                }

            }
        }
    }
}