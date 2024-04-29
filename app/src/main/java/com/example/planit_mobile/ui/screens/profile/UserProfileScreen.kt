package com.example.planit_mobile.ui.screens.profile

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.planit_mobile.ui.screens.common.backArrow

@Composable
fun UserProfileScreen(
    userInfo: User,
    onBackRequested: () -> Unit,
    onProfileRequested: () -> Unit,
    onHomeRequested: () -> Unit,
    onEventsRequested: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BotBar(navigation =
                NavigationHandlers(
                    onProfileRequested = onProfileRequested,
                    onHomeRequested = onHomeRequested,
                    onEventsRequested = onEventsRequested
                )
            )
        },
    ) {
        UpperHalf(padding = it, userInfo = userInfo, onBackRequested)
        MiddleSection(userInfo = userInfo)
        LowerHalf(userInfo = userInfo)
    }
}

@Composable
fun UpperHalf(padding: PaddingValues, userInfo: User, onBackRequested: () -> Unit){
    Box(
        modifier = Modifier
            .padding(padding)
            .height(275.dp)
            .background(color = Color(28, 155, 139, 255))
    ) {
        backArrow (Color.White) { onBackRequested() }
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
            listOf("Sports and Outdoor", "Culture", "Education", "Entertainment", /*"Volunteering"*/)),
        onBackRequested = {},
        onProfileRequested = {},
        onHomeRequested = {},
        onEventsRequested = {}
    )
}