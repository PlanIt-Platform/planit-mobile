package com.example.planit_mobile.services.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.sessionStorage.SessionDataStore
import kotlinx.coroutines.launch

suspend fun <T> executeRequest(
    request: suspend () -> T,
    onSuccess: suspend (T) -> Unit
){
    val response = runCatching { request() }
    if (response.isSuccess) {
        response.getOrNull()?.let { onSuccess(it) }
    }
}

fun <T> ViewModel.launchAndRequest(
    request: suspend () -> T,
    onSuccess: suspend (T) -> Unit
) {
    viewModelScope.launch {
        executeRequest(request, onSuccess)
    }
}


fun <T> ViewModel.launchAndAuthenticateRequest(
    request: suspend (accessToken: String, refreshToken: String, userId: Int) -> T,
    onSuccess: suspend (T, accessToken: String, refreshToken: String, userID: Int) -> Unit = { _, _, _, _ -> },
    sessionStorage: SessionDataStore
) {
    viewModelScope.launch {
        val userAccessToken = sessionStorage.getSessionAccessToken()
        val userRefreshToken = sessionStorage.getSessionRefreshToken()
        val userID =  sessionStorage.getUserID()

        if (userAccessToken == null || userRefreshToken == null  || userID == null) {
            return@launch
        }
        val result = request(userAccessToken, userRefreshToken, userID)
        executeRequest(
            request = { result },
            onSuccess = { onSuccess(result, userAccessToken, userRefreshToken, userID) }
        )
    }
}

