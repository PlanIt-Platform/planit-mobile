package com.example.planit_mobile.ui.screens.searchEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.EventService
import com.example.planit_mobile.services.models.SearchEventResult
import com.example.planit_mobile.services.utils.launchAndAuthenticateRequest
import com.example.planit_mobile.ui.screens.common.errorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.planit_mobile.ui.screens.common.Error

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

    val eventsState: Flow<List<SearchEventResult>>
        get() = eventsFlow.asStateFlow()
    val errorState: Flow<Error>
        get() = errorStateFlow.asStateFlow()

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

    fun dismissError() {
        errorStateFlow.value = Error("")
    }


}