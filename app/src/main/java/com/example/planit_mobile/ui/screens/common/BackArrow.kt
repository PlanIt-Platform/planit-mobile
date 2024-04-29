package com.example.planit_mobile.ui.screens.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun backArrow(
    color: Color = Color.Black,
    onBackRequested: () -> Unit
) {
    IconButton(onClick = { onBackRequested() }) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = color)
    }
}