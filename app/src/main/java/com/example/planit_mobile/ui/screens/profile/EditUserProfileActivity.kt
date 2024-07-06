package com.example.planit_mobile.ui.screens.profile

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
import com.example.planit_mobile.ui.screens.common.Loading
import com.example.planit_mobile.ui.screens.common.LoadingScreen
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.home.HomeActivity
import com.example.planit_mobile.ui.theme.PlanitMobileTheme
import kotlinx.coroutines.launch

class EditUserProfileActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val editUserViewModel by viewModels<EditUserProfileViewModel> {
        EditUserProfileViewModel.factory(dependencies.userService, dependencies.sessionStorage)
    }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, EditUserProfileActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            editUserViewModel.fetchUser()
            editUserViewModel.logState.collect{
                if (!it) {
                    GuestActivity.navigateTo(this@EditUserProfileActivity)
                }
            }
        }

        setContent {
            PlanitMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val loadState = editUserViewModel.loadState.collectAsState(initial = idle()).value
                    val user = editUserViewModel.userInfo.collectAsState(initial = null).value
                    val errorMessage = editUserViewModel.errorState.collectAsState(initial = Error("")).value.message

                    if (loadState is Loading) {
                        LoadingScreen {
                            HomeActivity.navigateTo(this@EditUserProfileActivity)
                        }
                    } else {
                        if(user != null) {
                            EditUserProfileScreen(
                                onBackRequested = { finish() },
                                onEdit = { name, interests, description ->
                                    editUserViewModel.editUser(name, interests, description)
                                    finish()
                                },
                                userInfo = user
                            )
                            ErrorPopup(
                                showDialog = errorMessage != "",
                                errorMessage = errorMessage
                            ) {
                                editUserViewModel.dismissError()
                            }
                        }
                    }
                }
            }
        }
    }

}