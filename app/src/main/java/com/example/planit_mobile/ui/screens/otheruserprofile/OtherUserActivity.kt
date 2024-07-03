package com.example.planit_mobile.ui.screens.otheruserprofile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.planit_mobile.PlanItDependencyProvider
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.ErrorPopup
import com.example.planit_mobile.ui.screens.common.LoadingScreen
import com.example.planit_mobile.ui.screens.common.getOrNull
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.common.loading
import kotlinx.coroutines.launch

class OtherUserActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val viewModel by viewModels<OtherUserViewModel> {
        OtherUserViewModel.factory(dependencies.userService, dependencies.sessionStorage)
    }

    companion object {
        private const val EXTRA_USER_ID = "extra_user_id"
        fun navigateTo(origin: ComponentActivity, userID: Int) {
            val intent = Intent(origin, OtherUserActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userID)
            }
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = intent.getIntExtra(EXTRA_USER_ID, 0)

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchUser(userId)
            }
        }


        setContent{
            val userState =
                viewModel.loadUserState.collectAsState(initial = idle()).value
            val errorState =
                viewModel.errorState.collectAsState(initial = Error("")).value.message
            if(userState == loading()){
                LoadingScreen{}
            } else {
                OtherUserScreen(
                    userInfo = userState.getOrNull(),
                    onBackRequested = { finish() }
                )
                ErrorPopup(
                    showDialog = errorState != "",
                    errorMessage = errorState
                ) {
                    finish()
                }
            }
        }
    }

}