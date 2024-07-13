package com.example.planit_mobile.ui.screens.otheruserprofile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.planit_mobile.R
import com.example.planit_mobile.domain.User
import com.example.planit_mobile.ui.screens.common.BackArrow

@Composable
fun OtherUserScreen(
    userInfo: User?,
    onBackRequested : () -> Unit
) {
    if (userInfo != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(Color(0xFF222A79))
            ) {
                BackArrow(Color.Black) {
                    onBackRequested()
                }
                Canvas(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = 35.dp)
                ) {
                    drawCircle(color = Color.White)
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 35.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_icon_2),
                        contentDescription = "Profile Icon",
                        modifier = Modifier
                            .requiredSize(100.dp)
                    )
                    Text(
                        text = userInfo.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "@" + userInfo.username,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Text(
                        text = userInfo.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column {
                    Text(
                        text = "Interests",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    if (userInfo.interests.isNotEmpty()){
                        userInfo.interests.forEach { interest ->
                            Text(
                                text = interest,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        Text(
                            text = userInfo.name + " has no Interests",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    if (userInfo.description != ""){
                        Text(
                            text = userInfo.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        Text(
                            text = userInfo.name + " has no Description",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
