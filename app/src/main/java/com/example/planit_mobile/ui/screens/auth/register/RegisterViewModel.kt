package com.example.planit_mobile.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.utils.launchAndAuthenticateRequest
import com.example.planit_mobile.services.utils.launchAndRequest
import com.example.planit_mobile.ui.screens.common.LoadState
import com.example.planit_mobile.ui.screens.common.errorMessage
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.common.loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.planit_mobile.ui.screens.common.Error

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
    private val errorStateFlow: MutableStateFlow<Error> = MutableStateFlow(Error(""))

    val loadState: Flow<LoadState<Int>>
        get() = loadStateFlow.asStateFlow()
    val errorState: Flow<Error>
        get() = errorStateFlow.asStateFlow()

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
            },
            onFailure = { errorStateFlow.value = errorMessage(it.message.toString()) }
        )
    }

    fun editUser(name: String, interests: String, description: String) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, userId ->
                userService.editUser(userAccessToken, userRefreshToken, name, interests.split(","), description)
                      },
            onSuccess = { _, _, _, _ ->
                loadStateFlow.value = step3()
            },
            onFailure = { errorStateFlow.value = errorMessage(it.message.toString()) },
            sessionStorage = sessionStorage
        )
    }

    fun dismissError() {
        errorStateFlow.value = Error("")
    }
}
