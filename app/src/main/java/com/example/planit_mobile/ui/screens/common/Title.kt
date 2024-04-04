package com.example.planit_mobile.ui.screens.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit

@Composable
fun Title(text: String, color: Color, fontSize: TextUnit) {
    Text(
        text = text,
        fontSize = fontSize,
        color = color,
    )
}