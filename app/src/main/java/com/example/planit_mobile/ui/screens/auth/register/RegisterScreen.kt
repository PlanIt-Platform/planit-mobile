package com.example.planit_mobile.ui.screens.auth.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.ui.screens.auth.register.steps.Step1
import com.example.planit_mobile.ui.screens.auth.register.steps.Step2
import com.example.planit_mobile.ui.screens.auth.register.steps.Step3
import com.example.planit_mobile.ui.screens.common.Title
import com.example.planit_mobile.ui.screens.common.BackArrow
import com.example.planit_mobile.ui.screens.common.LoadState
import com.example.planit_mobile.ui.screens.common.TextField
import com.example.planit_mobile.ui.screens.common.getOrNull


@Composable
fun RegisterScreen(
    onRegister: (String, String, String, String) -> Unit,
    onEdit: (String, List<String>, String) -> Unit,
    onBackRequested: () -> Unit,
    showError: Boolean,
    userCreationSuccessful: Boolean,
    dismissUserCreationSuccess: () -> Unit,
    steps: LoadState<RegisterState>,
    setSteps: (RegisterState) -> Unit
) {
    var userInfo by remember { mutableStateOf(
        UserInfo(0, "", "", "", "", "", listOf()))
    }
    if (showError) setSteps(Step1State)
    Box( modifier = Modifier.fillMaxSize() ){
        BackArrow (Color.White) { onBackRequested() }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
        ) {
            when (steps) {
                Step1State -> {
                    Step1(
                        userInfo = userInfo,
                        setUserInfo = { userInfo = it },
                        onRegister = onRegister
                    )
                }
                Step2State -> {
                    Step2(
                        userInterests = userInfo.interests,
                        setInterests = { newInterests ->
                            userInfo = userInfo.copy(interests = newInterests)
                        },
                        setStep = { setSteps(it) }
                    )
                }

                Step3State -> {
                    Step3(
                        userInfo = userInfo,
                        setDescription = { userInfo = userInfo.copy(description = it) },
                        onEdit = onEdit
                    )
                }
            }
        }
        if(userCreationSuccessful) {
            Toast.makeText(LocalContext.current, "User Created Successfully", Toast.LENGTH_SHORT).show()
            dismissUserCreationSuccess()
        }
    }
}