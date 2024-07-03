package com.example.planit_mobile.ui.screens.otheruserprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.domain.User
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.utils.launchAndAuthenticateRequest
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

class OtherUserViewModel (
    private val userService: UserService,
    private val sessionStorage: SessionDataStore
) : ViewModel()  {

    companion object {
        fun factory(userService: UserService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { OtherUserViewModel(userService, sessionStorage) }
        }
    }

    private val loadUserStateFlow : MutableStateFlow<LoadState<User>> = MutableStateFlow(idle())
    private val errorStateFlow: MutableStateFlow<Error> = MutableStateFlow(Error(""))

    val loadUserState: Flow<LoadState<User>>
        get() = loadUserStateFlow.asStateFlow()
    val errorState: Flow<Error>
        get() = errorStateFlow.asStateFlow()

    suspend fun fetchUser(id: Int? = null) {
        loadUserStateFlow.value = loading()
        val userId = sessionStorage.getUserID() ?: return
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                userService.fetchUserInfo(id ?: userId, userAccessToken, userRefreshToken)
            },
            onSuccess = {
                loadUserStateFlow.value = loaded(it)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
                loadUserStateFlow.value = idle()
                viewModelScope.launch { sessionStorage.clearSession() }
            },
            sessionStorage = sessionStorage
        )
    }

}
