package com.example.planit_mobile.ui.screens.calendar

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.services.models.UserEventsResult
import com.example.planit_mobile.ui.screens.common.BackArrow
import com.example.planit_mobile.ui.screens.common.BotBar
import com.example.planit_mobile.ui.screens.common.NavigationHandlers
import com.example.planit_mobile.ui.screens.nearMe.EventCard
import com.example.planit_mobile.ui.screens.utils.addToGoogleCalendarIntent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class CalendarInput(
    val day: Int,
    val month: Int,
    val year: Int,
    val isCurrentMonth: Boolean
)

private const val CALENDAR_ROWS = 6
private const val CALENDAR_COLUMNS = 7
private val weekDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onDayClick: (Int, Int, Int) -> Unit,
    userEvents: UserEventsResult?,
    selectedDay: Calendar,
    currentMonth: Calendar,
    setCurrentMonth: (Calendar) -> Unit,
    onBackRequested: () -> Unit,
    onEventClick: (Int, String) -> Unit
) {
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var daysInMonth by remember { mutableStateOf(getDaysInMonth(currentMonth)) }
    var isInfoDialogShown by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackArrow(Color.White) {
                        onBackRequested()
                    }
                },
                title = {
                    Text(
                        "Calendar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(24, 38, 44, 255)),
                actions = {
                    IconButton(onClick = { isInfoDialogShown = true }) {
                        Icon(Icons.Default.Info, contentDescription = "Information", tint = Color.White)
                    }
                }
            )
        },
    ) {
        if (isInfoDialogShown) {
            AlertDialog(
                onDismissRequest = { isInfoDialogShown = false },
                title = { Text(text = "Color Information") },
                text = {
                    Column {
                        Text(text = "Yellow: 1 event", fontSize = 20.sp, modifier = Modifier.padding(5.dp))
                        Text(text = "Green: 2 events", fontSize = 20.sp, modifier = Modifier.padding(5.dp))
                        Text(text = "Purple: 3+ events", fontSize = 20.sp, modifier = Modifier.padding(5.dp))
                    }
                },
                confirmButton = {
                    TextButton(onClick = { isInfoDialogShown = false }) {
                        Text(
                            text = "OK",
                            fontSize = 23.sp,
                            modifier = Modifier.offset(y = 10.dp)
                        )
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .padding(it)
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    val prevMonth =
                        (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
                    setCurrentMonth(prevMonth)
                    daysInMonth = getDaysInMonth(prevMonth)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Month",
                        tint = Color.White
                    )
                }
                Text(
                    text = getMonthName(currentMonth),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                IconButton(onClick = {
                    val nextMonth =
                        (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
                    setCurrentMonth(nextMonth)
                    daysInMonth = getDaysInMonth(nextMonth)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Month",
                        tint = Color.White
                    )
                }
            }
            Canvas(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .pointerInput(true) {
                        detectTapGestures(
                            onTap = { offset ->
                                val column =
                                    (offset.x / canvasSize.width * CALENDAR_COLUMNS).toInt() + 1
                                val row =
                                    ((offset.y / canvasSize.height * (CALENDAR_ROWS + 1)) + 1).toInt()
                                val dayIndex = column + (row - 2) * CALENDAR_COLUMNS - 1
                                if (dayIndex in daysInMonth.indices) {
                                    val date = daysInMonth[dayIndex]
                                    onDayClick(date.day, date.month, date.year)
                                }
                            }
                        )
                    }
            ) {
                val canvasHeight = size.height
                val canvasWidth = size.width
                canvasSize = Size(canvasWidth, canvasHeight)
                val ySteps = canvasHeight / (CALENDAR_ROWS + 1)
                val xSteps = canvasWidth / CALENDAR_COLUMNS

                drawRoundRect(
                    color = Color(0xFFFFFFFF),
                    cornerRadius = CornerRadius(25f, 25f),
                    style = Stroke(5f),
                    size = canvasSize,
                )

                for (i in 1..(CALENDAR_ROWS)) {
                    drawLine(
                        start = Offset(0f, i * ySteps),
                        end = Offset(canvasWidth, i * ySteps),
                        color = Color(0xFFFFFFFF),
                        strokeWidth = 5f
                    )
                }
                for (i in 1 until CALENDAR_COLUMNS) {
                    drawLine(
                        start = Offset(xSteps * i, 0f),
                        end = Offset(xSteps * i, canvasHeight),
                        color = Color(0xFFFFFFFF),
                        strokeWidth = 5f
                    )
                }

                val textHeight = 19.dp.toPx()

                for (i in weekDays.indices) {
                    val textPositionX = i * xSteps + 13f
                    val textPositionY = ySteps / 2 + textHeight / 2
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            weekDays[i],
                            textPositionX,
                            textPositionY,
                            Paint().apply {
                                textSize = textHeight
                                color = Color.White.toArgb()
                                isFakeBoldText = true
                            }
                        )
                    }
                }
                for (i in daysInMonth.indices) {

                    val eventCount = userEvents?.events?.count { event ->
                        if (event.date == null) return@count false
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        val eventCalendar = Calendar.getInstance()
                        eventCalendar.time = sdf.parse(event.date) ?: return@count false

                        val eventDay = eventCalendar.get(Calendar.DAY_OF_MONTH)
                        val eventMonth = eventCalendar.get(Calendar.MONTH)
                        val eventYear = eventCalendar.get(Calendar.YEAR)

                        eventDay == daysInMonth[i].day &&
                                eventMonth == daysInMonth[i].month &&
                                eventYear == daysInMonth[i].year
                    }
                        ?: 0

                    val rectangleColor = when (eventCount) {
                        1 -> Color(0xFFBDA31D)
                        2 -> Color(0xFF37A837)
                        3 -> Color(0xFF6947A5)
                        else -> Color.Transparent
                    }

                    val rectTopLeft = Offset(
                        (i % CALENDAR_COLUMNS) * xSteps,
                        (i / CALENDAR_COLUMNS + 1) * ySteps
                    )
                    val rectSize = Size(xSteps, ySteps)

                    drawRoundRect(
                        color = rectangleColor,
                        topLeft = rectTopLeft,
                        size = rectSize * 0.99f,
                        style = Fill
                    )

                    val textPositionX = (i % CALENDAR_COLUMNS) * xSteps + 13f
                    val textPositionY = (i / CALENDAR_COLUMNS + 1) * ySteps + 6.5f + textHeight
                    val isToday = daysInMonth[i].isCurrentMonth &&
                            daysInMonth[i].day == Calendar.getInstance()
                        .get(Calendar.DAY_OF_MONTH) &&
                            daysInMonth[i].month == Calendar.getInstance().get(Calendar.MONTH)

                    val isSelectedDay =
                        daysInMonth[i].day == selectedDay.get(Calendar.DAY_OF_MONTH) &&
                                daysInMonth[i].month == selectedDay.get(Calendar.MONTH) &&
                                daysInMonth[i].year == selectedDay.get(Calendar.YEAR) &&
                                daysInMonth[i].isCurrentMonth
                    val textColor = when {
                        isSelectedDay -> Color.Red.toArgb()
                        isToday -> Color(0xFF308EDA).toArgb()
                        daysInMonth[i].isCurrentMonth -> Color.White.toArgb()
                        else -> Color.Gray.toArgb()
                    }
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            if (daysInMonth[i].day != 0) "${daysInMonth[i].day}" else "",
                            textPositionX,
                            textPositionY,
                            Paint().apply {
                                textSize = textHeight
                                color = textColor
                                isFakeBoldText = true
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Events:",
                fontSize = 35.sp,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )
            if (userEvents != null) {
                val eventsForSelectedDay = userEvents.events.filter { event ->
                    if (event.date == null) return@filter false
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val eventCalendar = Calendar.getInstance()
                    eventCalendar.time = sdf.parse(event.date) ?: return@filter false

                    val eventDay = eventCalendar.get(Calendar.DAY_OF_MONTH)
                    val eventMonth = eventCalendar.get(Calendar.MONTH)
                    val eventYear = eventCalendar.get(Calendar.YEAR)

                    eventDay == selectedDay.get(Calendar.DAY_OF_MONTH) &&
                            eventMonth == selectedDay.get(Calendar.MONTH) &&
                            eventYear == selectedDay.get(Calendar.YEAR)
                }
                if (eventsForSelectedDay.isNotEmpty()) {
                    eventsForSelectedDay.forEach { ev ->
                       EventCard(
                           title = ev.title,
                           location = ev.location ?: "No location provided",
                           id = ev.id,
                           onEventClick = { id ->
                               onEventClick(id, ev.visibility)
                           }
                       )
                    }
                } else {
                    Text(
                        text = "No events found",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .offset(x = 90.dp, y = 90.dp),
                        color = Color.White
                    )
                }
            }
        }
    }
}

fun getMonthName(calendar: Calendar): String {
    return SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
}

fun getDaysInMonth(calendar: Calendar): List<CalendarInput> {
    val days = mutableListOf<CalendarInput>()
    val tempCalendar = calendar.clone() as Calendar

    // Get previous month's days
    tempCalendar.add(Calendar.MONTH, -1)
    val prevMonthDays = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val currentMonthFirstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    for (i in prevMonthDays - currentMonthFirstDayOfWeek + 2..prevMonthDays) {
        days.add(
            CalendarInput(
                i,
                tempCalendar.get(Calendar.MONTH),
                tempCalendar.get(Calendar.YEAR),
                false
            )
        )
    }

    // Get current month's days
    tempCalendar.add(Calendar.MONTH, 1)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    for (i in 1..daysInMonth) {
        days.add(
            CalendarInput(
                i,
                tempCalendar.get(Calendar.MONTH),
                tempCalendar.get(Calendar.YEAR),
                true
            )
        )
    }

    // Get next month's days
    tempCalendar.add(Calendar.MONTH, 1)
    val totalCells = CALENDAR_ROWS * CALENDAR_COLUMNS
    val remainingCells = totalCells - days.size

    for (i in 1..remainingCells) {
        days.add(
            CalendarInput(
                i,
                tempCalendar.get(Calendar.MONTH),
                tempCalendar.get(Calendar.YEAR),
                false
            )
        )
    }

    return days
}




