package com.crisis.ui.admin.functions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.crisis.dataLayer.responses.SosResponse
import com.crisis.databinding.SosPointBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.toast
import com.crisis.ui.common.viewModels.PointerViewModel
import com.crisis.ui.user.adapter.MessageAdapter

class Sos : Fragment() {
    private var userBind: SosPointBinding? = null
    private val bind get() = userBind
    private val models by activityViewModels<PointerViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        userBind = SosPointBinding.inflate(layoutInflater)
        return bind?.root
    }


    @SuppressLint("MissingPermission", "NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPoint()
        with(bind!!) {
            addPoint.isVisible = false
            getData()
        }
    }

    private fun getData() {
        val string = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)

        models.getComplaintsOFMine(
            id = "${string.getString("id", "")}",
            array = { data ->
                val array = arrayListOf<SosResponse.Data>()
                if (string.getString("typeOf", "") == "NGO"
                ) {

                    data.forEach {
                        Log.i("sdjfkdjfkdf", it.typeOF ?: "")
                        if (it.typeOF == "Volunteer") {
                            array.add(it)
                        }
                    }
                } else {
                    data.forEach {
                        if (it.typeOF != "Volunteer") {
                            array.add(it)
                        }
                    }
                }

                bind?.cycle7?.layoutManager = LinearLayoutManager(requireActivity())
                bind?.cycle7?.adapter = MessageAdapter(
                    requireActivity(),
                    array,
                    requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
                )
            }, "ldskfdkjfh", "sddsdf"
        )
    }
}