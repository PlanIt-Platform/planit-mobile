package com.example.planit_mobile.ui.screens.home

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CreateEventPopup(
    onDismiss: (Boolean) -> Unit,
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    subCategories: List<String>,
    createEventRequested: (
        String, String, String, String, String, String, String, String, String, String
    ) -> Unit,
    eventCreatedPopUp: Boolean
) {
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var expandedSub by remember { mutableStateOf(false) }
    var expandedVisibility by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedSubCategory by remember { mutableStateOf("") }
    var selectedVisibility by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var startDateTime by remember { mutableStateOf("") }
    var endDateTime by remember { mutableStateOf("") }
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
                Toast.makeText(context, "Start Date must be after current date", Toast.LENGTH_SHORT).show()
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
                            selectedTime.before(currentDate)) {
                            Toast.makeText(context, "Start Date must be after current date", Toast.LENGTH_SHORT).show()
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
                    selectedDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) {
                    timePickerDialog.updateTime(currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(
                        Calendar.MINUTE))
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
            ){
                Toast.makeText(context, "End date must be after start date", Toast.LENGTH_SHORT).show()
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
                            Toast.makeText(context, "End date must be after start date", Toast.LENGTH_SHORT).show()
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
                    selectedEndDate.get(Calendar.YEAR) == startDate.get(Calendar.YEAR)) {
                    timePickerDialog.updateTime(startDate.get(Calendar.HOUR_OF_DAY), startDate.get(
                        Calendar.MINUTE))
                }
                timePickerDialog.show()
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    endDatePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

    LazyColumn (
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                IconButton(onClick = { onDismiss(false) },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close dialog", tint = Color.White)
                }
                Text(
                    text = "Create Event",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Event Name Field
            SectionBox {
                TextField(
                    value = eventName,
                    onValueChange = { eventName = it },
                    label = { Text("Event Name *") },
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(0.9f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    )
                )
            }

            // Event Description Field
            SectionBox {
                TextField(
                    value = eventDescription,
                    onValueChange = { eventDescription = it },
                    label = { Text("Event Description") },
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(0.9f)
                )
            }

            // Categories, Subcategories and Visibility
            Row(
                modifier = Modifier.padding(start = 10.dp ,top = 10.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                ) {

                    // Categories
                    Row(modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)) {
                        Text(
                            "Categories *",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Row {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .border(1.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                                .background(Color.LightGray, RoundedCornerShape(4.dp))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(selectedCategory, modifier = Modifier.padding(start = 7.dp))
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Select category"
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.width(200.dp)
                            ) {
                                categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category) },
                                        onClick = {
                                            selectedCategory = category
                                            expanded = false
                                            if (selectedCategory != "Simple Meeting") {
                                                onCategorySelected(category)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Subcategories
                    Row(modifier = Modifier.padding(start = 5.dp, top = 15.dp, bottom = 5.dp)) {
                        Text(
                            "Subcategories",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Row {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .border(1.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                                .background(Color.LightGray, RoundedCornerShape(4.dp))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(selectedSubCategory, modifier = Modifier.padding(start = 7.dp))
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = { expandedSub = true }) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Select subcategory"
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = expandedSub,
                                onDismissRequest = { expandedSub = false }
                            ) {
                                if (selectedCategory.isNotEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text("") },
                                        onClick = {
                                            selectedSubCategory = ""
                                            expandedSub = false
                                        }
                                    )
                                    subCategories.forEach { subCategory ->
                                        DropdownMenuItem(
                                            text = { Text(subCategory) },
                                            onClick = {
                                                if (selectedCategory.isNotEmpty()) {
                                                    selectedSubCategory = subCategory
                                                    expandedSub = false
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Visibility
                    Row(modifier = Modifier.padding(start = 5.dp, top = 15.dp, bottom = 5.dp)) {
                        Text(
                            "Visibility *",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Row {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .border(1.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                                .background(Color.LightGray, RoundedCornerShape(4.dp))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(selectedVisibility, modifier = Modifier.padding(start = 7.dp))
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = { expandedVisibility = true }) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Select visibility"
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = expandedVisibility,
                                onDismissRequest = { expandedVisibility = false }) {
                                DropdownMenuItem(
                                    text = { Text("Public") },
                                    onClick = {
                                        selectedVisibility = "Public"
                                        expandedVisibility = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Private") },
                                    onClick = {
                                        selectedVisibility = "Private"
                                        expandedVisibility = false
                                    }
                                )

                            }
                        }
                    }
                }
            }


            //Start Date Field
            SectionBox {
                Column {
                    TextField(
                        value = startDateTime,
                        onValueChange = { startDateTime = it },
                        label = { Text("Start Date *") },
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(0.9f),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { startDatePickerDialog.show() }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                            }
                        }
                    )
                    if (startDateTime.isEmpty()){
                        Text(
                            text = "(Start Date must be after current date)",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            //End Date Field
            SectionBox {
                Column {
                    TextField(
                        value = endDateTime,
                        onValueChange = { endDateTime = it },
                        label = { Text("End Date") },
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(0.9f),
                        readOnly = true,
                        enabled = startDateTime.isNotEmpty(),
                        trailingIcon = {
                            IconButton(onClick = { if (startDateTime.isNotEmpty()) endDatePickerDialog.show() }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                            }
                        }
                    )
                    if (endDateTime.isEmpty()){
                        Text(
                            text = "(End Date must be after Start Date)",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            // Location
            SectionBox {
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(0.9f)
                )
            }

            // Price
            // Amount and Currency Fields
            SectionBox {
                Column {
                    Text(
                        text = "Price",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Row (modifier = Modifier.fillMaxWidth(0.9f)) {
                        // Amount Field
                        TextField(
                            value = amount,
                            onValueChange = {
                                if ((it.toDoubleOrNull() != null && it.toDouble() > 0 && it.matches("^-?\\d*(\\.\\d{0,2})?$".toRegex())) || it.isEmpty() || it == "" ) {
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

            // Password
            if (selectedVisibility == "Private") {
                SectionBox {
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password *") },
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
                            .fillMaxWidth(0.9f)
                    )
                }
            }


            // Create Event Button
            SectionBox {
                Button(
                    onClick = {
                        val price = when {
                            (amount.isEmpty() && currency.isEmpty()) -> "0.00Eur"
                            amount.isEmpty() -> "0.00$currency"
                            currency.isEmpty() -> amount+"Eur"
                            !amount.contains(".") -> "$amount.00$currency"
                            amount.split(".")[1].length == 1 -> amount+"0"+currency
                            amount.contains(".") && amount.split(".")[1].isEmpty() ->
                                amount+"00"+currency
                            else -> amount+currency
                        }
                        createEventRequested(
                            eventName, eventDescription, selectedCategory, selectedSubCategory,
                            location, selectedVisibility, startDateTime, endDateTime, price, password
                        )
                    },
                    enabled = eventName.isNotEmpty() && selectedCategory.isNotEmpty()
                            && selectedVisibility.isNotEmpty() && startDateTime.isNotEmpty() &&
                            (selectedVisibility == "Public" || password.isNotEmpty()),
                ) {
                    Text("Create Event")
                }
            }

            Row (modifier = Modifier.padding(top = 15.dp)) {
                Text(
                    text = "* Required Fields",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }

    if (eventCreatedPopUp) {
        //clear all fields
        eventName = ""; eventDescription = ""; selectedCategory = ""
        selectedSubCategory = ""; location = ""; selectedVisibility = ""
        startDateTime = ""; endDateTime = ""; amount = ""; currency = ""; password = ""

        onDismiss(false)
    }
}


@Composable
fun SectionBox(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}