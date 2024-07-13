package com.example.planit_mobile.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.models.UserEventsResult
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
import java.util.Calendar

class CalendarViewModel(
    private val service: UserService,
    private val sessionStorage: SessionDataStore
) : ViewModel() {

    companion object {
        fun factory(service: UserService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { CalendarViewModel(service, sessionStorage) }
        }
    }

    private val _selectedDay = MutableStateFlow(Calendar.getInstance())
    private val _currentMonth = MutableStateFlow(Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1) })
    private val loadStateFlow : MutableStateFlow<LoadState<UserEventsResult>> = MutableStateFlow(idle())
    private val errorStateFlow: MutableStateFlow<Error> = MutableStateFlow(Error(""))

    val currentMonth: Flow<Calendar>
        get() = _currentMonth.asStateFlow()
    val selectedDay: Flow<Calendar>
        get() = _selectedDay.asStateFlow()
    val loadState: Flow<LoadState<UserEventsResult>>
        get() = loadStateFlow.asStateFlow()
    val errorState: Flow<Error>
        get() = errorStateFlow.asStateFlow()

    fun getUserEvents() {
        loadStateFlow.value = loading()
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.getUserEvents(
                    userAccessToken,
                    userRefreshToken
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

    fun dismissError() {
        errorStateFlow.value = Error("")
    }

    fun selectDay(day: Int, month: Int, year: Int) {
        _selectedDay.value = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
    }

    fun updateMonth(currentMonthDate: Calendar) {
        _currentMonth.value = Calendar.getInstance().apply {
            set(Calendar.YEAR, currentMonthDate.get(Calendar.YEAR))
            set(Calendar.MONTH, currentMonthDate.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }
}