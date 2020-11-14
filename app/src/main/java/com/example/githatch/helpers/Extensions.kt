package com.example.githatch.helpers

import android.content.Context
import android.view.View
import android.widget.Toast

val Context.pixelDensity: Float
    get() = resources.displayMetrics.density

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun View.visible(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}
