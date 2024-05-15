package com.example.planit_mobile.ui.screens.auth.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
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
import com.example.planit_mobile.R
import com.example.planit_mobile.ui.screens.auth.Success
import com.example.planit_mobile.ui.screens.home.HomeActivity
import com.example.planit_mobile.ui.theme.PlanitMobileTheme
import kotlinx.coroutines.launch
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.ErrorPopup


class RegisterActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val viewModel by viewModels<RegisterViewModel> {
        RegisterViewModel.factory(dependencies.userService, dependencies.eventService, dependencies.sessionStorage)
    }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, RegisterActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.loadState.collect {
                if (it is Step3State) {
                    HomeActivity.navigateTo(this@RegisterActivity)
                }
            }
        }

        setContent {
            viewModel.getCategories()
            val categories = viewModel.categoriesState.collectAsState(initial = emptyList()).value
            val errorMessage = viewModel.errorState.collectAsState(initial = Error("")).value.message
            PlanitMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterScreen(
                        onRegister = { username, name, email, password ->
                            viewModel.register(username, name, email, password)
                        },
                        onEdit = { name, interests, description ->
                            viewModel.editUser(name, interests, description)
                        },
                        onBackRequested = { finish() },
                        showError = errorMessage != "",
                        categories = categories
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
}
