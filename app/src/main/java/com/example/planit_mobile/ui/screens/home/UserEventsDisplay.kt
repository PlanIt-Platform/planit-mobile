package com.example.planit_mobile.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.planit_mobile.services.models.SearchEventResult
import com.example.planit_mobile.services.models.UserEventsResult

/**
 *  This function will receive all the events of the user and allow the user to click on each of them
 *  to take the user to the event details screen.
**/
@Composable
fun UserEventsDisplay(
    userEvents: UserEventsResult?,
    onEventClick: (SearchEventResult) -> Unit
) {
    if(userEvents != null){
        LazyColumn {
            if (userEvents.events.isEmpty()) {
                item {
                    Text(
                        text = "You currently have no events. Create or join an event to get started!",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            }
            else {
                items(userEvents.events) { event ->
                    EventCard(
                        event = event,
                        onEventClick = { onEventClick(event) }
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(event: SearchEventResult, onEventClick: (SearchEventResult) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(2.dp, shape = RoundedCornerShape(10.dp))
            .background(Color(0xFF3A4079), shape = RoundedCornerShape(10.dp))
            .clickable { onEventClick(event) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Location: " + (event.location ?: "To be determined"),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

