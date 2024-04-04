package com.example.planit_mobile.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.utils.launchAndAuthenticateRequest
import com.example.planit_mobile.services.utils.launchAndRequest
import com.example.planit_mobile.ui.screens.common.LoadState
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.common.loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel(
    private val userService: UserService,
    private val sessionStorage: SessionDataStore,
) : ViewModel() {

    companion object {
        fun factory(userService: UserService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { RegisterViewModel(userService, sessionStorage) }
        }
    }

    private val loadStateFlow: MutableStateFlow<LoadState<Int>> = MutableStateFlow(idle())

    val loadState: Flow<LoadState<Int>>
        get() = loadStateFlow.asStateFlow()

    fun register(username: String, name: String, email: String, password: String) {
        loadStateFlow.value = loading()
        launchAndRequest(
            request = { userService.register(username, name, email, password) },
            onSuccess = {
                sessionStorage.setSession(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken,
                    userID = it.id,
                )
                loadStateFlow.value = step1()
            }
        )
    }

    fun editUser(name: String, interests: String, description: String) {
        launchAndAuthenticateRequest(
            request = { _, _, userId ->
                userService.editUser(userId, name, interests.split(","), description)
                      },
            onSuccess = { _, _, _, _ ->
                loadStateFlow.value = step3()
            },
            sessionStorage = sessionStorage
        )
    }
}
