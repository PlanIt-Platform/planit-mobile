package com.example.planit_mobile.ui.screens.auth.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.ui.screens.common.Title
import com.example.planit_mobile.ui.screens.common.TextField
import com.example.planit_mobile.ui.screens.common.BackArrow


@Composable
fun RegisterScreen(
    onRegister: (String, String, String, String) -> Unit,
    onEdit: (String, List<String>, String) -> Unit,
    onBackRequested: () -> Unit,
    showError: Boolean,
    categories: List<String>
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
                        setInterests = { newInterest ->
                            userInfo = userInfo.copy(
                                interests = userInfo.interests + newInterest
                            )
                        },
                        setStep = { steps = it },
                        categories = categories
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
    setInterests: (String) -> Unit,
    setStep: (RegisterState) -> Unit,
    categories: List<String>
) {
    val interests = categories.filter { it != "Simple Meeting" }
    Title(text = "What are you interested in?", Color.White, 30.sp)
    Column {
        for (interest in interests) {
            var checked by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { setInterests(interest); checked = it },
                    colors = CheckboxDefaults.colors(
                        checkmarkColor = Color.White,
                        checkedColor = Color.White,
                        uncheckedColor = Color.White
                    )
                )
                Text(text = interest, color = Color.White, fontSize = 15.sp)
            }
        }
    }
    Button(
        onClick = {
            setStep(Step3State)
        },
        content = { Text("Next") },
    )
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
