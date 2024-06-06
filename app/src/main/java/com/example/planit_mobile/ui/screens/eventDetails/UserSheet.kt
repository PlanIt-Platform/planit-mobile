package com.example.planit_mobile.ui.screens.eventDetails

//noinspection SuspiciousImport
import android.R
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.planit_mobile.services.models.UsersInEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSheet(
    usersInEvent: List<UsersInEvent>,
    onDismiss: () -> Unit,
    isUserOrganizer: Boolean = false,
    removeUserTask: (Int, Int) -> Unit,
    assignUserTask: (Int, String) -> Unit,
    kickUser: (Int) -> Unit,
    userID : Int?
){
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        UserList(
            usersInEvent,
            isUserOrganizer,
            removeUserTask,
            assignUserTask,
            kickUser,
            userID
        )
    }
}

@Composable
fun UserList(
    usersInEvent: List<UsersInEvent>,
    isUserOrganizer: Boolean,
    removeUserTask: (Int, Int) -> Unit,
    assignUserTask: (Int, String) -> Unit,
    kickUser: (Int) -> Unit,
    userID : Int?
) {
    var editMode by remember { mutableStateOf(false) }
    var showAssignTaskDialog by remember { mutableStateOf(false) }
    var selectedUserId by remember { mutableIntStateOf(0) }
    var taskName by remember { mutableStateOf("") }
    var taskType by remember { mutableStateOf("") }
    var taskTypeExpanded by remember { mutableStateOf(false) }
    var otherOrganizers by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Participants",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                if (isUserOrganizer && !editMode) {
                    Button(
                        onClick = { editMode = true },
                    ) {
                        Text("Edit", color = Color.White)
                    }
                }
                if (editMode) {
                    Button(
                        onClick = { editMode = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15AD18))
                    ) {
                        Text("Done", color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            usersInEvent.forEach { user ->
                if (userID != user.id && user.taskName == "Organizer") otherOrganizers = true
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Person,
                        contentDescription = "User",
                        tint = Color.White
                    )
                    Text(
                        text = user.username,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    if (isUserOrganizer && editMode) {
                        Spacer(modifier = Modifier.weight(1f))
                        Log.d("User", userID.toString())
                        Log.d("UserID", user.id.toString())
                        Log.d("Organizers", otherOrganizers.toString())
                        if (user.taskId != null && (userID != user.id || otherOrganizers)) {
                            IconButton(
                                onClick = { removeUserTask(user.id, user.taskId) },
                                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.DarkGray)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_menu_delete),
                                    contentDescription = "Remove Task",
                                    tint = Color.White
                                )
                            }
                        }
                        if (userID != user.id) {
                            if(user.taskId == null || user.taskName == null || user.taskName == "") {
                                IconButton(
                                    onClick = {
                                        selectedUserId = user.id
                                        showAssignTaskDialog = true
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color(
                                            0xFF15AD18
                                        )
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_menu_add),
                                        contentDescription = "Assign Task",
                                        tint = Color.White
                                    )
                                }
                            }
                            IconButton(
                                onClick = { kickUser(user.id) },
                                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Red)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_menu_close_clear_cancel),
                                    contentDescription = "Kick User",
                                    tint = Color.White
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = user.taskName ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }

    if (showAssignTaskDialog) {
        AlertDialog(
            onDismissRequest = {
                showAssignTaskDialog = false
                taskName = ""
                taskType = ""
                taskTypeExpanded = false
            },
            title = { Text("Assign Task") },
            text = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Select Task", modifier = Modifier.padding(end = 7.dp))
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                                .background(Color.LightGray, RoundedCornerShape(4.dp))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(taskType, modifier = Modifier.padding(start = 7.dp))
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = { taskTypeExpanded = true }) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Select task type"
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = taskTypeExpanded,
                                onDismissRequest = { taskTypeExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Organizer") },
                                    onClick = {
                                        taskType = "Organizer"
                                        taskTypeExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Custom") },
                                    onClick = {
                                        taskType = "Custom"
                                        taskTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    if (taskType == "Custom") {
                        TextField(
                            value = taskName,
                            onValueChange = { taskName = it },
                            label = { Text("Task Name") },
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (taskName.lowercase() == "organizer") taskName = "Organizer"
                        assignUserTask(
                            selectedUserId,
                            if (taskType == "Custom") taskName else "Organizer"
                        )
                        taskName = ""
                        taskType = ""
                        showAssignTaskDialog = false
                    }
                ) {
                    Text("Assign")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        taskName = ""
                        taskType = ""
                        showAssignTaskDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}