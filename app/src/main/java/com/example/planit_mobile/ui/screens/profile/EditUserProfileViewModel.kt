package com.example.planit_mobile.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
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

    private val loadStateFlow : MutableStateFlow<LoadState<Int>> = MutableStateFlow(idle())
    private val errorStateFlow: MutableStateFlow<Error> = MutableStateFlow(Error(""))
    private val categoriesFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    val categoriesState: Flow<List<String>>
        get() = categoriesFlow.asStateFlow()
    val loadState: Flow<LoadState<Int>>
        get() = loadStateFlow.asStateFlow()
    val errorState: Flow<Error>
        get() = errorStateFlow.asStateFlow()

    fun editUser(name: String, interests: String, description: String) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, userId ->
                userService.editUser(userAccessToken, userRefreshToken, name, interests.split(","), description)
            },
            onSuccess = {
                loadStateFlow.value = loaded(1)
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