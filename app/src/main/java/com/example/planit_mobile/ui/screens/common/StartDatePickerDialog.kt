package com.example.planit_mobile.ui.screens.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun startDatePickerDialog(
    context: Context,
    calendar: Calendar,
    startDateTime: (String) -> Unit,
    dateFormat: SimpleDateFormat
): DatePickerDialog {
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            val currentDate = Calendar.getInstance()
            if (selectedDate.before(currentDate)) {
                Toast.makeText(context, "Start Date must be after current date", Toast.LENGTH_SHORT)
                    .show()
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
                        if (selectedDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR) &&
                            selectedDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                            selectedTime.before(currentDate)
                        ) {
                            Toast.makeText(
                                context,
                                "Start Date must be after current date",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            startDateTime(dateFormat.format(calendar.time))
                        }
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                if (selectedDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR) &&
                    selectedDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
                ) {
                    timePickerDialog.updateTime(
                        currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(
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