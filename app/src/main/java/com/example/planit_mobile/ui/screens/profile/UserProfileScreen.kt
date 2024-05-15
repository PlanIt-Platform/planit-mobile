package com.example.planit_mobile.ui.screens.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.domain.User
import com.example.planit_mobile.ui.screens.common.BotBar
import com.example.planit_mobile.ui.screens.common.NavigationHandlers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userInfo: User,
    onProfileRequested: () -> Unit,
    onHomeRequested: () -> Unit,
    onEventsRequested: () -> Unit,
    onLogoutRequested: () -> Unit,
    onEditProfileRequested: () -> Unit
) {
    val dropdownMenuExpanded = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BotBar(navigation = NavigationHandlers(
                    onProfileRequested = onProfileRequested,
                    onHomeRequested = onHomeRequested,
                    onEventsRequested = onEventsRequested
                )
            )
        },
        containerColor = Color(240, 240, 240, 255),
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            UpperHalf(padding = it, userInfo = userInfo)
            MiddleSection(userInfo = userInfo)
            LowerHalf(userInfo = userInfo)
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .offset(y = 0.dp),
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = { dropdownMenuExpanded.value = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = dropdownMenuExpanded.value,
                        onDismissRequest = { dropdownMenuExpanded.value = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            dropdownMenuExpanded.value = false
                            onEditProfileRequested()
                        }, text = {
                            Text(text = "Edit Profile", fontSize = 16.sp)
                        })
                        DropdownMenuItem(onClick = {
                            dropdownMenuExpanded.value = false
                            onLogoutRequested()
                        }, text = {
                            Text(text = "Logout", fontSize = 16.sp)
                        })
                    }
                }
            )
        }
    }
}

@Composable
fun UpperHalf(padding: PaddingValues, userInfo: User){
    Box(
        modifier = Modifier
            .padding(padding)
            .height(275.dp)
            .background(color = Color(28, 185, 165, 255))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(
                modifier = Modifier.size(150.dp)
            ) {
                drawCircle(
                    color = Color.White
                )
            }
            Text(
                text = userInfo.name,
                style = androidx.compose.ui.text.TextStyle(color = Color.White),
                fontSize = 24.sp,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun MiddleSection(userInfo: User){
    Box(
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 235.dp)
            .height(95.dp)
            .width(500.dp)
            .background(Color.White)
            .shadow(2.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "@" + userInfo.username,
                style = androidx.compose.ui.text.TextStyle(color = Color.Black),
                fontSize = 16.sp
            )
            Text(
                text = userInfo.email,
                style = androidx.compose.ui.text.TextStyle(color = Color.Black),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun LowerHalf(userInfo: User){
    Box(modifier = Modifier.padding(0.dp, 340.dp, 0.dp, 0.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = "Interests:",
                modifier = Modifier.padding(20.dp),
                style = androidx.compose.ui.text.TextStyle(color = Color.Black),
                fontSize = 20.sp
            )
            val chunkedInterests = userInfo.interests.chunked(3)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (interestsColumn in chunkedInterests) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Top,
                    ) {
                        for (interest in interestsColumn) {
                            Text(
                                text = "• $interest",
                                style = androidx.compose.ui.text.TextStyle(color = Color.Black),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            Text(
                text = "Description:",
                modifier = Modifier.padding(20.dp),
                style = androidx.compose.ui.text.TextStyle(color = Color.Black),
                fontSize = 20.sp
            )
            Text(
                text = userInfo.description,
                modifier = Modifier.padding(horizontal = 20.dp),
                style = androidx.compose.ui.text.TextStyle(color = Color.Black),
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewUserProfileScreen() {
    UserProfileScreen(
        userInfo = User(
            1,
            "Daniel",
            "Daniel2003",
            "Im nice",
            "daniel@gmail.com",
            listOf("Sports and Outdoor", "Culture", "Education", "Entertainment"/*, "Volunteering"*/)),
        onProfileRequested = {},
        onHomeRequested = {},
        onEventsRequested = {},
        onLogoutRequested = {},
        onEditProfileRequested = {}
    )
}