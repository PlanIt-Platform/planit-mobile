package com.example.planit_mobile.ui.screens.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import com.example.planit_mobile.PlanItDependencyProvider
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.ErrorPopup
import com.example.planit_mobile.ui.screens.common.Idle
import com.example.planit_mobile.ui.screens.common.getOrNull
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.home.HomeActivity
import com.example.planit_mobile.ui.screens.searchEvent.SearchEventActivity
import kotlinx.coroutines.launch

class UserProfileActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val viewModel by viewModels<UserProfileViewModel> {
        UserProfileViewModel.factory(dependencies.userService, dependencies.sessionStorage)
    }

    companion object {
        fun navigateTo(origin: Activity, userID: Int? = null) {
            val intent = Intent(origin, UserProfileActivity::class.java).apply {
                putExtra("userID", userID)
            }
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userID = intent.getIntExtra("userID", 0)

        lifecycleScope.launch {
            viewModel.loadState.collect {
                if (it is Idle) {
                    if(userID != 0) {
                        viewModel.fetchUser(userID)
                    }else {
                        viewModel.fetchUser()
                    }
                }
            }
        }
        setContent {
            val state = viewModel.loadState.collectAsState(initial = idle()).value.getOrNull()
            val errorMessage = viewModel.errorState.collectAsState(initial = Error("")).value.message
            if (state != null) {
                UserProfileScreen(
                    userInfo = state,
                    onBackRequested = { finish() },
                    onProfileRequested = { navigateTo(this) },
                    onHomeRequested = { HomeActivity.navigateTo(this) },
                    onEventsRequested = { SearchEventActivity.navigateTo(this) }
                )
                ErrorPopup(
                    showDialog = errorMessage != "",
                    errorMessage = errorMessage) {
                    viewModel.dismissError()
                }
            }
        }
    }
}
