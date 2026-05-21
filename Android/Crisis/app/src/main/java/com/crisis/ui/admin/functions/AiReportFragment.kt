package com.crisis.ui.admin.functions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.crisis.databinding.AiReportLayoutBinding
import com.crisis.ui.common.viewModels.PointerViewModel

class AiReportFragment : Fragment() {

    private var _bind: AiReportLayoutBinding? = null
    private val bind get() = _bind!!
    private val models by activityViewModels<PointerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _bind = AiReportLayoutBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button
        bind.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        // Call AI agent
        models.getAiCrisisReport { result ->
            requireActivity().runOnUiThread {
                bind.loadingLayout.isVisible = false

                if (result.error == true || result.report == null) {
                    bind.errorCard.isVisible = true
                    val msg = result.message ?: "No response from server"
                    bind.errorText.text = when {
                        msg.contains("key", ignoreCase = true) ->
                            "Gemini API key not configured.\nOpen settings.py and set GEMINI_API_KEY."
                        msg.contains("503") || msg.contains("overload", ignoreCase = true) ->
                            "Gemini is busy right now.\nPlease try again in 30 seconds."
                        msg.contains("timeout", ignoreCase = true) || msg.contains("No response") ->
                            "Server timeout. Make sure Django is running\non your laptop on the same WiFi."
                        else -> "Error: $msg"
                    }
                } else {
                    // Stats
                    result.stats?.let { s ->
                        bind.statTotal.text   = s.total.toString()
                        bind.statPending.text = s.pending.toString()
                        bind.statActive.text  = s.active.toString()
                        bind.statDone.text    = s.completed.toString()
                    }
                    bind.reportCard.isVisible = true
                    bind.reportText.text = result.report
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}
