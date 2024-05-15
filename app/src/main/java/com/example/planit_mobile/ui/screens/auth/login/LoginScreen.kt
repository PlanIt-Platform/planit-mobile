package com.example.planit_mobile.ui.screens.auth.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
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
import com.example.planit_mobile.ui.screens.common.TextField
import com.example.planit_mobile.ui.screens.common.Title
import com.example.planit_mobile.ui.screens.common.BackArrow


@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onBackRequested: () -> Unit,
) {
    var emailOrName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box( modifier = Modifier.fillMaxSize() ){
        BackArrow (Color.White){ onBackRequested() }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 250.dp),
        ) {
            Title(text = "Who are you?", Color.White, 30.sp)
            TextField(
                text = emailOrName,
                onTextChange = { emailOrName = it },
                label = "Email Or username",
                leadingIcon = Icons.Default.Email,
                singleLine = true,
                maxLength = 30,
                padding = 20.dp
            )
            TextField(
                text = password,
                onTextChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                maxLength = 30,
                padding = 5.dp
            )
            Button(
                onClick = { onLogin(emailOrName, password) },
                content = { Text("Login") },
            )
        }
    }
}