package com.example.planit_mobile.ui.screens.common

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())

    val date = inputFormat.parse(dateString)
    return if (date != null) {
        outputFormat.format(date)
    } else {
        "Invalid date"
    }
}