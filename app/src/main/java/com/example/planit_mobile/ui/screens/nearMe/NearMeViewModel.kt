package com.example.planit_mobile.ui.screens.nearMe

import com.example.planit_mobile.services.EventService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.models.NearbyEventsResult
import com.example.planit_mobile.services.utils.launchAndAuthenticateRequest
import com.example.planit_mobile.ui.screens.common.LoadState
import com.example.planit_mobile.ui.screens.common.errorMessage
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.common.loaded
import com.example.planit_mobile.ui.screens.common.loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.planit_mobile.ui.screens.common.Error

class NearMeViewModel(
    private val service: EventService,
    private val sessionStorage: SessionDataStore
) : ViewModel() {

    companion object {
        fun factory(service: EventService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { NearMeViewModel(service, sessionStorage) }
        }
    }

    private val _selectedRadius = MutableStateFlow("25")
    private val _numberOfEvents = MutableStateFlow(1f)
    private val loadStateFlow : MutableStateFlow<LoadState<NearbyEventsResult>> = MutableStateFlow(idle())
    private val errorStateFlow: MutableStateFlow<Error> = MutableStateFlow(Error(""))

    val selectedRadius: Flow<String>
        get() = _selectedRadius.asStateFlow()
    val numberOfEvents: Flow<Float>
        get() = _numberOfEvents.asStateFlow()
    val loadState: Flow<LoadState<NearbyEventsResult>>
        get() = loadStateFlow.asStateFlow()
    val errorState: Flow<Error>
        get() = errorStateFlow.asStateFlow()

    fun findNearbyEvents(latitude: Double, longitude: Double, selectedRadius: Int, numEvents: Int) {
        loadStateFlow.value = loading()
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.findNearbyEvents(
                    userAccessToken,
                    userRefreshToken,
                    latitude,
                    longitude,
                    selectedRadius,
                    numEvents
                )
            },
            onSuccess = {
                loadStateFlow.value = loaded(it)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun updateRadius(newRadius: String) {
        _selectedRadius.value = newRadius
    }

    fun updateNumberOfEvents(newNumberOfEvents: Float) {
        _numberOfEvents.value = newNumberOfEvents
    }

    fun dismissError() {
        errorStateFlow.value = Error("")
    }
}