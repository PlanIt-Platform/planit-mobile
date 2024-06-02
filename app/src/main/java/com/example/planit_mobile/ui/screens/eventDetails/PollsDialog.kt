package com.example.planit_mobile.ui.screens.eventDetails

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.planit_mobile.services.models.PollModel
import com.example.planit_mobile.ui.screens.common.buildAnnotatedString

@SuppressLint("MutableCollectionMutableState")
@Composable
fun PollsDialog(
    polls: List<PollModel>,
    createPoll: (String, String, List<String>) -> Unit,
    onDismissRequest: () -> Unit,
    isUserOrganizer: Boolean,
    deletePoll: (Int) -> Unit,
    voteOnPoll: (Int, Int) -> Unit,
    createPollOptions: Int,
    setCreatePollOptions: (Int) -> Unit
) {
    var showPollsOrCreate by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85F)
                .fillMaxWidth(0.95f)
                .background(Color(0xFF221C36)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Polls",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.padding(16.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (!showPollsOrCreate) {
                setCreatePollOptions(1)
                LazyColumn {
                    if (polls.isNotEmpty()) {
                        items(polls) { poll ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.95f)
                                    .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Text(
                                            text = poll.title,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (isUserOrganizer) {
                                            IconButton(
                                                onClick = { deletePoll(poll.id) },
                                                modifier = Modifier
                                                    .height(25.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_menu_delete),
                                                    contentDescription = "Delete Poll",
                                                    tint = Color.White
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = buildAnnotatedString(
                                            "Created at: ",
                                            poll.created_at.substringBeforeLast(":")
                                        ),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.LightGray
                                    )
                                    Text(
                                        text = buildAnnotatedString(
                                            "Duration: ",
                                            poll.duration.toString() + " Hours"
                                        ),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.LightGray
                                    )
                                    poll.options.forEach { option ->
                                        Button(
                                            onClick = {
                                                voteOnPoll(poll.id, option.id)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth(0.9f)
                                                .padding(vertical = 8.dp)
                                        ) {
                                            Text(
                                                buildAnnotatedString(
                                                    option.title + ": ",
                                                    option.votes.toString() + " Votes"
                                                ),
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (isUserOrganizer) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Button(
                            onClick = {
                                showPollsOrCreate = true
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Create Poll")
                        }
                    }
                }
            } else {
                var pollTitle by remember { mutableStateOf("") }
                var pollDuration by remember { mutableStateOf("") }
                val durations = listOf("1", "4", "8", "12", "24", "72")
                var durationExpanded by remember { mutableStateOf(false) }
                var pollOptions by remember { mutableStateOf(mutableListOf(mutableStateOf(""))) }

                LazyColumn {
                    item {
                        Text(
                            text = "Create Poll",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(5.dp),
                            color = Color.White
                        )
                    }
                    item {
                        TextField(
                            value = pollTitle,
                            onValueChange = { pollTitle = it },
                            label = { Text("Title") },
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.93f)
                                .border(1.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                                .background(Color.LightGray, RoundedCornerShape(4.dp))
                                .height(50.dp)
                                .padding(5.dp)
                                .align(Alignment.CenterHorizontally),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = pollDuration.ifEmpty { "Duration" },
                                modifier = Modifier
                                    .clickable { durationExpanded = true }
                                    .fillMaxSize(),
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            )
                            DropdownMenu(
                                expanded = durationExpanded,
                                onDismissRequest = { durationExpanded = false }
                            ) {
                                durations.forEach { durationOption ->
                                    DropdownMenuItem(
                                        onClick = {
                                            pollDuration = durationOption
                                            durationExpanded = false
                                        },
                                        text = {
                                            Text(text = durationOption)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Text(
                            text = "Options",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(5.dp),
                            color = Color.White
                        )
                    }
                    items(createPollOptions) { index ->
                        TextField(
                            value = pollOptions[index].value,
                            onValueChange = { newValue ->
                                pollOptions[index].value = newValue
                            },
                            label = { Text("Option ${index + 1}") },
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                    item {
                        IconButton(
                            onClick = {
                                pollOptions.add(mutableStateOf(""))
                                setCreatePollOptions(createPollOptions + 1)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_menu_add),
                                contentDescription = "Add Option",
                                modifier = Modifier.padding(5.dp),
                                tint = Color.Green
                            )
                        }
                    }
                    item {
                        Button(
                            onClick = {
                                pollOptions = pollOptions.filter { it.value.isNotEmpty() }.toMutableList()
                                if (pollTitle.isNotEmpty() && pollDuration.isNotEmpty() && pollOptions.isNotEmpty()) {
                                    createPoll(pollTitle, pollDuration, pollOptions.map { it.value })
                                    showPollsOrCreate = false
                                }
                            },
                            modifier = Modifier.padding(5.dp),
                            enabled = pollOptions.any { it.value.isNotEmpty() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Text("Create", color = Color.Black)
                        }
                    }

                }


            }
        }
    }

}