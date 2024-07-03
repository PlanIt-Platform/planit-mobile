package com.example.planit_mobile.ui.screens.eventDetails

//noinspection SuspiciousImport
import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.planit_mobile.services.models.UsersInEvent
import com.example.planit_mobile.ui.screens.common.buildAnnotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSheet(
    usersInEvent: List<UsersInEvent>,
    onDismiss: () -> Unit,
    isUserOrganizer: Boolean = false,
    kickUser: (Int) -> Unit,
    assignRole: (Int) -> Unit,
    removeRole: (Int, Int) -> Unit,
    userID : Int?,
    onNavigateToOtherUserProfile: (Int) -> Unit
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
            kickUser,
            assignRole,
            removeRole,
            userID,
            onNavigateToOtherUserProfile = onNavigateToOtherUserProfile
        )
    }
}

@Composable
fun UserList(
    usersInEvent: List<UsersInEvent>,
    isUserOrganizer: Boolean,
    kickUser: (Int) -> Unit,
    assignRole: (Int) -> Unit,
    removeRole: (Int, Int) -> Unit,
    userID : Int?,
    onNavigateToOtherUserProfile: (Int) -> Unit
) {
    var editMode by remember { mutableStateOf(false) }

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Person,
                        contentDescription = "User",
                        tint = Color.White
                    )
                    if (user.id != userID){
                        ClickableText(
                            text = buildAnnotatedString("", user.username),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 10.dp),
                            onClick = {
                                onNavigateToOtherUserProfile(user.id)
                            }
                        )
                    }else{
                        Text(
                            text = user.username,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 10.dp),
                            color = Color.White
                        )
                    }
                    if (isUserOrganizer && editMode) {
                        Spacer(modifier = Modifier.weight(1f))
                        if (userID != user.id) {
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
                            if(user.roleName != "Organizer"){
                                IconButton(
                                    onClick = { assignRole(user.id) },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color(
                                            0xFF15AD18
                                        )
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.AccountCircle,
                                        contentDescription = "Assign Role",
                                        tint = Color.White
                                    )
                                }
                            }
                            if (user.roleId != null && user.roleName != "Participant") {
                                IconButton(
                                    onClick = { removeRole(user.id, user.roleId) },
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Red)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Remove Role",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = user.roleName ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}