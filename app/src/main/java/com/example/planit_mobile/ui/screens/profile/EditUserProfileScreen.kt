package com.example.planit_mobile.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.domain.User
import com.example.planit_mobile.ui.screens.common.BackArrow
import com.example.planit_mobile.ui.screens.common.Title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserProfileScreen(
    categories: List<String>,
    onBackRequested: () -> Unit,
    onEdit: (String, List<String>, String) -> Unit,
    userInfo: User
) {
    var name by remember(userInfo) { mutableStateOf(userInfo.name) }
    var description by remember(userInfo) { mutableStateOf(userInfo.description) }
    var selectedInterests by remember(userInfo) { mutableStateOf(userInfo.interests) }

    val scrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(5, 13, 36, 33) //5, 13, 36, 33   0x54050D24
                ),
                title = {
                    Title(
                        text = "Edit Profile",
                        color = Color.White,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    BackArrow(Color.White) { onBackRequested() }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            LazyColumn (state = scrollState) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(
                            "Edit Your Profile Name",
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier.padding(start = 5.dp, top = 10.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            maxLines = 2,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(
                            "Interests",
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }

                    val interests = categories.filter { it != "Simple Meeting" }

                    interests.forEach { category ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = category in selectedInterests,
                                onCheckedChange = {
                                    selectedInterests = if (category in selectedInterests) {
                                        selectedInterests.filter { it != category }
                                    } else {
                                        selectedInterests + category
                                    }
                                }
                            )
                            Text(category, color = Color.White)
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(
                            "Description",
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        TextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 5.dp, bottom = 30.dp)
                    ) {
                        Button(
                            onClick = {
                                onEdit(name, selectedInterests, description)
                            },
                            content = { Text("Submit") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                        )
                    }
                }
            }
        }
    }

}
