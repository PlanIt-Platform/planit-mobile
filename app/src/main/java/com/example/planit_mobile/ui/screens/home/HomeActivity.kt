package com.example.planit_mobile.ui.screens.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.planit_mobile.PlanItDependencyProvider
import com.example.planit_mobile.ui.screens.auth.guest.GuestActivity
import com.example.planit_mobile.ui.screens.calendar.CalendarActivity
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.ErrorPopup
import com.example.planit_mobile.ui.screens.common.Idle
import com.example.planit_mobile.ui.screens.common.Loading
import com.example.planit_mobile.ui.screens.common.LoadingScreen
import com.example.planit_mobile.ui.screens.common.getOrNull
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.myDetails.edit.EditDetailsActivity
import com.example.planit_mobile.ui.screens.myDetails.UserProfileScreen
import com.example.planit_mobile.ui.screens.myDetails.MyDetailsViewModel
import com.example.planit_mobile.ui.screens.eventDetails.EventDetailsActivity
import com.example.planit_mobile.ui.screens.nearMe.NearMeActivity
import com.example.planit_mobile.ui.screens.searchEvent.SearchEventScreen
import com.example.planit_mobile.ui.screens.searchEvent.SearchEventViewModel
import com.example.planit_mobile.ui.theme.PlanitMobileTheme
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * The application's home activity.
 */
class HomeActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModel.factory(dependencies.userService, dependencies.eventService, dependencies.sessionStorage)
    }

    private val userViewModel by viewModels<MyDetailsViewModel> {
        MyDetailsViewModel.factory(dependencies.userService, dependencies.sessionStorage)
    }

    private val eventViewModel by viewModels<SearchEventViewModel> {
        SearchEventViewModel.factory(dependencies.eventService, dependencies.sessionStorage)
    }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, HomeActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            combine(userViewModel.loadState, userViewModel.logState) { loadState, logState ->
                Pair(loadState, logState)
            }.collect { (loadState, logState) ->
                if (loadState is Idle) {
                    userViewModel.fetchUser()
                }
                if (!logState) {
                    GuestActivity.navigateTo(this@HomeActivity)
                }
            }
        }

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.homeTabState.collect { homeTab ->
                    if (homeTab == HomeTabState.HOME) {
                        homeViewModel.getCategories()
                        homeViewModel.getUserEvents()
                    }
                    if (homeTab == HomeTabState.PROFILE) {
                        userViewModel.refreshData()
                    }
                    if (homeTab == HomeTabState.EVENTS) {
                        eventViewModel.searchEvents(null, 0)
                        homeViewModel.getUserEvents()
                    }
                }
            }
        }

        setContent {
            PlanitMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val homeTabState =
                        homeViewModel.homeTabState.collectAsState(initial = HomeTabState.HOME).value
                    val userErrorMessage =
                        userViewModel.errorState.collectAsState(initial = Error("")).value.message
                    val userState = userViewModel.loadState.collectAsState(initial = idle()).value
                    val logState = userViewModel.logState.collectAsState(initial = false).value

                    when (homeTabState) {

                        HomeTabState.HOME -> {
                            val categories =
                                homeViewModel.categoriesState.collectAsState(initial = emptyList()).value
                            val homeErrorMessage =
                                homeViewModel.errorState.collectAsState(initial = Error("")).value.message
                            val eventCreatedState =
                                homeViewModel.eventCreatedState.collectAsState(initial = false).value
                            val eventCreatedMessage =
                                homeViewModel.eventCreatedMessageState.collectAsState(initial = "").value
                            val userEvents =
                                homeViewModel.userEventsState.collectAsState(initial = null).value

                            if (userState is Loading) {
                                LoadingScreen {
                                    navigateTo(this@HomeActivity)
                                }
                            } else {
                                HomeScreen(
                                    onProfileRequested = {
                                        lifecycleScope.launch { userViewModel.refreshData() }
                                        homeViewModel.setHomeTabState(HomeTabState.PROFILE)
                                    },
                                    onHomeRequested = { lifecycleScope.launch {homeViewModel.refreshData()} },
                                    onCalendarRequested = {
                                        CalendarActivity.navigateTo(this@HomeActivity)
                                    },
                                    onEventsRequested = {
                                        eventViewModel.searchEvents(null, 0)
                                        homeViewModel.setHomeTabState(HomeTabState.EVENTS)
                                    },
                                    categories = categories,
                                    createEventRequested = { title, description, category,
                                                             locationType, location, latitude,
                                                             longitude, visibility, date, endDate, price, password ->
                                        homeViewModel.createEvent(
                                            title,
                                            description,
                                            category,
                                            locationType,
                                            location,
                                            latitude,
                                            longitude,
                                            visibility,
                                            date,
                                            endDate,
                                            price,
                                            password
                                        )
                                    },
                                    eventCreatedPopUp = eventCreatedState,
                                    eventCreatedMessage = eventCreatedMessage,
                                    userEvents = userEvents,
                                    onEventClick = { event ->
                                        EventDetailsActivity.navigateTo(this@HomeActivity, event.id, event.visibility)
                                    },
                                    dismissEventCreatedPopUp = {
                                        homeViewModel.dismissEventCreated()
                                    },
                                    onNearMeRequested = {
                                        NearMeActivity.navigateTo(this@HomeActivity)
                                    }
                                )
                                ErrorPopup(
                                    showDialog = homeErrorMessage != "",
                                    errorMessage = homeErrorMessage
                                ) {
                                    homeViewModel.dismissError()
                                }
                                if (logState) {
                                    ErrorPopup(
                                        showDialog = userErrorMessage != "",
                                        errorMessage = userErrorMessage
                                    ) {
                                        userViewModel.dismissError()
                                    }
                                }
                            }
                        }

                        HomeTabState.PROFILE -> {
                            val state = userViewModel.loadState.collectAsState(initial = idle())
                                .value.getOrNull()
                            if (state != null) {
                                if (userState is Loading) {
                                    LoadingScreen {
                                        navigateTo(this@HomeActivity)
                                    }
                                } else {
                                    UserProfileScreen(
                                        userInfo = state,
                                        onProfileRequested = {
                                            lifecycleScope.launch { userViewModel.refreshData() }
                                        },
                                        onHomeRequested = {
                                            homeViewModel.setHomeTabState(HomeTabState.HOME)
                                        },
                                        onEventsRequested = {
                                            eventViewModel.searchEvents(null, 0)
                                            homeViewModel.setHomeTabState(HomeTabState.EVENTS)
                                        },
                                        onLogoutRequested = {
                                            userViewModel.setLoadingState()
                                            userViewModel.logout()
                                            GuestActivity.navigateTo(this@HomeActivity)
                                        },
                                        onEditProfileRequested = {
                                            EditDetailsActivity.navigateTo(this@HomeActivity)
                                        }
                                    )
                                    ErrorPopup(
                                        showDialog = userErrorMessage != "",
                                        errorMessage = userErrorMessage
                                    ) {
                                        userViewModel.dismissError()
                                    }
                                }
                            }
                        }

                        HomeTabState.EVENTS -> {
                            homeViewModel.getCategories()
                            val categories =
                                homeViewModel.categoriesState.collectAsState(initial = emptyList()).value
                            val eventErrorMessage = eventViewModel.errorState.collectAsState(
                                initial = Error("")).value.message
                            val joinEventWithCode = eventViewModel.joinEventWithCodeState.collectAsState(
                                initial = null).value
                            if(joinEventWithCode != null){
                                val eventDetailsState = eventViewModel.eventDetailsState.collectAsState(
                                    initial = null).value
                                if (eventDetailsState != null){
                                    eventViewModel.dismissJoinEventWithCode()
                                    EventDetailsActivity.navigateTo(
                                        this@HomeActivity,
                                        joinEventWithCode,
                                        eventDetailsState.visibility
                                    )
                                }
                            }
                            SearchEventScreen(
                                onProfileRequested = {
                                    homeViewModel.setHomeTabState(HomeTabState.PROFILE)
                                },
                                onHomeRequested = {
                                    homeViewModel.setHomeTabState(HomeTabState.HOME)
                                },
                                onEventsRequested = {
                                    lifecycleScope.launch {eventViewModel.refreshData()}
                                },
                                onNearMeRequested = {
                                    NearMeActivity.navigateTo(this@HomeActivity)
                                },
                                onSearch = { searchQuery ->
                                    eventViewModel.searchEvents(searchQuery, 0)
                                },
                                events =
                                    eventViewModel.eventsState.collectAsState(initial = emptyList())
                                        .value,
                                categories = categories,
                                onEventClick = { event ->
                                    EventDetailsActivity.navigateTo(this@HomeActivity, event.id, event.visibility)
                                },
                                searchEventCode = { code ->
                                    eventViewModel.joinEventWithCode(code)
                                },
                                getMoreEvents = { query, offset ->
                                    eventViewModel.getMoreEvents(query, offset)
                                }
                            )
                            ErrorPopup(
                                showDialog = userErrorMessage != "" || eventErrorMessage != "",
                                errorMessage = eventErrorMessage
                            ) {
                                eventViewModel.dismissError()
                            }
                        }

                    }
                }
            }
        }
    }
}

