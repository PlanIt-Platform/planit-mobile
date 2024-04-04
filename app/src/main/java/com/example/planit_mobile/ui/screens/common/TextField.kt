package com.example.planit_mobile.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(
    text: String,
    onTextChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    singleLine: Boolean = true,
    error: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLength: Int,
    padding: Dp
) {
    val textLimitExceeded = text.length > maxLength
    val limitedText = if (textLimitExceeded) text.substring(0, maxLength) else text
    return OutlinedTextField(
        value = limitedText,
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = "Icon", tint = Color.White) },
        onValueChange = {
                var newText = it
                if (it.contains("\n")) {
                newText = it.replace("\n", "")
            }
            onTextChange(newText)
        },
        label = { Text(text = label, color = Color.White) },
        singleLine = singleLine,
        isError = error,
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color(0xFF1EABBD),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        ),
        modifier = Modifier
            .scale(0.8f)
            .padding(top = padding)
    )
}