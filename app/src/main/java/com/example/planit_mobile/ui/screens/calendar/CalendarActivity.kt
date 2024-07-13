package com.example.planit_mobile.ui.screens.calendar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.planit_mobile.PlanItDependencyProvider
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.ErrorPopup
import com.example.planit_mobile.ui.screens.common.Idle
import com.example.planit_mobile.ui.screens.common.Loading
import com.example.planit_mobile.ui.screens.common.LoadingScreen
import com.example.planit_mobile.ui.screens.common.getOrNull
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.eventDetails.EventDetailsActivity
import com.example.planit_mobile.ui.screens.home.HomeActivity
import com.example.planit_mobile.ui.theme.PlanitMobileTheme
import kotlinx.coroutines.launch
import java.util.Calendar

class CalendarActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val viewModel by viewModels<CalendarViewModel> {
        CalendarViewModel.factory(dependencies.userService, dependencies.sessionStorage)
    }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, CalendarActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.loadState.collect {
                if (it is Idle) {
                    viewModel.getUserEvents()
                }
            }
        }

        setContent {
            PlanitMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val eventsState = viewModel.loadState.collectAsState(initial = idle()).value
                    val errorMessage =
                        viewModel.errorState.collectAsState(initial = Error("")).value.message
                    val selectedDay =
                        viewModel.selectedDay.collectAsState(initial = Calendar.getInstance()).value
                    val currentMonth =
                        viewModel.currentMonth.collectAsState(initial = Calendar.getInstance()).value
                    if (eventsState is Loading) {
                        LoadingScreen(
                            onBackRequested = {
                                finish()
                            }
                        )
                    } else {
                        CalendarScreen(
                            onDayClick = { day, month, year ->
                                viewModel.selectDay(day, month, year)
                            },
                            userEvents = eventsState.getOrNull(),
                            selectedDay = selectedDay,
                            currentMonth = currentMonth,
                            setCurrentMonth = { monthDate ->
                                viewModel.updateMonth(monthDate)
                            },
                            onBackRequested = { finish() },
                            onEventClick = { id, visibility ->
                                EventDetailsActivity.navigateTo(
                                    this@CalendarActivity,
                                    id,
                                    visibility
                                )
                            }
                        )
                        ErrorPopup(
                            showDialog = errorMessage != "",
                            errorMessage = errorMessage
                        ) {
                            viewModel.dismissError()
                        }
                    }
                }
            }
        }
    }
}