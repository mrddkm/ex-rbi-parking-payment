package com.arkhe.rbi.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.rbi.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel(), ILoginViewModel {
    private val _uiState = MutableStateFlow(LoginUiState())
    override val uiState = _uiState.asStateFlow()

    override fun updateUserId(userId: String) {
        _uiState.value = _uiState.value.copy(userId = userId)
    }

    override fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    override fun login() {
        val state = _uiState.value
        if (state.userId.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(error = "Please fill all fields")
            return
        }

        _uiState.value = state.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val result = authRepository.login(state.userId, state.password)
            if (result.isSuccess) {
                _uiState.value = state.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } else {
                _uiState.value = state.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Login failed"
                )
            }
        }
    }
}

data class LoginUiState(
    val userId: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)