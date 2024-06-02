package com.example.planit_mobile.ui.screens.common

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun buildAnnotatedString(boldText: String, normalText: String) =
    androidx.compose.ui.text.buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(boldText)
        }
        append(normalText)
    }