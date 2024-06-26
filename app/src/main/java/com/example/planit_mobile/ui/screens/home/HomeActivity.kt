package com.example.planit_mobile.ui.screens.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.ErrorPopup
import com.example.planit_mobile.ui.screens.common.Idle
import com.example.planit_mobile.ui.screens.common.Loading
import com.example.planit_mobile.ui.screens.common.LoadingScreen
import com.example.planit_mobile.ui.screens.common.getOrNull
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.profile.EditUserProfileActivity
import com.example.planit_mobile.ui.screens.profile.UserProfileScreen
import com.example.planit_mobile.ui.screens.profile.UserProfileViewModel
import com.example.planit_mobile.ui.screens.eventDetails.EventDetailsActivity
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

    private val userViewModel by viewModels<UserProfileViewModel> {
        UserProfileViewModel.factory(dependencies.userService, dependencies.sessionStorage)
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
                            val subcategories =
                                homeViewModel.subcategoriesState.collectAsState(initial = emptyList()).value
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
                                    onEventsRequested = {
                                        eventViewModel.searchEvents(null, 0)
                                        homeViewModel.setHomeTabState(HomeTabState.EVENTS)
                                    },
                                    categories = categories,
                                    onCategorySelected = { category ->
                                        homeViewModel.getSubcategories(category)
                                    },
                                    subCategories = subcategories,
                                    createEventRequested = { title, description, category,
                                                             subcategory, location, visibility, date,
                                                             endDate, price, password ->
                                        homeViewModel.createEvent(
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
                                    eventCreatedPopUp = eventCreatedState,
                                    eventCreatedMessage = eventCreatedMessage,
                                    userEvents = userEvents,
                                    onEventClick = { event ->
                                        EventDetailsActivity.navigateTo(this@HomeActivity, event.id, event.visibility)
                                    },
                                    dismissEventCreatedPopUp = {
                                        homeViewModel.dismissEventCreated()
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
                                            EditUserProfileActivity.navigateTo(this@HomeActivity)
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

