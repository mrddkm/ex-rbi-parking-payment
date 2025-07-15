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
import com.arkhe.rbi.di.databaseModule
import com.arkhe.rbi.di.networkModule
import com.arkhe.rbi.di.repositoryModule
import com.arkhe.rbi.di.viewModelModule
import com.arkhe.rbi.domain.repository.AuthRepository
import com.arkhe.rbi.presentation.login.LoginScreen
import com.arkhe.rbi.presentation.main.MainScreen
import com.arkhe.rbi.presentation.register.RegisterScreen
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    private val authRepository: AuthRepository by inject()

    @SuppressLint("LocalContextConfigurationRead")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            modules(
                networkModule,
                databaseModule,
                repositoryModule,
                viewModelModule
            )
        }
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val themePref = remember { ThemePreference(context) }
            val themeFlow = themePref.themeFlow.collectAsState(initial = null)

            var isRegistered by remember { mutableStateOf(false) }
            var isLoggedIn by remember { mutableStateOf(false) }
            var activeUserID: String by remember { mutableStateOf("Unknown") }

            LaunchedEffect(Unit) {
                isRegistered = authRepository.hasRegisteredUser()
                if (isRegistered) activeUserID = authRepository.getUser()
            }

            val currentUser by authRepository.getCurrentUser().collectAsState(initial = null)
            LaunchedEffect(currentUser) {
                isLoggedIn = currentUser != null
            }

            println("Is Registered: $isRegistered")
            println("Is Logged In: $isLoggedIn")
            println("Active UserID: $activeUserID")

            val isSystemDark = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                    (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

                else -> false
            }
            val isDarkTheme = themeFlow.value ?: isSystemDark

            AppTheme(darkTheme = isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when {
                        !isRegistered -> {
                            RegisterScreen(
                                modifier = Modifier.padding(innerPadding),
                                isDarkTheme = isDarkTheme,
                                onThemeToggle = {
                                    coroutineScope.launch {
                                        themePref.setDarkTheme(!isDarkTheme)
                                    }
                                },
                                onRegistrationSuccess = {
                                    coroutineScope.launch {
                                        isRegistered = authRepository.hasRegisteredUser()
                                    }
                                }
                            )
                        }

                        !isLoggedIn -> {
                            if (activeUserID == "Unknown"){
                                LaunchedEffect(Unit) {
                                    activeUserID = authRepository.getUser()
                                }
                            }

                            LoginScreen(
                                modifier = Modifier.padding(innerPadding),
                                isDarkTheme = isDarkTheme,
                                onThemeToggle = {
                                    coroutineScope.launch {
                                        themePref.setDarkTheme(!isDarkTheme)
                                    }
                                },
                                userId = activeUserID,
                                onLoginSuccess = {
                                    isLoggedIn = true
                                }
                            )
                        }

                        else -> {
                            MainScreen(
                                modifier = Modifier.padding(innerPadding),
                                isDarkTheme = isDarkTheme,
                                onThemeToggle = {
                                    coroutineScope.launch {
                                        themePref.setDarkTheme(!isDarkTheme)
                                    }
                                },
                                onLogout = {
                                    isLoggedIn = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}