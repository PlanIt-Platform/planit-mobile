package com.example.planit_mobile.ui.screens.auth.register.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.ui.screens.auth.register.RegisterState
import com.example.planit_mobile.ui.screens.auth.register.Step3State
import com.example.planit_mobile.ui.screens.common.Title

@Composable
fun Step2(
    userInterests: List<String>,
    setInterests: (List<String>) -> Unit,
    setStep: (RegisterState) -> Unit
) {
    var interestInput by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf(userInterests) }
    var invalidInterest by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(y = 80.dp)
    ) {
        Title(text = "What are you interested in?", Color.White, 30.sp)
        TextField(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 20.dp),
            value = interestInput,
            onValueChange = {
                interestInput = it
                invalidInterest = interestInput in interests
            },
            label = { Text("Interest") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (interestInput.isNotBlank() && interestInput !in interests) {
                    interests = interests + interestInput
                    interestInput = ""
                }
            }),
            trailingIcon = {
                IconButton(onClick = {
                    if (interestInput.isNotBlank() && interestInput !in interests) {
                        interests = interests + interestInput
                        interestInput = ""
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Interest")
                }
            },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "") }
        )
        if (invalidInterest) {
            Text("Interest is already in Interest List", color = Color.Red)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 16.dp)
                .heightIn(max = 200.dp)
        ) {
            LazyColumn(modifier = Modifier.align(Alignment.Center)) {
                item {
                    interests.forEach { interest ->
                        val truncatedInterest =
                            if (interest.length > 8) interest.take(8) + "..." else interest
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(13.dp)
                                )
                                .padding(vertical = 8.dp)
                                .width(150.dp)
                                .height(30.dp)
                                .wrapContentHeight()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = truncatedInterest,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 10.dp),
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                IconButton(
                                    onClick = {
                                        interests = interests.filterNot { it == interest }
                                    },
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove"
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier
                            .height(8.dp)
                            .width(10.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                setInterests(interests)
                setStep(Step3State)
            },
            content = { Text("Next") },
        )
    }
}