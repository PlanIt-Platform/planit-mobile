package com.example.planit_mobile.ui.screens.searchEvent

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
import com.example.planit_mobile.ui.screens.common.Idle
import com.example.planit_mobile.ui.screens.common.getOrNull
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.home.HomeActivity
import com.example.planit_mobile.ui.theme.PlanitMobileTheme
import kotlinx.coroutines.launch

class SearchEventActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val viewModel by viewModels<SearchEventViewModel> {
        SearchEventViewModel.factory(dependencies.eventService, dependencies.sessionStorage)
    }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, SearchEventActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlanitMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchEventScreen(
                        onProfileRequested = { navigateTo(this) },
                        onHomeRequested = { HomeActivity.navigateTo(this) },
                        onEventsRequested = { navigateTo(this) },
                        onSearch = { searchQuery ->
                            viewModel.search(searchQuery)
                        },
                    )
                }
            }
        }
    }
}
