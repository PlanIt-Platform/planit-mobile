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
import com.example.planit_mobile.ui.screens.profile.UserProfileActivity
import com.example.planit_mobile.ui.screens.searchEvent.SearchEventActivity
import com.example.planit_mobile.ui.theme.PlanitMobileTheme
import kotlinx.coroutines.launch

/**
 * The application's home activity.
 */
class HomeActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModel.factory(dependencies.userService, dependencies.sessionStorage)
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
            viewModel.logState.collect {
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
                    HomeScreen(
                        onLogoutRequested = { viewModel.logout() },
                        onProfileRequested = { UserProfileActivity.navigateTo(this) },
                        onHomeRequested = { navigateTo(this) },
                        onEventsRequested = { SearchEventActivity.navigateTo(this) }
                    )
                }
            }
        }
    }
}

