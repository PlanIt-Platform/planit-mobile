package com.example.planit_mobile.ui.screens.eventDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.planit_mobile.services.models.Message
import com.example.planit_mobile.ui.screens.common.buildAnnotatedString

@Composable
fun ChatDialog(
    onDismiss: () -> Unit,
    sendMessage: (String) -> Unit,
    messages: List<Message>
) {
    var message by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85F)
                .fillMaxWidth(0.95f)
                .background(Color(0xFF221C36)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
                    verticalArrangement = Arrangement.Top
                ) {
                    IconButton(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Event Chat",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                }

                LazyColumn {
                    item {
                        messages.forEach {
                            Row {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "User",
                                    tint = Color.White,
                                    modifier = Modifier.padding(5.dp)
                                )
                                Text(
                                    text = buildAnnotatedString(
                                        boldText = (it.name ?: "Unknown") + ": ",
                                        normalText = it.text
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }

                Column (
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                ){
                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Message") },
                        modifier = Modifier
                            .padding(16.dp),
                    )
                    IconButton(
                        onClick = {
                            if (message.isNotEmpty()) {
                                sendMessage(message)
                                message = ""
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.Blue
                        ),
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}