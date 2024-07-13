package com.example.planit_mobile.ui.screens.auth.register.steps

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.ui.screens.auth.register.UserInfo
import com.example.planit_mobile.ui.screens.common.TextField
import com.example.planit_mobile.ui.screens.common.Title

@Composable
fun Step1(
    userInfo: UserInfo,
    setUserInfo: (UserInfo) -> Unit,
    onRegister: (String, String, String, String) -> Unit
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
        padding = 0.dp
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
        },
        content = { Text("Next") },
    )
}