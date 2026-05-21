package com.crisis.ui.admin.models

import androidx.annotation.DrawableRes

data class Core(
    var name: String,
    @DrawableRes var resId: Int,
) {
}