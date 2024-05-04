package com.example.planit_mobile.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.planit_mobile.domain.User
import com.example.planit_mobile.ui.screens.common.BotBar
import com.example.planit_mobile.ui.screens.common.NavigationHandlers
import com.example.planit_mobile.ui.screens.profile.UserProfileScreen

@Composable
fun HomeScreen(
    onLogoutRequested: () -> Unit,
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
        BackgroundBox(
            it,
            onLogoutRequested
        )
    }
}

@Composable
fun BackgroundBox(
    it: PaddingValues,
    onLogoutRequested: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row {

            }
            Column {
                Button(
                    onClick =  onLogoutRequested,
                    content = { Text("Logout") },
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewUserProfileScreen() {
    HomeScreen(
        onLogoutRequested = {},
        onProfileRequested = {},
        onHomeRequested = {},
        onEventsRequested = {},
    )
}