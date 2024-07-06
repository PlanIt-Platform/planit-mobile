package com.example.planit_mobile.ui.screens.common

import android.content.Context
import android.location.Geocoder
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import java.util.Locale

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun MyGoogleMap(
    viewOnlyMode: Boolean = true,
    cameraPositionState: CameraPositionState,
    context: Context,
    locationCoords: LatLng,
    markerVisibility: Boolean = false,
    columnScrollingEnabled: (Boolean) -> Unit = {},
    locationChange: (String) -> Unit = {},
    locationCoordsChange: (LatLng) -> Unit = {},
    markerVisibilityChange: (Boolean) -> Unit = {}
){
    Box {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .height(350.dp)
                .pointerInteropFilter(
                    onTouchEvent = {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                columnScrollingEnabled(false)
                                false
                            }
                            else -> {
                                columnScrollingEnabled(true)
                                true
                            }
                        }
                    }
                ),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                if (!viewOnlyMode) {
                    try{
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val locationName =
                            geocoder.getFromLocation(it.latitude, it.longitude, 1)
                        if(locationName != null){
                            val locationLatLng = locationName[0]
                            locationChange(locationLatLng.getAddressLine(0))
                            locationCoordsChange(it)
                            markerVisibilityChange(true)
                        }
                    } catch (e: Exception){
                        Toast.makeText(context, "Invalid Location", Toast.LENGTH_SHORT).show()
                    }
                }
                columnScrollingEnabled(true)
            }
        ) {
            Marker(
                state = MarkerState(position = locationCoords),
                visible = markerVisibility
            )
        }
    }
}