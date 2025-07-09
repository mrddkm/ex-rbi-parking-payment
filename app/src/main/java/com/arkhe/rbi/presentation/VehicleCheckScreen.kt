package com.arkhe.rbi.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkhe.rbi.applications.payment.model.StatusRequest
import com.arkhe.rbi.applications.payment.model.StatusResponse
import com.arkhe.rbi.core.ktorclient.KtorClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun VehicleCheckScreen(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    context: Context,
    coroutineScope: CoroutineScope,
) {
    var areaCode by remember { mutableStateOf("") }
    var plateNumber by remember { mutableStateOf("") }
    var seriesCode by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("Silahkan input plat kendaraan") }
    var isLoading by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }
    val ktorClient = remember { KtorClient() }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        HeaderSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
        )
        Spacer(modifier = Modifier.height(40.dp))
        ContentSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f)
                .verticalScroll(rememberScrollState()),
            areaCode = areaCode,
            plateNumber = plateNumber,
            seriesCode = seriesCode,
            outputText = outputText,
            isLoading = isLoading,
            hasError = hasError,
            onAreaCodeChange = { areaCode = it },
            onPlateNumberChange = { plateNumber = it },
            onSeriesCodeChange = { seriesCode = it },
            onClearClick = {
                areaCode = ""
                plateNumber = ""
                seriesCode = ""
                outputText = "Silahkan input plat kendaraan"
                hasError = false
            },
            onCheckClick = {
                if (areaCode.isNotBlank() && plateNumber.isNotBlank() && seriesCode.isNotBlank()) {
                    val plateId = "$areaCode$plateNumber$seriesCode"
                    coroutineScope.launch {
                        isLoading = true
                        hasError = false
                        try {
                            val result = ktorClient.postGetStatus(
                                StatusRequest(id = plateId)
                            )

                            if (result.isSuccess) {
                                val response = result.getOrThrow()
                                outputText = formatApiResponse(response)
                            } else {
                                hasError = true
                                outputText = "Terjadi kesalahan..."

                                Toast.makeText(
                                    context,
                                    "Gagal memeriksa status plat kendaraan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (_: Exception) {
                            hasError = true
                            outputText = "Terjadi kesalahan..."

                            Toast.makeText(
                                context,
                                "Gagal memeriksa status plat kendaraan",
                                Toast.LENGTH_SHORT
                            ).show()

                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    outputText = "Lengkapi semua field input"
                    hasError = true
                }
            }
        )
        FooterSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.15f),
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle
        )
    }
}

private fun formatApiResponse(response: StatusResponse): String {
    return buildString {
        appendLine(response.message ?: "Tidak Diketahui")
    }
}

//@Preview(showBackground = true)
//@Composable
//fun VehiclePlateCheckerPreview() {
//    AppTheme {
//        VehicleCheckScreen()
//    }
//}
