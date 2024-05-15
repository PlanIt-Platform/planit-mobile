package com.example.planit_mobile.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.domain.User
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.utils.executeRequest
import com.example.planit_mobile.services.utils.launchAndAuthenticateRequest
import com.example.planit_mobile.services.utils.launchAndRequest
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.LoadState
import com.example.planit_mobile.ui.screens.common.errorMessage
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.common.loaded
import com.example.planit_mobile.ui.screens.common.loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val service: UserService,
    private val sessionStorage: SessionDataStore
) : ViewModel() {

    companion object {
        fun factory(service: UserService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { UserProfileViewModel(service, sessionStorage) }
        }
    }

    private val loadStateFlow : MutableStateFlow<LoadState<User>> = MutableStateFlow(idle())
    private val errorStateFlow: MutableStateFlow<Error> = MutableStateFlow(Error(""))
    private val logStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val logState: Flow<Boolean>
        get() = logStateFlow.asStateFlow()

    val loadState: Flow<LoadState<User>>
        get() = loadStateFlow.asStateFlow()
    val errorState: Flow<Error>
        get() = errorStateFlow.asStateFlow()

    suspend fun fetchUser(id: Int? = null) {
        loadStateFlow.value = loading()
        val userId = sessionStorage.getUserID() ?: return
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.fetchUserInfo(id ?: userId, userAccessToken, userRefreshToken)
            },
            onSuccess = {
                loadStateFlow.value = loaded(it)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
                logStateFlow.value = false
                loadStateFlow.value = idle()
                viewModelScope.launch { sessionStorage.clearSession() }
            },
            sessionStorage = sessionStorage
        )
    }

    fun setLoadingState() {
        loadStateFlow.value = loading()
    }

    fun logout() {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.logout(userAccessToken, userRefreshToken)
            },
            onSuccess = {
                logStateFlow.value = false
                viewModelScope.launch { sessionStorage.clearSession() }
            },
            onFailure = {},
            sessionStorage = sessionStorage
        )
    }

    fun dismissError() {
        errorStateFlow.value = Error("")
    }

    suspend fun refreshData() {
        fetchUser()
    }
}