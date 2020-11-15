package com.example.githatch.helpers

import android.app.Activity
import android.os.Build
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

        fun numberFormatter(number: Int): String {
            if (number > 1000) return "${String.format("%.1f", number.toDouble() / 1000)}k"
            return number.toString()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun dateFormatter(date: String): String {
            val zonedDateTime: ZonedDateTime =
                ZonedDateTime.parse(date).withZoneSameInstant(ZoneId.of("Europe/Vienna"))
            val fmt: DateTimeFormatter =
                DateTimeFormatter.ofPattern("d.MM.yyyy H:mm", Locale.ENGLISH)
            return fmt.format(zonedDateTime)
        }

        fun dateFormatterAlt(date: String): String {
            val zonedDateTime = ZonedDateTime.parse(date).withZoneSameInstant(ZoneId.of("Europe/Vienna"))
            return when (val year = zonedDateTime.year) {
                ZonedDateTime.now().year -> {
                    val fmt: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)
                    fmt.format(zonedDateTime)
                }
                else -> "${ZonedDateTime.now().year - year}y ago"
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