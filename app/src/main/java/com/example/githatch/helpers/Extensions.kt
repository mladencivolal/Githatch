package com.example.githatch.helpers

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

@ColorInt
fun Context.getColorCompat(@ColorRes resourceId: Int) = ContextCompat.getColor(this, resourceId)

fun Context.getDrawableCompat(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)

fun Context.getDimension(@DimenRes resourceId: Int) = resources.getDimension(resourceId)

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.pixelDensity: Float
    get() = resources.displayMetrics.density

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun View.visible(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}


fun Context.getPxFromDp(dp: Float) = TypedValue
    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()