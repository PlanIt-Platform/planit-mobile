package com.example.planit_mobile.ui.screens.auth.register

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.ui.screens.common.Title
import com.example.planit_mobile.ui.screens.common.BackArrow
import com.example.planit_mobile.ui.screens.common.TextField


@Composable
fun RegisterScreen(
    onRegister: (String, String, String, String) -> Unit,
    onEdit: (String, List<String>, String) -> Unit,
    onBackRequested: () -> Unit,
    showError: Boolean,
    userCreationSuccessful: Boolean,
    dismissUserCreationSuccess: () -> Unit
) {
    var steps by remember { mutableStateOf<RegisterState>(Step1State) }
    var userInfo by remember { mutableStateOf(
        UserInfo(0, "", "", "", "", "", listOf()))
    }
    if (showError) steps = Step1State
    Box( modifier = Modifier.fillMaxSize() ){
        BackArrow (Color.White) { onBackRequested() }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
        ) {
            when (steps) {
                Step1State -> {
                    Step1(
                        userInfo = userInfo,
                        setUserInfo = { userInfo = it },
                        onRegister = onRegister,
                        onNext = { steps = Step2State },
                    )
                }

                Step2State -> {
                    Step2(
                        userInterests = userInfo.interests,
                        setInterests = { newInterests ->
                            userInfo = userInfo.copy(interests = newInterests)
                        },
                        setStep = { steps = it }
                    )
                }

                Step3State -> {
                    Step3(
                        userInfo = userInfo,
                        setDescription = { userInfo = userInfo.copy(description = it) },
                        onEdit = onEdit
                    )
                }
            }
        }
        if(userCreationSuccessful) {
            Toast.makeText(LocalContext.current, "User Created Successfully", Toast.LENGTH_SHORT).show()
            dismissUserCreationSuccess()
        }
    }
}

@Composable
fun Step1(
    userInfo: UserInfo,
    setUserInfo: (UserInfo) -> Unit,
    onRegister: (String, String, String, String) -> Unit,
    onNext: () -> Unit
) {
    Title(text = "Join us!", Color.White, 30.sp)
    TextField(
        text = userInfo.username,
        onTextChange = {setUserInfo(userInfo.copy(username = it))},
        label = "Username",
        leadingIcon = Icons.Default.Face,
        singleLine = true,
        maxLength = 20,
        padding = 12.dp
    )
    TextField(
        text = userInfo.name,
        onTextChange = {setUserInfo(userInfo.copy(name = it))},
        label = "Name",
        leadingIcon = Icons.Default.Face,
        singleLine = true,
        maxLength = 20,
        padding = 12.dp
    )
    TextField(
        text = userInfo.email,
        onTextChange = {setUserInfo(userInfo.copy(email = it))},
        label = "Email",
        leadingIcon = Icons.Default.Email,
        singleLine = true,
        maxLength = 30,
        padding = 0.dp
    )
    TextField(
        text = userInfo.password,
        onTextChange = {setUserInfo(userInfo.copy(password = it))},
        label = "Password",
        leadingIcon = Icons.Default.Lock,
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        maxLength = 30,
        padding = 0.dp
    )
    Button(
        onClick = {
            onRegister(userInfo.username, userInfo.name, userInfo.email, userInfo.password)
            onNext()
        },
        content = { Text("Next") },
    )
}

@Composable
fun Step2(
    userInterests: List<String>,
    setInterests: (List<String>) -> Unit,
    setStep: (RegisterState) -> Unit
) {
    var interestInput by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf(userInterests) }
    var invalidInterest by remember { mutableStateOf(false) }

    Title(text = "What are you interested in?", Color.White, 30.sp)
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        androidx.compose.material3.TextField(
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
        Box(modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.6f)){
            LazyColumn {
                item {
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
                                    interests = interests.filterNot { it == interest }
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                setInterests(interests)
                setStep(Step3State)
            },
            content = { Text("Next") },
        )
    }
}

@Composable
fun Step3(
    userInfo: UserInfo,
    setDescription: (String) -> Unit,
    onEdit: (String, List<String>, String) -> Unit
) {
    Title(text = "Almost there!", Color.White, 30.sp)
    TextField(
        text = userInfo.description,
        onTextChange = setDescription,
        label = "Tell us about yourself...",
        leadingIcon = Icons.Default.Edit,
        singleLine = false,
        maxLength = 200,
        padding = 10.dp
    )
    Button(
        onClick = {
            onEdit(userInfo.name, userInfo.interests, userInfo.description)
        },
        content = { Text("Finish") },
    )
}
