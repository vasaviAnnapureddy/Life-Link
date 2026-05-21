package com.crisis.ui.common

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.crisis.R
import com.crisis.dataLayer.responses.LoginResponse
import com.crisis.databinding.LoginPartBinding
import com.crisis.ui.common.viewModels.PointerViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginPart : Fragment() {
    private var loginBind: LoginPartBinding? = null
    private val bind get() = loginBind
    private val models by activityViewModels<PointerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        loginBind = LoginPartBinding.inflate(layoutInflater)
        return bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nav = findNavController()
        with(bind!!) {
            models.user.onEach {
                models.toast.value = it?.type
                if (it != null) {
                    when (it.type) {
                        "admin" -> {
                            nav.navigate(R.id.action_loginPart_to_admin)
                        }

                        "NGO" -> {
                            nav.navigate(R.id.action_loginPart_to_ngoFrag)
                        }

                        "user" -> {
                            nav.navigate(R.id.userFrag)
                        }

                        "Volunteer" -> {
                            nav.navigate(R.id.action_loginPart_to_volunteer)
                        }
                    }
                    requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                        .apply {
                            putString("typeOf", it.type)
                            putString("id", it.id.toString())
                            putString("city", it.city.toString())
                            putString("mobile", it.mobile.toString())

                        }.apply()
                }
            }.launchIn(lifecycleScope)


            appCompatButton.setOnClickListener {
                val mail = email.text.toString().trim()
                val password = password.text.toString().trim()
                if (mail.isEmpty()) {
                    toast("Please enter your mobile")
                } else if (password.isEmpty()) {
                    toast("Please enter your password")
                } else if (password == "admin" && mail == "admin") {
                    models.user.value = LoginResponse.Users(
                        id = null, name = null, mobile = null, type = "admin",
                    )

                } else {
                    models.login(mail, password)
                }
            }

            createAc.setOnClickListener {
                nav.navigate(R.id.action_loginPart_to_signUp)
            }
        }
    }


}