package com.arkhe.rbi.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.rbi.data.remote.dto.RegisterRequest
import com.arkhe.rbi.domain.repository.AuthRepository
import com.arkhe.rbi.utils.DeviceInfoProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val deviceInfoProvider: DeviceInfoProvider
) : ViewModel(), IRegisterViewModel {
    private val _uiState = MutableStateFlow(RegisterUiState())
    override val uiState = _uiState.asStateFlow()

    override fun updateUserId(userId: String) {
        _uiState.value = _uiState.value.copy(userId = userId)
    }

    override fun register() {
        val state = _uiState.value
        if (state.userId.isBlank()) {
            _uiState.value = state.copy(error = "Please fill all fields")
            return
        }

        _uiState.value = state.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val deviceInfo = deviceInfoProvider.getDeviceInfo()
            val request = RegisterRequest(
                userId = state.userId.uppercase(),
                phone = state.phone,
                imei = deviceInfo.imei,
                brand = deviceInfo.brand,
                type = deviceInfo.type,
                osVersion = deviceInfo.osVersion,
                osName = deviceInfo.osName
            )

            val result = authRepository.register(request)
            if (result.isSuccess) {
                _uiState.value = state.copy(
                    isLoading = false,
                    isSuccess = true,
                    successMessage = result.getOrNull()?.message ?: "Registration successful"
                )
            } else {
                _uiState.value = state.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Registration failed"
                )
            }
        }
    }
}

data class RegisterUiState(
    val userId: String = "",
    val phone: String = "085659988939",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)