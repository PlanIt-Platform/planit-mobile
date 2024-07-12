package com.example.planit_mobile.ui.screens.auth.guest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore

class GuestViewModel(
    private val sessionStorage: SessionDataStore
): ViewModel() {
    companion object {
        fun factory(sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { GuestViewModel(sessionStorage) }
        }
    }

    suspend fun isLogged() : Boolean {
        val isLoggedIn = kotlin.runCatching { sessionStorage.isLogged() }.getOrNull()
        return isLoggedIn ?: false
    }

}