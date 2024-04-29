package com.example.planit_mobile.ui.screens.auth.guest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.UserService
import kotlinx.coroutines.launch

class GuestViewModel(
    private val service: UserService,
    private val sessionStorage: SessionDataStore
): ViewModel() {
    companion object {
        fun factory(service: UserService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { GuestViewModel(service, sessionStorage) }
        }
    }

    enum class GuessState {
        Idle,
        Loading,
        Loaded,
    }

    suspend fun isLogged() : Boolean {
        val isLoggedIn = kotlin.runCatching { sessionStorage.isLogged() }.getOrNull()
        return isLoggedIn ?: false
    }

}