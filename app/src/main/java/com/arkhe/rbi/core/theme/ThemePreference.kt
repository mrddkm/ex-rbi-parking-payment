package com.arkhe.rbi.core.theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")

class ThemePreference(private val context: Context) {
    companion object {
        val THEME_DARK_KEY = booleanPreferencesKey("theme_dark")
    }

    val themeFlow: Flow<Boolean?> = context.dataStore.data
        .map { prefs -> prefs[THEME_DARK_KEY] }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[THEME_DARK_KEY] = isDark
        }
    }
}