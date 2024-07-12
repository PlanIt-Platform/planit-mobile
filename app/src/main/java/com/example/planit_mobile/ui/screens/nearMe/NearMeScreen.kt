package com.example.planit_mobile.ui.screens.nearMe

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.services.models.NearbyEventModel
import com.example.planit_mobile.services.models.NearbyEventsResult
import com.example.planit_mobile.ui.screens.common.BackArrow
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearMeScreen(
    userLocation: Location,
    selectedRadius: String,
    numberOfEvents: Float,
    nearbyEvents: NearbyEventsResult,
    onEventClick: (NearbyEventModel) -> Unit,
    onRadiusChanged: (String) -> Unit,
    onNumberChanged: (Float) -> Unit,
    onBackRequested: () -> Unit,
    onFindNearbyEventsRequested: (Int, Int) -> Unit
) {
    val radiusOptions = listOf("25", "50", "100")
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
                        "Find out what's around you!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp // Adjust the font size as needed
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(24, 38, 44, 255))
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            Box {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        LatLng(userLocation.latitude, userLocation.longitude),
                        8f
                    )
                }
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    cameraPositionState = cameraPositionState,
                ) {
                    Circle(
                        center = LatLng(userLocation.latitude, userLocation.longitude),
                        radius = selectedRadius.toDouble() * 1000,
                        fillColor =
                        when (selectedRadius) {
                            "25" -> Color(0x7CC3E69F)
                            "50" -> Color(0x886CB3D5)
                            else -> Color(0x80F3E285)
                        },
                        strokeColor = Color(0xFF454546),
                    )
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                userLocation.latitude,
                                userLocation.longitude
                            )
                        ),
                        title = "Your location",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )
                    nearbyEvents.events.forEach { event ->
                        Marker(
                            state = MarkerState(position = LatLng(event.latitude, event.longitude)),
                            title = event.title,
                            snippet = event.location
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .offset(x = 20.dp, y = 10.dp)
                    .height(40.dp)
                    .width(148.dp)
                    .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(15.dp))
                    .border(2.dp, SolidColor(Color.Gray), shape = RoundedCornerShape(15.dp))
            ) {

                RadiusSelector(
                    onRadiusSelected = { value -> onRadiusChanged(value) },
                    selectedRadius = selectedRadius,
                    radiusOptions = radiusOptions
                )
            }
            Box(
                modifier = Modifier
                    .offset(x = 70.dp, y = 450.dp)
                    .height(65.dp)
                    .width(270.dp)
                    .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(15.dp))
                    .border(2.dp, SolidColor(Color.Gray), shape = RoundedCornerShape(15.dp))
            ) {
                Text(
                    text = "Number of events: ${numberOfEvents.toInt()}",
                    modifier = Modifier.padding(8.dp),
                )
                Slider(
                    value = numberOfEvents,
                    onValueChange = { value -> onNumberChanged(value) },
                    valueRange = 1f..100f,
                    steps = 99,
                    modifier = Modifier.padding(vertical = 40.dp, horizontal = 10.dp)
                )
            }
            Box(
                modifier = Modifier
                    .offset(y = 550.dp)
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    if (nearbyEvents.events.isEmpty()) {
                        Text(
                            text = "No events found",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            modifier = Modifier
                                .offset(x = 100.dp, y = 70.dp)
                        )
                    }
                    nearbyEvents.events.forEach { event ->
                        EventCard(event, onEventClick)
                    }
                }
            }
            FloatingActionButton(
                modifier = Modifier .align(Alignment.BottomCenter)
                    .offset(x = 165.dp, y = (-20).dp),
                onClick = { onFindNearbyEventsRequested(selectedRadius.toInt() * 1000, numberOfEvents.toInt()) },
                shape = CircleShape,
                containerColor = Color(0xFF3543C5),
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Find Events",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun EventCard(event: NearbyEventModel, onEventClick: (NearbyEventModel) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(2.dp, shape = RoundedCornerShape(10.dp))
            .background(Color(0xFF3A4079), shape = RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onEventClick(event) }
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Location: ${event.location}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}


@Composable
fun RadiusSelector(
    onRadiusSelected: (String) -> Unit,
    selectedRadius: String,
    radiusOptions: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

        Text(
            text = "Radius: $selectedRadius" + "km",
            modifier = Modifier
                .clickable { expanded = true }
                .padding(
                    horizontal = 16.dp, vertical = 10.dp
                )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            radiusOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "$option km",
                            style = TextStyle(fontSize = 16.sp)
                        )
                    },
                    onClick = {
                        onRadiusSelected(option)
                        expanded = false
                    },
                    modifier = Modifier.width(90.dp)
                )
            }
        }
}