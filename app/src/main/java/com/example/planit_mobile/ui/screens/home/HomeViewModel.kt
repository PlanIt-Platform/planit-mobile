package com.example.planit_mobile.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.EventService
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.models.UserEventsResult
import com.example.planit_mobile.services.utils.launchAndAuthenticateRequest
import com.example.planit_mobile.services.utils.launchAndRequest
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.errorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val service: UserService,
    private val eventService: EventService,
    private val sessionStorage: SessionDataStore
) : ViewModel() {

    companion object {
        fun factory(service: UserService, eventService: EventService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { HomeViewModel(service, eventService, sessionStorage) }
        }
    }

    private val categoriesFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private val errorStateFlow: MutableStateFlow<Error> = MutableStateFlow(Error(""))
    private val subcategoriesFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private val eventCreated = MutableStateFlow(false)
    private val eventCreatedMessage = MutableStateFlow("")
    private val userEvents: MutableStateFlow<UserEventsResult?> = MutableStateFlow(null)

    val categoriesState: Flow<List<String>>
        get() = categoriesFlow.asStateFlow()
    val errorState: Flow<Error>
        get() = errorStateFlow.asStateFlow()
    val subcategoriesState: Flow<List<String>>
        get() = subcategoriesFlow.asStateFlow()
    val eventCreatedState: Flow<Boolean>
        get() = eventCreated.asStateFlow()
    val eventCreatedMessageState: Flow<String>
        get() = eventCreatedMessage.asStateFlow()
    val userEventsState: Flow<UserEventsResult?>
        get() = userEvents.asStateFlow()

    fun getCategories() {
        launchAndRequest(
            request = { eventService.getCategories() },
            onSuccess = {
                categoriesFlow.value = it
            },
            onFailure = { errorStateFlow.value = errorMessage(it.message.toString()) },
        )
    }

    fun getSubcategories(category: String) {
        launchAndRequest(
            request = {
                val validURICategory = category.replace(" ", "-")
                eventService.getSubcategories(validURICategory)
            },
            onSuccess = {
                subcategoriesFlow.value = it
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
        )
    }

    fun createEvent(
        title: String,
        description: String?,
        category: String,
        subcategory: String,
        location: String?,
        visibility: String?,
        date: String,
        endDate: String?,
        price: String,
        password: String
    ) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                eventService.createEvent(
                    userAccessToken,
                    userRefreshToken,
                    title,
                    description,
                    category,
                    subcategory,
                    location,
                    visibility,
                    date,
                    endDate,
                    price,
                    password
                )
            },
            onSuccess = { result ->
                val (id, eventTitle, status) = result
                eventCreatedMessage.value = "$eventTitle with ID $id: $status"
                eventCreated.value = true
                refreshData()
            },
            onFailure = { errorStateFlow.value = errorMessage(it.message.toString()) },
            sessionStorage = sessionStorage
        )
    }

    fun getUserEvents() {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.getUserEvents(userAccessToken, userRefreshToken)
            },
            onSuccess = {
                userEvents.value = it
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun dismissEventCreated() {
        eventCreated.value = false
    }

    fun refreshData() {
        getUserEvents()
    }

    fun dismissError() {
        errorStateFlow.value = Error("")
    }

    private val _homeTabState = MutableStateFlow(HomeTabState.HOME)
    val homeTabState: Flow<HomeTabState> = _homeTabState.asStateFlow()

    fun setHomeTabState(homeTabState: HomeTabState) {
        _homeTabState.value = homeTabState
    }
}
