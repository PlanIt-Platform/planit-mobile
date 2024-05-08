package com.example.planit_mobile.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.utils.launchAndAuthenticateRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val service: UserService,
    private val sessionStorage: SessionDataStore
) : ViewModel() {

    companion object {
        fun factory(service: UserService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { HomeViewModel(service, sessionStorage) }
        }
    }

    fun refreshData() {
        viewModelScope.launch {

        }
    }

    private val _homeTabState = MutableStateFlow(HomeTabState.HOME)
    val homeTabState: Flow<HomeTabState> = _homeTabState.asStateFlow()

    fun setHomeTabState(homeTabState: HomeTabState) {
        _homeTabState.value = homeTabState
    }
}
