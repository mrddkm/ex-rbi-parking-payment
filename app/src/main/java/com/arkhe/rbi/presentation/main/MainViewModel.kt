package com.arkhe.rbi.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.rbi.data.local.UserEntity
import com.arkhe.rbi.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getCurrentUser().collectLatest { user ->
                _uiState.value = _uiState.value.copy(currentUser = user)
            }
        }
    }

    fun updatePlateNumber(plateNumber: String) {
        _uiState.value = _uiState.value.copy(plateNumber = plateNumber)
    }

    fun updateNominal(nominal: String) {
        _uiState.value = _uiState.value.copy(selectedNominal = nominal)
    }

    fun generateQRIS() {
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

        // Generate QRIS with selected nominal
        val modifiedQRIS = modifyQRISAmount(user.qris, state.selectedNominal)
        _uiState.value = state.copy(
            generatedQRIS = modifiedQRIS,
            error = null
        )
    }

    private fun modifyQRISAmount(originalQRIS: String, nominal: String): String {
        // Simple QRIS amount modification
        // In real implementation, you would need to properly modify the QRIS structure
        return originalQRIS + "_" + nominal.replace("Rp", "").replace(".", "")
    }

    fun logout() {
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
    val error: String? = null
)