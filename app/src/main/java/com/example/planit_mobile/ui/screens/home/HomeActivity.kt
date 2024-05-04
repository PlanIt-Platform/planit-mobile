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
import androidx.lifecycle.lifecycleScope
import com.example.planit_mobile.PlanItDependencyProvider
import com.example.planit_mobile.ui.screens.auth.guest.GuestActivity
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.ErrorPopup
import com.example.planit_mobile.ui.screens.common.Idle
import com.example.planit_mobile.ui.screens.common.getOrNull
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.profile.UserProfileScreen
import com.example.planit_mobile.ui.screens.profile.UserProfileViewModel
import com.example.planit_mobile.ui.screens.searchEvent.SearchEventScreen
import com.example.planit_mobile.ui.screens.searchEvent.SearchEventViewModel
import com.example.planit_mobile.ui.theme.PlanitMobileTheme
import kotlinx.coroutines.launch

/**
 * The application's home activity.
 */
class HomeActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModel.factory(dependencies.userService, dependencies.sessionStorage)
    }

    private val userViewModel by viewModels<UserProfileViewModel> {
        UserProfileViewModel.factory(dependencies.userService, dependencies.sessionStorage)
    }

    private val eventViewModel by viewModels<SearchEventViewModel> {
        SearchEventViewModel.factory(dependencies.eventService, dependencies.sessionStorage)
    }

    companion object {
        fun navigateTo(origin: Activity, userID: Int? = null) {
            val intent = Intent(origin, HomeActivity::class.java).apply {
                putExtra("userID", userID)
            }
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userID = intent.getIntExtra("userID", 0)

        lifecycleScope.launch {
            userViewModel.loadState.collect {
                if (it is Idle) {
                    if(userID != 0) {
                        userViewModel.fetchUser(userID)
                    }else {
                        userViewModel.fetchUser()
                    }
                }
            }
            if(!homeViewModel.isLogged()){
                GuestActivity.navigateTo(this@HomeActivity)
            }
            homeViewModel.logState.collect {
                if (!it) {
                    GuestActivity.navigateTo(this@HomeActivity)
                }
            }
        }

        setContent {
            PlanitMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val homeTabState = homeViewModel.homeTabState.collectAsState(HomeTabState.HOME).value
                    when (homeTabState) {
                        HomeTabState.HOME -> {
                            HomeScreen(
                                onLogoutRequested = { homeViewModel.logout() },
                                onProfileRequested = { homeViewModel.setHomeTabState(HomeTabState.PROFILE) },
                                onHomeRequested = { lifecycleScope.launch {homeViewModel.refreshData()} },
                                onEventsRequested = { homeViewModel.setHomeTabState(HomeTabState.EVENTS) }
                            )
                        }
                        HomeTabState.PROFILE -> {
                            val state = userViewModel.loadState.collectAsState(initial = idle()).value.getOrNull()
                            val errorMessage = userViewModel.errorState.collectAsState(initial = Error("")).value.message
                            if (state != null) {
                                UserProfileScreen(
                                    userInfo = state,
                                    onProfileRequested = { lifecycleScope.launch {userViewModel.refreshData()} },
                                    onHomeRequested = { homeViewModel.setHomeTabState(HomeTabState.HOME) },
                                    onEventsRequested = { homeViewModel.setHomeTabState(HomeTabState.EVENTS) }
                                )
                                ErrorPopup(
                                    showDialog = errorMessage != "",
                                    errorMessage = errorMessage) {
                                    userViewModel.dismissError()
                                }
                            }
                        }
                        HomeTabState.EVENTS -> {
                            SearchEventScreen(
                                onProfileRequested = { homeViewModel.setHomeTabState(HomeTabState.PROFILE) },
                                onHomeRequested = { homeViewModel.setHomeTabState(HomeTabState.HOME) },
                                onEventsRequested = { lifecycleScope.launch {eventViewModel.refreshData()} },
                                onSearch = { searchQuery ->
                                    eventViewModel.search(searchQuery)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

