package com.example.planit_mobile.ui.screens.utils

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Locale

fun addToGoogleCalendarIntent(
    context: Context,
    title: String,
    description: String,
    startDate: String,
    endDate: String,
    location: String?
) {
    val startMillis: Long = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(startDate)?.time ?: 0

    val endMillis: Long =
        if (endDate.isEmpty()) {
        startMillis
    }
        else {
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(endDate)?.time ?: 0
    }
    val intent = Intent(Intent.ACTION_INSERT)
        .setData(CalendarContract.Events.CONTENT_URI)
        .putExtra(CalendarContract.Events.TITLE, title)
        .putExtra(CalendarContract.Events.DESCRIPTION, description)
        .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)

    if (intent.resolveActivity(context.packageManager) != null) {
        ContextCompat.startActivity(context, intent, null)
    } else {
        Toast.makeText(context, "No app found to handle this action", Toast.LENGTH_SHORT).show()
    }

}