package com.example.planit_mobile.ui.screens.auth.register.steps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.ui.screens.auth.register.UserInfo
import com.example.planit_mobile.ui.screens.common.TextField
import com.example.planit_mobile.ui.screens.common.Title

@Composable
fun Step3(
    userInfo: UserInfo,
    setDescription: (String) -> Unit,
    onEdit: (String, List<String>, String) -> Unit
) {
    Column(
        modifier = Modifier.offset(y = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
}
