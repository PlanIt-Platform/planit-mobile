package com.example.planit_mobile.ui.screens.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorPopup(showDialog: Boolean, errorMessage: String, onDialogDismiss: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDialogDismiss()
            },
            title = {
                Text(text = "Error")
            },
            text = {
                Text(text = errorMessage)
            },
            confirmButton = {
                Button(onClick = {
                    onDialogDismiss()
                }) {
                    Text("OK")
                }
            }
        )
    }
}