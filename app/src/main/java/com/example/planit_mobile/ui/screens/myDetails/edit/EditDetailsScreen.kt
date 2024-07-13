package com.example.planit_mobile.ui.screens.myDetails.edit

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun EditDetailsScreen(
    onBackRequested: () -> Unit,
    onEdit: (String, List<String>, String) -> Unit,
    userInfo: User
) {
    var name by remember(userInfo) { mutableStateOf(userInfo.name) }
    var description by remember(userInfo) { mutableStateOf(userInfo.description) }
    var interestInput by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf(userInfo.interests) }
    var invalidInterest by remember { mutableStateOf(false) }

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
            Column {
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

                // Interests
                Column (modifier = Modifier.padding(10.dp).fillMaxHeight(0.5f)) {
                    TextField(
                        value = interestInput,
                        onValueChange = {
                            interestInput = it
                            invalidInterest = interestInput in interests
                        },
                        label = { Text("Interest") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (interestInput.isNotBlank() && interestInput !in interests) {
                                interests = interests + interestInput
                                interestInput = ""
                            }
                        }),
                        trailingIcon = {
                            IconButton(onClick = {
                                if (interestInput.isNotBlank() && interestInput !in interests) {
                                    interests = interests + interestInput
                                    interestInput = ""
                                }
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Interest")
                            }
                        },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "") }
                    )
                    if (invalidInterest) {
                        Text("Interest is already in Interest List", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                    ) {
                        Column (modifier = Modifier
                            .verticalScroll(rememberScrollState(), true).fillMaxHeight()
                        ) {
                            interests.forEach { interest ->
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row {
                                        Text(
                                            text = interest,
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                        IconButton(onClick = {
                                            interests =
                                                interests.filterNot { it == interest }
                                        }) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Remove"
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        }
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
                            onEdit(name, interests, description)
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
