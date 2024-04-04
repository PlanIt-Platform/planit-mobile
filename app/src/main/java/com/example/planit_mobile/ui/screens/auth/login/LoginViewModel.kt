package com.example.planit_mobile.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.utils.launchAndRequest
import com.example.planit_mobile.ui.screens.auth.succeeded
import com.example.planit_mobile.ui.screens.common.LoadState
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.common.loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class LoginViewModel(
    private val userService: UserService,
    private val sessionStorage: SessionDataStore,
) : ViewModel() {

    companion object {
        fun factory(userService: UserService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { LoginViewModel(userService, sessionStorage) }
        }
    }

    private val loadStateFlow: MutableStateFlow<LoadState<String>> = MutableStateFlow(idle())

    val loadState: Flow<LoadState<String>>
        get() = loadStateFlow.asStateFlow()

    fun login(emailOrName: String, password: String) {
        loadStateFlow.value = loading()
        launchAndRequest(
            request = { userService.login(emailOrName, password) },
            onSuccess = {
                sessionStorage.setSession(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken,
                    userID = it.id,
                )
                loadStateFlow.value = succeeded()
            }
        )
    }
}
