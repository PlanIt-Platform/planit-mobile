package com.example.planit_mobile.ui.screens.eventDetails

import android.location.Geocoder
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.services.models.EventModel
import com.example.planit_mobile.ui.screens.common.MyGoogleMap
import com.example.planit_mobile.ui.screens.common.buildAnnotatedString
import com.example.planit_mobile.ui.screens.common.endDatePickerDialog
import com.example.planit_mobile.ui.screens.common.formatDate
import com.example.planit_mobile.ui.screens.common.startDatePickerDialog
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
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
        String, String?, String, String?, String?, String?, String, String, String?, String, String
    ) -> Unit,
    deleteEvent: () -> Unit,
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    subCategories: List<String>
) {
    Log.d("EventDetailsContent", "EventDetailsContent")
    var editMode by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(eventDetails.title) }
    var description by remember { mutableStateOf(eventDetails.description) }
    var category by remember { mutableStateOf(eventDetails.category) }
    var subCategory by remember { mutableStateOf(eventDetails.subcategory) }
    var locationType by remember { mutableStateOf(eventDetails.locationType) }
    var location by remember { mutableStateOf(eventDetails.location) }
    var locationLink by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(eventDetails.visibility) }
    var amount by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val initialLocation = LatLng(0.0, 0.0)
    var locationCoords by remember { mutableStateOf(initialLocation)}
    var columnScrollingEnabled by remember { mutableStateOf(true) }
    var markerVisibility by remember { mutableStateOf(false) }
    var locationSearch by remember { mutableStateOf("") }
    var locationSwitchState by remember { mutableStateOf(false) }

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

    val startDatePickerDialog = startDatePickerDialog(
        context,
        calendar,
        { sdt -> startDateTime = sdt },
        dateFormat
    )

    startDatePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

    val endDatePickerDialog = endDatePickerDialog(
        context,
        startDateTime,
        { edt -> endDateTime = edt },
        calendar,
        dateFormat
    )

    endDatePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

    Box(
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState(),
                    columnScrollingEnabled
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                    "Visibility",
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
                    DetailsTexts("Date", formatDate(eventDetails.date))
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

            //Location Type
            if (!editMode) {
                if (eventDetails.locationType != null){
                    DetailsTexts(title = "Location Type", text = eventDetails.locationType)
                }
            }

            //Location
            if(!editMode){
                if (eventDetails.location != null && eventDetails.location != ""){
                    DetailsTexts(title = "Location", text = eventDetails.location)
                    if(eventDetails.locationType == "Physical"){
                        var successfulLocation by remember { mutableStateOf(false)}
                        try{
                            val geocoder = Geocoder(context, Locale.getDefault())
                            val locationName =
                                geocoder.getFromLocationName(eventDetails.location, 1)
                            if(locationName != null){
                                val locationLatLng = locationName[0]
                                location = locationLatLng.getAddressLine(0)
                                locationCoords = LatLng(locationLatLng.latitude, locationLatLng.longitude)
                            }
                            successfulLocation = true
                        } catch (e: Exception){
                            Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                        }
                        if(successfulLocation){
                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(locationCoords, 8f)
                            }
                            LaunchedEffect(cameraPositionState.isMoving) {
                                if (!cameraPositionState.isMoving) {
                                    columnScrollingEnabled = true
                                }
                            }
                            MyGoogleMap(
                                viewOnlyMode = true,
                                cameraPositionState = cameraPositionState,
                                context = context,
                                locationCoords = locationCoords,
                                markerVisibility = true,
                                columnScrollingEnabled = { scroll -> columnScrollingEnabled = scroll }
                            )
                        } else {
                            DetailsTexts(title = "Location", text = "")
                        }
                    } else {
                        DetailsTexts(title = "Event Link", text = eventDetails.location)
                    }
                }
            } else {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.7f),
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
                            locationType ?: "Physical",
                            color = Color.White
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
                        if(location != "" && location != null) markerVisibility = true
                        val cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(initialLocation, 0.5f)
                        }
                        LaunchedEffect(cameraPositionState.isMoving) {
                            if (!cameraPositionState.isMoving) {
                                columnScrollingEnabled = true
                            }
                        }
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
                                    }
                                } catch (e: Exception){
                                    Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
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
                            locationChange = { locat -> location = locat },
                            locationCoordsChange = { coords -> locationCoords = coords },
                            markerVisibilityChange = { marker -> markerVisibility = marker }
                        )
                        Text(
                            text = "Selected location: $location",
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
                        "Price",
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

            // Leave, Edit and Delete Buttons
            if (isUserInEvent && !editMode)
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
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
            //Save Changes Button
            if (editMode) {
                Row (modifier = Modifier.padding(top = 15.dp)) {
                    Text(
                        text = "* Required Fields",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                }
                Row (horizontalArrangement = Arrangement.Center){
                    Button(
                        onClick = {
                            locationSearch = ""
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
                            val finalLocation =
                                if (locationType == "Physical") {
                                    if (location == "") null else location
                                } else {
                                    if (locationLink == "") null else locationLink
                                }
                            val finalLocationType =
                                if (finalLocation == null) null else locationType
                            editEvent(
                                title,
                                description,
                                category,
                                subCategory,
                                finalLocationType,
                                finalLocation,
                                visibility,
                                startDateTime,
                                endDateTime,
                                price,
                                password
                            )
                            editMode = false
                        },
                        modifier = Modifier
                            .padding(bottom = 10.dp),
                    ) {
                        Text("Save Changes", color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
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