package com.example.planit_mobile.ui.screens.auth

import com.example.planit_mobile.ui.screens.common.LoadState

sealed class AuthState<out T> : LoadState<T>()
data object Success : AuthState<Nothing>()

fun succeeded(): Success = Success
