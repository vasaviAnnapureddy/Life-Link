package com.crisis.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.crisis.R
import com.crisis.ui.common.toast
import com.crisis.ui.common.viewModels.PointerViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SingleModule : AppCompatActivity() {
    private val model by viewModels<PointerViewModel>()

    private val dialog by lazy {
        Dialog(this).apply {
            setContentView(R.layout.progress_dialog)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_module)


        model.dialog.onEach {
            if (it) dialog.show() else dialog.dismiss()
        }.launchIn(lifecycleScope)


        model.toast.onEach {
            if (it != null) {
                Log.i("SomethingPoint", "$it")
                toast(it)
                model.setDialog()
            }
        }.launchIn(lifecycleScope)


    }
}