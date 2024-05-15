package com.example.planit_mobile.ui.screens.profile

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
import com.example.planit_mobile.PlanItDependencyProvider
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.ErrorPopup
import com.example.planit_mobile.ui.theme.PlanitMobileTheme

class EditUserProfileActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val editUserViewModel by viewModels<EditUserProfileViewModel> {
        EditUserProfileViewModel.factory(dependencies.userService, dependencies.eventService, dependencies.sessionStorage)
    }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, EditUserProfileActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlanitMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    editUserViewModel.getCategories()
                    val categories = editUserViewModel.categoriesState.collectAsState(initial = emptyList()).value
                    val errorMessage = editUserViewModel.errorState.collectAsState(initial = Error("")).value.message
                    EditUserProfileScreen(
                        categories = categories,
                        onBackRequested = { finish() },
                        onEdit = { name, description, interests ->
                            editUserViewModel.editUser(name, description, interests)
                        }
                    )
                    ErrorPopup(
                        showDialog = errorMessage != "",
                        errorMessage = errorMessage) {
                        editUserViewModel.dismissError()
                    }
                }
            }
        }
    }

}