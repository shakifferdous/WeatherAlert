package com.shakif.weatheralertapp.utility

import android.view.View

fun View.disableView(viewAlpha: Float = 0.5f) {
    alpha = viewAlpha
    isClickable = false
    isEnabled = false
}

fun View.enableView() {
    alpha = 1f
    isClickable = true
    isEnabled = true
}