package com.example.githatch.helpers

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Helper {
    companion object {
        fun hideKeyboard(activity: Activity) {
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun textFormatter(text: String, start: Int, colorString: String): SpannableStringBuilder {
            val viewsText = text
            val ssb = SpannableStringBuilder(viewsText)
            val fcs =
                ForegroundColorSpan(Color.parseColor(colorString))
            ssb.setSpan(fcs, start, viewsText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return ssb
        }

        fun numberFormatter(number: Int): String {
            if (number > 1000) {
                return "${String.format("%.1f", number.toDouble() / 1000)}k"
            }
            return number.toString()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun dateFormatter(date: String): String {
            val parsed: ZonedDateTime = ZonedDateTime.parse(date)
            val z = parsed.withZoneSameInstant(ZoneId.of("Europe/Vienna"))
            val fmt: DateTimeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy H:mm", Locale.ENGLISH)
            return fmt.format(z)
        }

        fun dateFormatterAlt(date: String): String {
            val parsed: ZonedDateTime = ZonedDateTime.parse(date)
            val z = parsed.withZoneSameInstant(ZoneId.of("Europe/Vienna"))
            val year = z.year
            when (year) {
                ZonedDateTime.now().year -> {
                    val fmt: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("d.MM", Locale.ENGLISH)
                    return fmt.format(z)
                }
                else -> return "~${ZonedDateTime.now().year - year}y ago"
            }
        }
    }

    internal enum class SortBy {
        stars, forks, updated;
    }

    internal enum class OrderBy {
        asc, desc;
    }
}