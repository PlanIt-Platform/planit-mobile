package com.example.planit_mobile.ui.screens.eventDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.planit_mobile.services.models.EventModel
import com.example.planit_mobile.services.models.Message
import com.example.planit_mobile.services.models.PollModel
import com.example.planit_mobile.services.models.UsersInEvent
import com.example.planit_mobile.ui.screens.common.BackArrow

@Composable
fun EventDetailsScreen(
    onBackRequested: () -> Unit,
    eventDetails: EventModel?,
    isUserInEvent: Boolean,
    joinEvent: (String) -> Unit,
    usersInEvent: List<UsersInEvent>?,
    isUserOrganizer : Boolean,
    leaveEvent: () -> Unit,
    editEvent: (
        String, String?, String, String?, String?, String, String, String?, String, String
    ) -> Unit,
    deleteEvent: () -> Unit,
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    subCategories: List<String>,
    updateUsersInEvent: () -> Unit,
    removeUserTask: (Int, Int) -> Unit,
    assignUserTask: (Int, String) -> Unit,
    kickUser: (Int) -> Unit,
    userID : Int?,
    getPolls: () -> Unit,
    polls: List<PollModel>?,
    createPoll: (String, String, List<String>) -> Unit,
    deletePoll: (Int) -> Unit,
    voteOnPoll: (Int, Int) -> Unit,
    sendMessage: (String) -> Unit,
    messages: List<Message>
) {
    var showUserSheet by remember { mutableStateOf(false) }
    var showPollsDialog by remember { mutableStateOf(false) }
    var showChatDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            EventDetailsTopBar (
                onBackRequested = onBackRequested,
                userInEvent = isUserInEvent,
                joinEvent = joinEvent,
                accessPolls = {
                    getPolls()
                    showPollsDialog = true
                },
                viewParticipants = {
                    showUserSheet = true
                    updateUsersInEvent()
                }
            )
        },
        floatingActionButton = {
            if (isUserInEvent) {
                FloatingActionButton(
                    onClick = { showChatDialog = true },
                    shape = CircleShape,
                    modifier = Modifier.offset(y = (-25).dp),
                    containerColor = Color.LightGray
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.stat_notify_chat),
                        contentDescription = "Chat",
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            if(eventDetails != null) {
                EventDetailsContent (
                    eventDetails = eventDetails,
                    isUserInEvent = isUserInEvent,
                    isUserOrganizer = isUserOrganizer,
                    leaveEvent = {leaveEvent()},
                    editEvent = { title, description, category, subCategory, location, visibility, date, endDate, price, password ->
                        editEvent(title, description, category, subCategory, location, visibility, date, endDate, price, password)
                    },
                    deleteEvent = {deleteEvent()},
                    categories = categories,
                    onCategorySelected = onCategorySelected,
                    subCategories = subCategories
                )
            }
            if(showUserSheet) {
                UserSheet(
                    usersInEvent = usersInEvent ?: emptyList(),
                    onDismiss = { showUserSheet = false },
                    isUserOrganizer = isUserOrganizer,
                    removeUserTask = {userId, taskId ->
                        removeUserTask (userId, taskId)
                    },
                    assignUserTask = {userId, task ->
                        assignUserTask(userId, task)
                    },
                    kickUser = {userId ->
                        kickUser(userId)
                    },
                    userID = userID
                )
            }
            if(showPollsDialog){
                var createPollOptions by remember { mutableIntStateOf(1) }
                PollsDialog(
                    polls = polls ?: emptyList(),
                    createPoll = {pollTitle, duration, options ->
                        createPoll(pollTitle, duration, options)
                        showPollsDialog = false
                    },
                    onDismissRequest = { showPollsDialog = false },
                    isUserOrganizer = isUserOrganizer,
                    deletePoll = {pollId ->
                        deletePoll(pollId)
                    },
                    voteOnPoll = {pollId, optionId ->
                        voteOnPoll(pollId, optionId)
                    },
                    createPollOptions = createPollOptions,
                    setCreatePollOptions = { newNumber -> createPollOptions = newNumber}
                )
            }
            if(showChatDialog){
                ChatDialog(
                    onDismiss = { showChatDialog = false },
                    sendMessage = { message ->
                        sendMessage(message)
                    },
                    messages = messages
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsTopBar(
    onBackRequested: () -> Unit,
    userInEvent: Boolean,
    joinEvent: (String) -> Unit,
    accessPolls: () -> Unit,
    viewParticipants: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackArrow (Color.White) {
                onBackRequested()
            }
        },
        actions = {
            if (userInEvent) {
                IconButton(onClick = { accessPolls() }) {
                    Icon(
                        Icons.Filled.List,
                        contentDescription = "Access Polls",
                        tint = Color.LightGray,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 8.dp)
                    )
                }
                IconButton(onClick = { viewParticipants() }) {
                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = "View Participants",
                        tint = Color.LightGray,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                Button(onClick = { joinEvent("") }) {
                    Text("Join Event")
                }
            }
        },
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background)
    )
}
