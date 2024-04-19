package com.example.planit_mobile.services.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.sessionStorage.SessionDataStore
import kotlinx.coroutines.launch

suspend fun <T> executeRequest(
    request: suspend () -> T,
    onSuccess: suspend (T) -> Unit,
    onFailure: suspend (Throwable) -> Unit
){
    val response = runCatching { request() }
    if (response.isSuccess) {
        response.getOrNull()?.let { onSuccess(it) }
    }
    if(response.isFailure){
        onFailure(response.exceptionOrNull()!!)
    }
}

fun <T> ViewModel.launchAndRequest(
    request: suspend () -> T,
    onSuccess: suspend (T) -> Unit,
    onFailure: suspend (Throwable) -> Unit
) {
    viewModelScope.launch {
        executeRequest(request, onSuccess, onFailure)
    }
}


fun <T> ViewModel.launchAndAuthenticateRequest(
    request: suspend (accessToken: String, refreshToken: String, userId: Int) -> T,
    onSuccess: suspend (T, accessToken: String, refreshToken: String, userID: Int) -> Unit = { _, _, _, _ -> },
    onFailure: suspend (Throwable) -> Unit,
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
            onSuccess = { onSuccess(result, userAccessToken, userRefreshToken, userID) },
            onFailure = onFailure
        )
    }
}

