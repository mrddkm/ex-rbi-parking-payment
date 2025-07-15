package com.arkhe.rbi.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhe.rbi.R
import com.arkhe.rbi.presentation.shared.FooterSection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onRegistrationSuccess: () -> Unit,
    viewModel: IRegisterViewModel = koinViewModel<RegisterViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRegistrationSuccess()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackBarHostState.showSnackbar(error)
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.app_ic),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Activation",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "to Parking Payment",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.userId.uppercase(),
                            onValueChange = viewModel::updateUserId,
                            label = { Text("User ID") },
                            placeholder = { Text("Input User ID") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = viewModel::register,
                            modifier = modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            enabled = !uiState.isLoading && uiState.userId.isNotBlank(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text(
                                    text = "Submit",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            FooterSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }
    }
}

val mockUiState = RegisterUiState(
    userId = "",
    phone = "",
    isLoading = false,
    isSuccess = false,
    error = null,
    successMessage = null
)

class MockRegisterViewModel : IRegisterViewModel {
    override val uiState: StateFlow<RegisterUiState> = MutableStateFlow(mockUiState)
    override fun updateUserId(userId: String) {}
    override fun register() {}
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        isDarkTheme = false,
        onThemeToggle = {},
        onRegistrationSuccess = {},
        viewModel = MockRegisterViewModel()
    )
}