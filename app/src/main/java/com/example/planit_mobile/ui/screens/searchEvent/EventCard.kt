package com.example.planit_mobile.ui.screens.searchEvent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.planit_mobile.services.models.SearchEventResult
import com.example.planit_mobile.ui.screens.common.buildAnnotatedString
import com.example.planit_mobile.ui.screens.common.formatDate

@Composable
fun EventCard(event: SearchEventResult, onEventClick: (SearchEventResult) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3A4079),
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable(onClick = { onEventClick(event) })
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = buildAnnotatedString("Visibility: ", event.visibility),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            if (event.visibility != "Private") {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString(
                        "Description: ",
                        if (event.description != null && event.description != "")
                            event.description else "No description available"
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString("Category: ", event.category ?: ""),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString(
                        "Location: ",
                        event.location ?: "No location available"
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text =
                    buildAnnotatedString(
                        "Date: ",
                        if (event.date != null) formatDate(event.date) else ""
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = { onEventClick(event) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2104A2))
            ) {
                Text("View Details")
            }
        }
    }
}