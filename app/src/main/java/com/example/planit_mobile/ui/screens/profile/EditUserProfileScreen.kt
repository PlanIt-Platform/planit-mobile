package com.example.planit_mobile.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.ui.screens.common.BackArrow
import com.example.planit_mobile.ui.screens.common.Title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserProfileScreen(
    categories: List<String>,
    onBackRequested: () -> Unit,
    onEdit: (String, String, String) -> Unit,

) {
    Box (
        modifier = Modifier.fillMaxSize()
    ){
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .offset(y = 0.dp),
            title = { Title("Edit Profile", Color.White, 24.sp) },
            actions = {
                Row {
                    BackArrow(Color.White) { onBackRequested() }
                }
            }
        )

    }
    Column {
        categories.forEach {
            Text(text = it)
        }
    }
}
