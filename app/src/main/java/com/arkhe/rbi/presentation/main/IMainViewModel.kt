package com.arkhe.rbi.presentation.main

import kotlinx.coroutines.flow.StateFlow

interface IMainViewModel {
    val uiState: StateFlow<MainUiState>
    fun updatePlatKendaraan(platKendaraan: String)
    fun updateNominal(nominal: String)
    fun generateQRIS()
    fun logout()
}