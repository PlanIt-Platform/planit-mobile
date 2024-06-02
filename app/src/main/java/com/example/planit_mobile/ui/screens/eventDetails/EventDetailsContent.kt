package com.example.planit_mobile.ui.screens.eventDetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.services.models.EventModel
import com.example.planit_mobile.ui.screens.common.buildAnnotatedString
import com.example.planit_mobile.ui.screens.common.formatDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun EventDetailsContent(
    eventDetails: EventModel,
    isUserInEvent: Boolean,
    isUserOrganizer: Boolean,
    leaveEvent: () -> Unit,
    editEvent: (
        String, String?, String, String?, String?, String, String, String?, String, String
    ) -> Unit,
    deleteEvent: () -> Unit,
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    subCategories: List<String>
) {

    var editMode by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(eventDetails.title) }
    var description by remember { mutableStateOf(eventDetails.description) }
    var category by remember { mutableStateOf(eventDetails.category) }
    var subCategory by remember { mutableStateOf(eventDetails.subcategory) }
    var location by remember { mutableStateOf(eventDetails.location) }
    var visibility by remember { mutableStateOf(eventDetails.visibility) }
    var amount by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var catExpanded by remember { mutableStateOf(false) }
    var subCatExpanded by remember { mutableStateOf(false) }
    var visibilityExpanded by remember { mutableStateOf(false) }

    var startDateTime by remember { mutableStateOf(eventDetails.date.dropLast(3)) }
    val endDate = if (eventDetails.endDate != null) eventDetails.endDate.dropLast(3) else ""
    var endDateTime by remember { mutableStateOf(endDate) }
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    var passwordVisibility by remember { mutableStateOf(false) }

    val startDatePickerDialog = DatePickerDialog(
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
                            startDateTime = dateFormat.format(calendar.time)
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

    startDatePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

    val endDatePickerDialog = DatePickerDialog(
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
                endDateTime = ""
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
                            endDateTime = ""
                        } else {
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            endDateTime = dateFormat.format(calendar.time)
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

    endDatePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

    Box(
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {

                //Title
                DetailsTexts(
                    "Title",
                    title,
                    editMode,
                    true,
                    MaterialTheme.typography.displayMedium,
                    Color.White
                ) { t ->
                    title = t
                }

                Spacer(modifier = Modifier.height(10.dp))

                //Description
                DetailsTexts(
                    "Description",
                    if (description != "") description
                    else "No description available",
                    editMode,
                ) { d ->
                    description = d
                }

                //Visibility
                if (!editMode) {
                    DetailsTexts(
                        "Visibility: ",
                        visibility,
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .border(1.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                            .background(Color.LightGray, RoundedCornerShape(4.dp))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(visibility, modifier = Modifier.padding(start = 7.dp))
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { visibilityExpanded = true }) {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Select visibility"
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = visibilityExpanded,
                            onDismissRequest = { visibilityExpanded = false }) {
                            DropdownMenuItem(
                                text = { Text("Public") },
                                onClick = {
                                    visibility = "Public"
                                    password = ""
                                    visibilityExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Private") },
                                onClick = {
                                    visibility = "Private"
                                    visibilityExpanded = false
                                }
                            )

                        }
                    }
                }

                //Date
                Row {
                    if (!editMode) {
                        DetailsTexts("Date: ", formatDate(eventDetails.date))
                    } else {
                        Column {
                            TextField(
                                value = startDateTime,
                                onValueChange = { startDateTime = it },
                                label = { Text("Start Date *") },
                                modifier = Modifier
                                    .padding(2.dp)
                                    .fillMaxWidth(0.8f),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { startDatePickerDialog.show() }) {
                                        Icon(
                                            Icons.Default.DateRange,
                                            contentDescription = "Pick Date"
                                        )
                                    }
                                }
                            )
                            if (startDateTime.isEmpty()) {
                                Text(
                                    text = "(Start Date must be after current date)",
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }

                //End Date
                if (!editMode) {
                    DetailsTexts(
                        "End Date",
                        if (eventDetails.endDate != null) formatDate(eventDetails.endDate) else ""
                    )
                } else {
                    Column {
                        TextField(
                            value = endDateTime,
                            onValueChange = { endDateTime = it },
                            label = { Text("End Date") },
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth(0.8f),
                            readOnly = true,
                            enabled = startDateTime.isNotEmpty(),
                            trailingIcon = {
                                IconButton(onClick = { if (startDateTime.isNotEmpty()) endDatePickerDialog.show() }) {
                                    Icon(
                                        Icons.Default.DateRange,
                                        contentDescription = "Pick Date"
                                    )
                                }
                            }
                        )
                        if (endDateTime.isEmpty()) {
                            Text(
                                text = "(End Date must be after Start Date)",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                //Location
                DetailsTexts("Location", location, editMode) { l ->
                    location = l
                }

                //Category
                if (!editMode) {
                    DetailsTexts("Category", eventDetails.category)
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .border(1.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                            .background(Color.LightGray, RoundedCornerShape(4.dp))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(category, modifier = Modifier.padding(start = 7.dp))
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { catExpanded = true }) {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Category"
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = catExpanded,
                            onDismissRequest = { catExpanded = false },
                            modifier = Modifier.width(200.dp)
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        category = cat
                                        catExpanded = false
                                        if (cat != "Simple Meeting") {
                                            onCategorySelected(cat)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                //Subcategory
                if (!editMode) {
                    DetailsTexts("Subcategory", eventDetails.subcategory)
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .border(1.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                            .background(Color.LightGray, RoundedCornerShape(4.dp))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                subCategory ?: "",
                                modifier = Modifier.padding(start = 7.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { subCatExpanded = true }) {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Select subcategory"
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = subCatExpanded,
                            onDismissRequest = { subCatExpanded = false }
                        ) {
                            if (category.isNotEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("") },
                                    onClick = {
                                        subCategory = ""
                                        subCatExpanded = false
                                    }
                                )
                                subCategories.forEach { subCat ->
                                    DropdownMenuItem(
                                        text = { Text(subCat) },
                                        onClick = {
                                            if (category.isNotEmpty()) {
                                                subCategory = subCat
                                                subCatExpanded = false
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                //Price
                if (!editMode) {
                    if (eventDetails.priceAmount != null && eventDetails.priceCurrency != null) {
                        DetailsTexts(
                            "Price: ",
                            "${eventDetails.priceAmount}${eventDetails.priceCurrency}"
                        )
                    }
                } else {
                    Column {
                        Text(
                            text = "Price",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(modifier = Modifier.fillMaxWidth(0.8f)) {
                            // Amount Field
                            TextField(
                                value = amount,
                                onValueChange = {
                                    if ((it.toDoubleOrNull() != null && it.toDouble() > 0 && it.matches(
                                            "^-?\\d*(\\.\\d{0,2})?$".toRegex()
                                        )) || it.isEmpty() || it == ""
                                    ) {
                                        amount = it
                                    }
                                },
                                label = { Text("Amount") },
                                modifier = Modifier
                                    .weight(0.55f)
                                    .padding(2.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            // Currency Field
                            TextField(
                                value = currency,
                                onValueChange = {
                                    if (it.length <= 3) {
                                        currency = it
                                    }
                                },
                                label = { Text("Currency") },
                                modifier = Modifier
                                    .weight(0.45f)
                                    .padding(2.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )
                        }
                    }
                }

                //Password
                if (editMode && visibility == "Private") {
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Change Old Password") },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

            }
            // Leave, Edit and Delete Buttons
            if (isUserInEvent && !editMode) item {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { leaveEvent() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier
                            .fillMaxWidth(0.33f)
                            .padding(end = 5.dp)
                    ) {
                        Text(
                            "Leave Event",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    if (isUserOrganizer) {
                        Button(
                            onClick = { editMode = true },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(end = 5.dp)
                        ) {
                            Text(
                                "Edit Event",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Button(
                            onClick = { deleteEvent() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Delete Event",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                        }
                    }
                }
            }
            //Save Changes Button
            if (editMode) {
                item {
                    Button(
                        onClick = {
                            val price = when {
                                (amount.isEmpty() && currency.isEmpty()) -> "0.00Eur"
                                amount.isEmpty() -> "0.00$currency"
                                currency.isEmpty() -> amount + "Eur"
                                !amount.contains(".") -> "$amount.00$currency"
                                amount.split(".")[1].length == 1 -> amount + "0" + currency
                                amount.contains(".") && amount.split(".")[1].isEmpty() ->
                                    amount + "00" + currency

                                else -> amount + currency
                            }
                            editEvent(
                                title, description, category, subCategory, location, visibility,
                                startDateTime, endDateTime, price, password
                            )
                            editMode = false
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(bottom = 10.dp),
                    ) {
                        Text("Save Changes", color = Color.White)
                    }
                }
            }
        }
        if (editMode) {
            IconButton(
                onClick = { editMode = false },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .width(50.dp)
                    .height(50.dp),
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Red)
            ) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "Cancel",
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun DetailsTexts(
    title: String,
    text: String?,
    editMode: Boolean = false,
    textOnly: Boolean = false,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleMedium,
    color: Color = Color.LightGray,
    onEdit: (String) -> Unit = {}
) {
    Row {
        if (!editMode && text != null) {
            val displayText: AnnotatedString =
                if (text == "") AnnotatedString("No $title available")
                else if (textOnly) AnnotatedString(text)
                else buildAnnotatedString("$title: ", text)

            Text(
                text = displayText,
                style = style,
                color = color
            )

        } else {
            TextField(
                value = text ?: "",
                onValueChange = {
                    onEdit(it)
                },
                label = { Text(title) },
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth(0.8f)
            )
        }
    }
}