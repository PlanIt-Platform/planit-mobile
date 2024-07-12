package com.example.planit_mobile.ui.screens.home

import android.location.Geocoder
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.planit_mobile.ui.screens.common.MyGoogleMap
import com.example.planit_mobile.ui.screens.common.endDatePickerDialog
import com.example.planit_mobile.ui.screens.common.startDatePickerDialog
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CreateEventPopup(
    onDismiss: (Boolean) -> Unit,
    categories: List<String>,
    createEventRequested: (
        String, String, String, String?, String?, String?, String?, String, String, String, String, String
    ) -> Unit,
    eventCreatedPopUp: Boolean
) {
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var expandedVisibility by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedVisibility by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var locationLink by remember { mutableStateOf("") }
    var locationSwitchState by remember { mutableStateOf(false) }
    var locationType by remember { mutableStateOf("Physical") }
    var amount by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val initialLocation = LatLng(0.0, 0.0)
    var locationCoords by remember { mutableStateOf(initialLocation)}
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 0.5f)
    }
    var markerVisibility by remember { mutableStateOf(false) }
    var locationSearch by remember { mutableStateOf("") }
    var successfulLocation by remember { mutableStateOf(false)}
    var columnScrollingEnabled by remember { mutableStateOf(true) }

    var startDateTime by remember { mutableStateOf("") }
    var endDateTime by remember { mutableStateOf("") }
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    var passwordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            columnScrollingEnabled = true
        }
    }

    val startDatePickerDialog = startDatePickerDialog(
        context,
        calendar,
        {sdt -> startDateTime = sdt},
        dateFormat
    )

    startDatePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

    val endDatePickerDialog = endDatePickerDialog(context,
        startDateTime,
        { edt -> endDateTime = edt },
        calendar,
        dateFormat
    )

    endDatePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState(),
                columnScrollingEnabled
            )
    ) {
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

        // Categories and Visibility
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
                                    }
                                )
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
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Location Type",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Switch(
                        checked = locationSwitchState,
                        onCheckedChange = {
                            locationSwitchState = it
                            locationType = if (!locationSwitchState) "Physical" else "Online"
                        },
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        locationType,
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (locationType == "Online") {
                    TextField(
                        value = locationLink,
                        onValueChange = { locationLink = it },
                        label = { Text("Insert link to event") },
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(0.9f)
                    )
                } else if (locationType == "Physical") {
                    TextField(
                        value = locationSearch,
                        onValueChange = { locationSearch = it },
                        label = { Text("Search Location") },
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(0.9f),
                        keyboardActions = KeyboardActions(onDone = {
                            try{
                                val geocoder = Geocoder(context, Locale.getDefault())
                                val locationName =
                                    geocoder.getFromLocationName(locationSearch, 1)
                                if(locationName != null){
                                    val locationLatLng = locationName[0]
                                    location = locationLatLng.getAddressLine(0)
                                    locationCoords = LatLng(locationLatLng.latitude, locationLatLng.longitude)
                                    markerVisibility = true
                                    cameraPositionState.position = CameraPosition.fromLatLngZoom(locationCoords, 15f)
                                    successfulLocation = true
                                }
                            } catch (e: Exception){
                                Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                                successfulLocation = false
                            }
                        }),
                        singleLine = true
                    )
                    MyGoogleMap(
                        viewOnlyMode = false,
                        cameraPositionState = cameraPositionState,
                        context = context,
                        locationCoords = locationCoords,
                        markerVisibility = markerVisibility,
                        columnScrollingEnabled = { scroll -> columnScrollingEnabled = scroll },
                        locationChange = { locat -> location = locat; successfulLocation = true },
                        locationCoordsChange = { coords -> locationCoords = coords },
                        markerVisibilityChange = { marker -> markerVisibility = marker }
                    )
                    Text(
                        text = "Selected location: $location",
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = "Selected coordinates: ${locationCoords.latitude}, ${locationCoords.longitude}",
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                    Button(
                        onClick = {
                            location = ""
                            locationSearch = ""
                            locationCoords = initialLocation
                            markerVisibility = false
                        }
                    ) {
                        Text("Clear Location")
                    }
                }
            }
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

        Row (modifier = Modifier.padding(top = 15.dp)) {
            Text(
                text = "* Required Fields",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(5.dp)
            )
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
                    val finalLocation =
                        if (locationType == "Physical"){
                            if (location == "") null else location
                        } else {
                            if(locationLink == "")null else locationLink
                        }
                    val finalLocationType = if (finalLocation == null) null else locationType
                    val (latitude, longitude) =
                        if (locationType == "Physical" && location.isNotEmpty() && successfulLocation) {
                            Pair(locationCoords.latitude.toString(), locationCoords.longitude.toString())
                        } else {
                            Pair(null, null)
                        }
                    createEventRequested(
                        eventName, eventDescription, selectedCategory,
                        finalLocationType, finalLocation, latitude, longitude, selectedVisibility,
                        startDateTime, endDateTime, price, password
                    )
                },
                enabled = eventName.isNotEmpty() && selectedCategory.isNotEmpty()
                        && selectedVisibility.isNotEmpty() && startDateTime.isNotEmpty() &&
                        (selectedVisibility == "Public" || password.isNotEmpty()),
            ) {
                Text("Create Event")
            }
        }
    }

    if (eventCreatedPopUp) {
        //clear all fields
        eventName = ""; eventDescription = ""; selectedCategory = ""; locationType = ""
        location = ""; locationCoords = LatLng(0.0,0.0)
        selectedVisibility = ""; startDateTime = ""; endDateTime = ""; amount = ""; currency = ""
        password = ""

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