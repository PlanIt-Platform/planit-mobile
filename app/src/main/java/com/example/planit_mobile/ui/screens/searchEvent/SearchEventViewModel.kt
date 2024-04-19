package com.example.planit_mobile.ui.screens.searchEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.domain.User
import com.example.planit_mobile.services.EventService
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.utils.launchAndRequest
import com.example.planit_mobile.ui.screens.common.LoadState
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.common.loaded
import com.example.planit_mobile.ui.screens.common.loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchEventViewModel(
    private val service: EventService,
    private val sessionStorage: SessionDataStore
) : ViewModel() {

    companion object {
        fun factory(service: EventService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { SearchEventViewModel(service, sessionStorage) }
        }
    }

    private val loadStateFlow : MutableStateFlow<LoadState<User>> = MutableStateFlow(idle())

    val loadState: Flow<LoadState<User>>
        get() = loadStateFlow.asStateFlow()

    fun search(query: String) {
    }


}