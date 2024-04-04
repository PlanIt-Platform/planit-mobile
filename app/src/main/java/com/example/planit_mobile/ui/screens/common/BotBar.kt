package com.example.planit_mobile.ui.screens.common

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.planit_mobile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotBar(navigation: NavigationHandlers = NavigationHandlers()) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 30.dp, top = 40.dp),
            ) {
            }
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (navigation.onEventsRequested != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    ) {
                        IconButton(
                            onClick = navigation.onEventsRequested,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.binoculars_icon),
                                contentDescription = stringResource(id = R.string.bot_bar_navigate_to_events),
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Events",
                            color = Color.White
                        )
                    }
                }
                if (navigation.onHomeRequested != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    ) {
                        IconButton(
                            onClick = navigation.onHomeRequested,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.home_icon),
                                contentDescription = stringResource(id = R.string.bot_bar_navigate_to_home),
                                tint = Color.White,
                            )
                        }
                        Text(
                            text = "Home",
                            color = Color.White
                        )
                    }
                }
                if (navigation.onProfileRequested != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    ) {
                        IconButton(
                            onClick = navigation.onProfileRequested,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.profile_icon),
                                contentDescription = stringResource(id = R.string.bot_bar_navigate_to_profile),
                                tint = Color.White,
                            )
                        }
                        Text(
                            text = "Profile",
                            color = Color.White
                        )
                    }
                }
            }
        },
        colors = topAppBarColors(containerColor = Color(24, 38, 44, 255)),
        modifier = Modifier.height(85.dp)
    )
}