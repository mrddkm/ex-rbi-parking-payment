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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.arkhe.rbi.core.theme.ThemePreference
import com.arkhe.rbi.presentation.VehicleCheckScreen
import com.arkhe.rbi.ui.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("LocalContextConfigurationRead")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val themePref = remember { ThemePreference(context) }
            val themeFlow = themePref.themeFlow.collectAsState(initial = null)
            val isSystemDark = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                    (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                else -> false
            }
            val isDarkTheme = themeFlow.value ?: isSystemDark
            AppTheme(darkTheme = isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                }
            }
        }
    }
}