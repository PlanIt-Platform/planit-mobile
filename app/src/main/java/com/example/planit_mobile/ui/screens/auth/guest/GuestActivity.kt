package com.example.planit_mobile.ui.screens.auth.guest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.planit_mobile.PlanItDependencyProvider
import com.example.planit_mobile.ui.screens.auth.login.LoginActivity
import com.example.planit_mobile.ui.screens.auth.register.RegisterActivity
import com.example.planit_mobile.ui.theme.PlanitMobileTheme

/**
 * The application's home activity.
 */
class GuestActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, GuestActivity::class.java)
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
                    GuestScreen(
                        onLoginRequested = { LoginActivity.navigateTo(this) },
                        onRegisterRequested = { RegisterActivity.navigateTo(this) }
                    )
                }
            }
        }
    }
}
