package com.example.planit_mobile.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.domain.User
import com.example.planit_mobile.services.EventService
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.utils.launchAndAuthenticateRequest
import com.example.planit_mobile.services.utils.launchAndRequest
import com.example.planit_mobile.ui.screens.common.LoadState
import com.example.planit_mobile.ui.screens.common.errorMessage
import com.example.planit_mobile.ui.screens.common.idle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.loaded
import com.example.planit_mobile.ui.screens.common.loading
import kotlinx.coroutines.launch


class EditUserProfileViewModel (
    private val userService: UserService,
    private val eventService: EventService,
    private val sessionStorage: SessionDataStore
) : ViewModel() {

    companion object {
        fun factory(userService: UserService, eventService: EventService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { EditUserProfileViewModel(userService, eventService, sessionStorage) }
        }
    }

    private val loadStateFlow : MutableStateFlow<LoadState<Any>> = MutableStateFlow(idle())
    private val userInfoFlow : MutableStateFlow<User> = MutableStateFlow(
        User(0, "", "", "", "", emptyList())
    )
    private val errorStateFlow: MutableStateFlow<Error> = MutableStateFlow(Error(""))
    private val categoriesFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private val logStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val loadState: Flow<LoadState<Any>>
        get() = loadStateFlow.asStateFlow()
    val userInfo: Flow<User>
        get() = userInfoFlow.asStateFlow()
    val errorState: Flow<Error>
        get() = errorStateFlow.asStateFlow()
    val categoriesState: Flow<List<String>>
        get() = categoriesFlow.asStateFlow()
    val logState: Flow<Boolean>
        get() = logStateFlow.asStateFlow()


    suspend fun fetchUser(id: Int? = null) {
        loadStateFlow.value = loading()
        val userId = sessionStorage.getUserID() ?: return
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                userService.fetchUserInfo(id ?: userId, userAccessToken, userRefreshToken)
            },
            onSuccess = {
                userInfoFlow.value = it
                loadStateFlow.value = loaded(it)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
                loadStateFlow.value = idle()
                viewModelScope.launch { sessionStorage.clearSession() }
                logStateFlow.value = false
            },
            sessionStorage = sessionStorage
        )
    }

    fun editUser(name: String, interests: List<String>, description: String) {
        loadStateFlow.value = loading()
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, userId ->
                userService.editUser(userAccessToken, userRefreshToken, name, interests, description)
            },
            onSuccess = {
                loadStateFlow.value = loaded(true)
            },
            onFailure = { errorStateFlow.value = errorMessage(it.message.toString()) },
            sessionStorage = sessionStorage
        )
    }

    fun getCategories() {
        launchAndRequest(
            request = { eventService.getCategories() },
            onSuccess = {
                categoriesFlow.value = it
            },
            onFailure = { errorStateFlow.value = errorMessage(it.message.toString()) },
        )
    }

    fun dismissError() {
        errorStateFlow.value = Error("")
    }
}