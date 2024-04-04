package com.example.planit_mobile.ui.screens.auth.register

import com.example.planit_mobile.ui.screens.common.LoadState

sealed class RegisterState : LoadState<Nothing>()
data object Step1State : RegisterState()
data object Step2State : RegisterState()
data object Step3State: RegisterState()

fun step1(): Step1State = Step1State
fun step2(): Step2State = Step2State
fun step3(): Step3State = Step3State