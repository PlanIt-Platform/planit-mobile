package com.example.planit_mobile.ui.screens.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.planit_mobile.services.models.SearchEventResult
import com.example.planit_mobile.services.models.UserEventsResult
import com.example.planit_mobile.ui.screens.searchEvent.EventCard

/**
 *  TODO ()
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
            items(userEvents.events) { event ->
                EventCard(
                    event = event,
                    onEventClick = { onEventClick(event) }
                )
            }
        }
    }
}
