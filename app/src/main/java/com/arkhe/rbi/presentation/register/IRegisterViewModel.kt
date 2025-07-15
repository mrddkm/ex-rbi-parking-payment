package com.arkhe.rbi.presentation.register

import kotlinx.coroutines.flow.StateFlow

interface IRegisterViewModel {
    val uiState: StateFlow<RegisterUiState>
    fun updateUserId(userId: String)
    fun register()
}