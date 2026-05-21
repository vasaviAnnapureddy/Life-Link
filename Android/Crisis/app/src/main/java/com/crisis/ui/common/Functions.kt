package com.crisis.ui.common

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.text.HtmlCompat
import androidx.core.util.rangeTo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.backPoint() {
    val nav = findNavController()

    requireActivity().onBackPressedDispatcher.addCallback(
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                nav.navigateUp()
            }

        }
    )
}
fun Fragment.homePoint() {
    requireActivity().onBackPressedDispatcher.addCallback(
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().moveTaskToBack(true)
            }

        }
    )


}

fun Fragment.toast(any: Any?) {
    Toast.makeText(requireContext(), "$any", Toast.LENGTH_SHORT).show()
}

fun Activity.toast(any: Any?) {
    Toast.makeText(applicationContext, "$any", Toast.LENGTH_SHORT).show()
}

fun spanned(name: String) = HtmlCompat.fromHtml(
    name, HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
)

fun Activity.checkAndRequest(array: Array<String>, status: (Boolean) -> Unit) {
    var num = 0
    for (element in array) {
        if (ActivityCompat.checkSelfPermission(
                this,
                element
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            num++
        }
    }
    status.invoke(num == array.size)
}

fun Activity.requestPermissionPoint(array: Array<String>) {
        ActivityCompat.requestPermissions(this, array,11)
}