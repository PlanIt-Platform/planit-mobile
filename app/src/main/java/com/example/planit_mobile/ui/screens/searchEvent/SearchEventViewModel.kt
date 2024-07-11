package com.example.planit_mobile.ui.screens.searchEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.EventService
import com.example.planit_mobile.services.models.EventModel
import com.example.planit_mobile.services.models.SearchEventResult
import com.example.planit_mobile.services.utils.launchAndAuthenticateRequest
import com.example.planit_mobile.ui.screens.common.errorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.common.loaded
import kotlinx.coroutines.launch

class SearchEventViewModel(
    private val service: EventService,
    private val sessionStorage: SessionDataStore
) : ViewModel() {

    companion object {
        fun factory(service: EventService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { SearchEventViewModel(service, sessionStorage) }
        }
    }

    private val eventsFlow : MutableStateFlow<List<SearchEventResult>> = MutableStateFlow(emptyList())
    private val errorStateFlow: MutableStateFlow<Error> = MutableStateFlow(Error(""))
    private val joinEventWithCodeFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val eventDetailsFlow: MutableStateFlow<EventModel?> = MutableStateFlow(null)

    val eventsState: Flow<List<SearchEventResult>>
        get() = eventsFlow.asStateFlow()
    val errorState: Flow<Error>
        get() = errorStateFlow.asStateFlow()
    val joinEventWithCodeState: Flow<Int?>
        get() = joinEventWithCodeFlow.asStateFlow()
    val eventDetailsState: Flow<EventModel?>
        get() = eventDetailsFlow.asStateFlow()

    fun refreshData() {
        searchEvents(null, 0)
    }

    fun searchEvents(query: String?, offset: Int) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.searchEvents(userAccessToken, userRefreshToken, query, 10, offset)
            },
            onSuccess = {
                if(eventsFlow.value != it.events){ eventsFlow.value = it.events }
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun joinEventWithCode(code: String) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                val eventResult = service.joinEventWithCode(userAccessToken, userRefreshToken, code)
                joinEventWithCodeFlow.value = eventResult.id
                getEventDetails(eventResult.id)
            },
            onSuccess = {
                refreshData()
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    private fun getEventDetails(eventID: Int) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.fetchEventInfo(eventID, userAccessToken, userRefreshToken)
            },
            onSuccess = {
                eventDetailsFlow.value = it
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun dismissJoinEventWithCode() {
        joinEventWithCodeFlow.value = null
        eventDetailsFlow.value = null
    }

    fun dismissError() {
        errorStateFlow.value = Error("")
    }


}