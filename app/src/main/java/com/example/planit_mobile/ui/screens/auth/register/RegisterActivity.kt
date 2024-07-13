package com.example.planit_mobile.ui.screens.auth.register

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.planit_mobile.PlanItDependencyProvider
import com.example.planit_mobile.ui.screens.home.HomeActivity
import com.example.planit_mobile.ui.theme.PlanitMobileTheme
import kotlinx.coroutines.launch
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.ErrorPopup
import com.example.planit_mobile.ui.screens.common.getOrNull


class RegisterActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val viewModel by viewModels<RegisterViewModel> {
        RegisterViewModel.factory(dependencies.userService, dependencies.sessionStorage)
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
                if (it is SuccessState) {
                    HomeActivity.navigateTo(this@RegisterActivity)
                }
            }
        }

        setContent {
            val userCreationSuccessful = viewModel.userCreationSuccessfulState.collectAsState(initial = false).value
            val errorMessage = viewModel.errorState.collectAsState(initial = Error("")).value.message
            val loadState by viewModel.loadState.collectAsState(initial = Step1State)
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
                        onBackRequested = {
                            when(loadState){
                                step1() -> finish()
                                step2() -> finish()
                                step3() -> viewModel.setLoadState(step2())
                            }
                        },
                        showError = errorMessage != "",
                        userCreationSuccessful = userCreationSuccessful,
                        dismissUserCreationSuccess = { viewModel.dismissUserCreationSuccess() },
                        steps = loadState,
                        setSteps = { viewModel.setLoadState(it) }
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
