package com.arkhe.rbi.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.rbi.data.local.UserEntity
import com.arkhe.rbi.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository
) : ViewModel(), IMainViewModel {
    private val _uiState = MutableStateFlow(MainUiState())
    override val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getCurrentUser().collectLatest { user ->
                _uiState.value = _uiState.value.copy(currentUser = user)
            }
        }
    }

    override fun updatePlateNumber(plateNumber: String) {
        _uiState.value = _uiState.value.copy(plateNumber = plateNumber)
    }

    override fun updateNominal(nominal: String) {
        _uiState.value = _uiState.value.copy(selectedNominal = nominal)
    }

    override fun generateQRIS() {
        val state = _uiState.value
        if (state.plateNumber.isBlank() || state.selectedNominal.isBlank()) {
            _uiState.value = state.copy(error = "Please fill all fields")
            return
        }

        val user = state.currentUser
        if (user == null) {
            _uiState.value = state.copy(error = "User not logged in")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)

            try {
                // Simulate API call delay
                delay(2000)

                // Generate QRIS with selected nominal
                val modifiedQRIS = modifyQRISAmount(user.qris, state.selectedNominal)
                _uiState.value = _uiState.value.copy(
                    generatedQRIS = modifiedQRIS,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to generate QRIS: ${e.message}"
                )
            }
        }
    }

    private fun modifyQRISAmount(originalQRIS: String, nominal: String): String {
        val amount = nominal.replace("Rp", "").replace(".", "").replace(",", "")
        return "${originalQRIS}_${amount}_${System.currentTimeMillis()}"
    }

    override fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}

data class MainUiState(
    val currentUser: UserEntity? = null,
    val plateNumber: String = "",
    val selectedNominal: String = "",
    val generatedQRIS: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)