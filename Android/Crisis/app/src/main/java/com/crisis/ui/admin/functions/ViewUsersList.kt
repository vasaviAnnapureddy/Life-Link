package com.crisis.ui.admin.functions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.crisis.databinding.UserListLayoutBinding
import com.crisis.ui.admin.adapters.UserListAdapter
import com.crisis.ui.common.viewModels.PointerViewModel

class ViewUsersList : Fragment() {

    private var _bind: UserListLayoutBinding? = null
    private val bind get() = _bind!!
    private val models by activityViewModels<PointerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _bind = UserListLayoutBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typeOF = arguments?.getString("typeOF") ?: "NGO"
        bind.listTitle.text = if (typeOF == "NGO") "NGO List" else "Volunteer List"
        bind.backBtn.setOnClickListener { findNavController().navigateUp() }

        bind.userRecycler.layoutManager = LinearLayoutManager(requireContext())

        models.viewUsersByType(typeOF) { users ->
            if (users.isEmpty()) {
                bind.emptyText.isVisible = true
                bind.userRecycler.isVisible = false
            } else {
                bind.emptyText.isVisible = false
                bind.userRecycler.isVisible = true
                bind.userRecycler.adapter = UserListAdapter(requireContext(), users)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}
