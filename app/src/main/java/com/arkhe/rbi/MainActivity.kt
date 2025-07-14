package com.arkhe.rbi

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.arkhe.rbi.core.theme.ThemePreference
import com.arkhe.rbi.core.ui.theme.AppTheme
import com.arkhe.rbi.domain.repository.AuthRepository
import com.arkhe.rbi.presentation.main.MainScreen
import com.arkhe.rbi.presentation.register.RegisterScreen
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val authRepository: AuthRepository by inject()

    @SuppressLint("LocalContextConfigurationRead")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val themePref = remember { ThemePreference(context) }
            val themeFlow = themePref.themeFlow.collectAsState(initial = null)

            // Authentication state
            val currentUser by authRepository.getCurrentUser().collectAsState(initial = null)
            var isAuthenticated by remember { mutableStateOf(false) }

            // Check authentication status
            LaunchedEffect(currentUser) {
                isAuthenticated = currentUser != null
            }

            val isSystemDark = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                    (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

                else -> false
            }
            val isDarkTheme = themeFlow.value ?: isSystemDark

            AppTheme(darkTheme = isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isAuthenticated) {
                        // Show main screen if authenticated
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            onLogout = {
                                isAuthenticated = false
                            }
                        )
                    } else {
                        // Show auth screen if not authenticated
                        RegisterScreen(
                            modifier = Modifier.padding(innerPadding),
                            onRegistrationSuccess = {
                                isAuthenticated = true
                            }
                        )
                    }
                    /*
                    VehicleCheckScreen(
                        modifier = Modifier.padding(innerPadding),
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = {
                            coroutineScope.launch {
                                themePref.setDarkTheme(!isDarkTheme)
                            }
                        },
                        context = context,
                        coroutineScope = coroutineScope
                    )
                    */
                }
            }
        }
    }
}