package com.example.planit_mobile.ui.screens.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun endDatePickerDialog(
    context: Context,
    startDateTime: String,
    endDateTime: (String) -> Unit,
    calendar: Calendar,
    dateFormat: SimpleDateFormat
): DatePickerDialog {
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedEndDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            val startDate = Calendar.getInstance().apply {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                time = sdf.parse(startDateTime)!!
            }
            if (selectedEndDate.get(Calendar.DAY_OF_YEAR) < startDate.get(Calendar.DAY_OF_YEAR) ||
                selectedEndDate.get(Calendar.YEAR) < startDate.get(Calendar.YEAR)
            ) {
                Toast.makeText(context, "End date must be after start date", Toast.LENGTH_SHORT)
                    .show()
                endDateTime("")
            } else {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        val selectedTime = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }
                        val startDateHourMinute = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, startDate.get(Calendar.HOUR_OF_DAY))
                            set(Calendar.MINUTE, startDate.get(Calendar.MINUTE))
                        }
                        if (
                            (selectedEndDate.get(Calendar.DAY_OF_YEAR) == startDate.get(Calendar.DAY_OF_YEAR) &&
                                    selectedEndDate.get(Calendar.YEAR) == startDate.get(Calendar.YEAR) &&
                                    selectedTime.timeInMillis <= startDateHourMinute.timeInMillis)
                        ) {
                            Toast.makeText(
                                context,
                                "End date must be after start date",
                                Toast.LENGTH_SHORT
                            ).show()
                            endDateTime("")
                        } else {
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            endDateTime(dateFormat.format(calendar.time))
                        }
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                if (selectedEndDate.get(Calendar.DAY_OF_YEAR) == startDate.get(Calendar.DAY_OF_YEAR) &&
                    selectedEndDate.get(Calendar.YEAR) == startDate.get(Calendar.YEAR)
                ) {
                    timePickerDialog.updateTime(
                        startDate.get(Calendar.HOUR_OF_DAY), startDate.get(
                            Calendar.MINUTE
                        )
                    )
                }
                timePickerDialog.show()
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    return datePickerDialog
}