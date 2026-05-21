package com.crisis.ui.admin.functions

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.crisis.databinding.MessageBoxBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.viewModels.PointerViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SendMessages : Fragment() {
    private var messageBoxBinding: MessageBoxBinding? = null
    private val bind get() = messageBoxBinding
    private val models by activityViewModels<PointerViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        messageBoxBinding = MessageBoxBinding.inflate(layoutInflater)
        return bind?.root
    }


    @SuppressLint("MissingPermission", "NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPoint()
        with(bind!!) {
            models.toast.onEach {
                if (it != null) {
                    if (it == "Send") {
                        findNavController().navigateUp()
                    }
                }
            }.launchIn(lifecycleScope)
            models.getCities {
                spinner.adapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, it)
            }

            sendSMS.setOnClickListener {
                val titleMessage = titleMessage.text.toString().trim()
                val description23 = description23.text.toString().trim()
                val selectText = spinner.selectedItem.toString()
                when {
                    titleMessage.isEmpty() -> {
                        models.toast.value = "Please enter your Title"
                    }

                    description23.isEmpty() -> {
                        models.toast.value = "Please enter your Description"
                    }

                    else -> {
                        val text = selectText.split(")")
                        models.sendMessage(
                            title = titleMessage,
                            message = description23,
                            city = text[1],
                            raise = "${System.currentTimeMillis()}"
                        )
                    }
                }
            }
        }
    }
}