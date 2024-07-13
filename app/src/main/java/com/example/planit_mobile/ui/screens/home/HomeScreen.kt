package com.example.planit_mobile.ui.screens.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.planit_mobile.services.models.SearchEventResult
import com.example.planit_mobile.services.models.UserEventsResult
import com.example.planit_mobile.ui.screens.common.BotBar
import com.example.planit_mobile.ui.screens.common.NavigationHandlers

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(
    onProfileRequested: () -> Unit,
    onHomeRequested: () -> Unit,
    onCalendarRequested: () -> Unit,
    onEventsRequested: () -> Unit,
    categories: List<String>,
    createEventRequested : (
        String, String, String, String?, String?, String?, String?, String, String, String, String, String
    ) -> Unit,
    eventCreatedPopUp : Boolean,
    eventCreatedMessage : String,
    userEvents: UserEventsResult?,
    onEventClick: (SearchEventResult) -> Unit,
    dismissEventCreatedPopUp: () -> Unit,
    onNearMeRequested: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Home",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )
                },
                colors = topAppBarColors(containerColor = Color(24, 38, 44, 255)),
                actions = {
                    IconButton(
                        onClick = {
                           onCalendarRequested()
                        },
                        modifier = Modifier.size(35.dp)
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Calendar",
                            tint = Color.White,
                            modifier = Modifier.size(29.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BotBar(navigation =
            NavigationHandlers(
                onProfileRequested = onProfileRequested,
                onHomeRequested = onHomeRequested,
                onEventsRequested = onEventsRequested
            )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 10.dp),
                onClick = { showDialog = true },
                shape = CircleShape,
                containerColor = Color(0xFF3543C5),
            ) {
                Icon(Icons.Default.Add,
                    contentDescription = "Create Event",
                    tint = Color.White
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {

        BackgroundBox(
            it,
            userEvents = userEvents,
            onEventClick = { event ->
                onEventClick(event)
            },
            onNearMeRequested = {
                onNearMeRequested()
            }
        )

        // Create Event Dialog
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(39, 62, 73, 255))
                ) {
                    CreateEventPopup (
                        onDismiss = { action -> showDialog = action },
                        categories = categories,
                        createEventRequested = createEventRequested,
                        eventCreatedPopUp = eventCreatedPopUp
                    )
                }
            }
        }

        // Event Created Popup
        if (eventCreatedPopUp) {
            Toast.makeText(LocalContext.current, eventCreatedMessage, Toast.LENGTH_SHORT).show()
            dismissEventCreatedPopUp()
            showDialog = false
        }

    }
}

@Composable
fun BackgroundBox(
    it: PaddingValues,
    userEvents: UserEventsResult?,
    onEventClick: (SearchEventResult) -> Unit,
    onNearMeRequested: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ){
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "My events",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(10.dp)
                    )
                    Row {
                        UserEventsDisplay(
                            userEvents = userEvents,
                            onEventClick = { event ->
                                onEventClick(event)
                            }
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Want to find events around you?",
                        color = Color.White,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Click here",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp).clickable { onNearMeRequested() }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewUserProfileScreen() {
    HomeScreen(
        onProfileRequested = {},
        onHomeRequested = {},
        onCalendarRequested = {},
        onEventsRequested = {},
        categories = listOf("Simple Meeting", "Birthday Party", "Wedding"),
        createEventRequested = { _, _, _, _, _, _, _, _, _, _, _, _ -> },
        eventCreatedPopUp = false,
        eventCreatedMessage = "",
        userEvents = UserEventsResult(
            1,
            "username",
            listOf()
        ),
        onEventClick = {},
        dismissEventCreatedPopUp = {},
        onNearMeRequested = {}
    )
}