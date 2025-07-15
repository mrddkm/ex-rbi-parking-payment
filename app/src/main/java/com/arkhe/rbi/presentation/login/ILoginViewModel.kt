package com.arkhe.rbi.presentation.login

import kotlinx.coroutines.flow.StateFlow

interface ILoginViewModel {
    val uiState: StateFlow<LoginUiState>
    fun updateUserId(userId: String)
    fun updatePassword(password: String)
    fun login()
}