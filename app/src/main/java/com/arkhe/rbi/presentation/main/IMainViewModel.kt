package com.arkhe.rbi.presentation.main

import kotlinx.coroutines.flow.StateFlow

interface IMainViewModel {
    val uiState: StateFlow<MainUiState>
    fun updatePlateNumber(plateNumber: String)
    fun updateNominal(nominal: String)
    fun generateQRIS()
    fun logout()
}