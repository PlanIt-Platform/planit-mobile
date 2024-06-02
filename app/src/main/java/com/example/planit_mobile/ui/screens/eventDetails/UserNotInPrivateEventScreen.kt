package com.example.planit_mobile.ui.screens.eventDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.planit_mobile.ui.screens.common.BackArrow


@Composable
fun UserNotInPrivateEventScreen(
    joinEvent: (String) -> Unit,
    onBackRequested: () -> Unit
) {
    var password by remember { mutableStateOf("") }

    Box(
        contentAlignment = Alignment.TopStart
    ){
        BackArrow (Color.White) {
            onBackRequested()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Private Event",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 5.dp),
            color = Color.White
        )

        Text(
            text = "Please enter the event password to join.",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 30.dp),
            color = Color(0xFFC7C7C7)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Event Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8F),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { joinEvent(password) },
            modifier = Modifier.fillMaxWidth(0.55F)
        ) {
            Text("Join Event")
        }
    }
}