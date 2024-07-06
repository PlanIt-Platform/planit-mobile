package com.example.planit_mobile.ui.screens.nearMe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.planit_mobile.PlanItDependencyProvider
import com.example.planit_mobile.services.models.NearbyEventsResult
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.ErrorPopup
import com.example.planit_mobile.ui.screens.common.Idle
import com.example.planit_mobile.ui.screens.common.Loaded
import com.example.planit_mobile.ui.screens.common.Loading
import com.example.planit_mobile.ui.screens.common.LoadingScreen
import com.example.planit_mobile.ui.screens.common.getOrNull
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.eventDetails.EventDetailsActivity
import com.example.planit_mobile.ui.screens.home.HomeActivity
import com.example.planit_mobile.ui.theme.PlanitMobileTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class NearMeActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val viewModel by viewModels<NearMeViewModel> {
        NearMeViewModel.factory(dependencies.eventService, dependencies.sessionStorage)
    }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, NearMeActivity::class.java)
            origin.startActivity(intent)
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()
    }

    private fun getLastLocation() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, permissions, 1)
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLocation = location

                    lifecycleScope.launch {
                        viewModel.loadState.collect {
                            if (it is Idle) {
                                viewModel.findNearbyEvents(
                                    currentLocation.latitude,
                                    currentLocation.longitude,
                                    25000,
                                    20
                                )
                            }
                        }
                    }

                    setContent {
                        val errorMessage = viewModel.errorState.collectAsState(initial = Error("")).value.message
                        val nearbyEventsState = viewModel.loadState.collectAsState(initial = idle()).value
                        val selectedRadius = viewModel.selectedRadius.collectAsState(initial = "25").value
                        val numberOfEvents = viewModel.numberOfEvents.collectAsState(initial = 1f).value
                        PlanitMobileTheme {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                if (nearbyEventsState is Loading) {
                                    LoadingScreen {
                                        HomeActivity.navigateTo(this)
                                    }
                                }
                                else {
                                    NearMeScreen(
                                        userLocation = currentLocation,
                                        selectedRadius = selectedRadius,
                                        numberOfEvents = numberOfEvents,
                                        nearbyEvents = nearbyEventsState.getOrNull() ?: NearbyEventsResult(emptyList()),
                                        onEventClick = { event ->
                                            EventDetailsActivity.navigateTo(
                                                this@NearMeActivity,
                                                event.id,
                                                "Public"
                                            )
                                        },
                                        onRadiusChanged = { newRadius ->
                                            viewModel.updateRadius(newRadius)
                                        },
                                        onNumberChanged = { newNumberOfEvents ->
                                            viewModel.updateNumberOfEvents(newNumberOfEvents)
                                        },
                                        onBackRequested = { finish() },
                                        onFindNearbyEventsRequested = { selectedRadius, numEvents ->
                                            viewModel.findNearbyEvents(
                                                currentLocation.latitude,
                                                currentLocation.longitude,
                                                selectedRadius,
                                                numEvents
                                            )
                                        },
                                    )
                                    ErrorPopup(
                                        showDialog = errorMessage != "",
                                        errorMessage = errorMessage
                                    ) {
                                        viewModel.dismissError()
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }
}
