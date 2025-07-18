package com.arkhe.rbi.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhe.rbi.R
import com.arkhe.rbi.data.local.UserEntity
import com.arkhe.rbi.presentation.shared.FooterSection
import com.arkhe.rbi.presentation.shared.HeaderMainSection
import com.arkhe.rbi.presentation.shared.SourceCodePro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onLogout: () -> Unit,
    viewModel: IMainViewModel = koinViewModel<MainViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var areaCode by remember { mutableStateOf("") }
    var plateNumber by remember { mutableStateOf("") }
    var seriesCode by remember { mutableStateOf("") }
    val areaCodeFocusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackBarHostState.showSnackbar(error)
        }
    }

    LaunchedEffect(Unit) {
        areaCodeFocusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.client_dishub_ic),
                            contentDescription = "Client",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(2.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            "Dinas Perhubungan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(start = 1.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            HeaderMainSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f)
            )
            Column(
                modifier = modifier
                    .weight(0.73f)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                uiState.currentUser?.let { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(
                                text = user.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Plat Kendaraan",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 4.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BasicTextField(
                                    value = areaCode,
                                    onValueChange = {
                                        if (it.length <= 2) areaCode = it.uppercase()
                                    },
                                    textStyle = TextStyle(
                                        fontSize = 35.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        fontFamily = SourceCodePro
                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.Characters,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .width(60.dp)
                                        .focusRequester(areaCodeFocusRequester),
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = Color.White,
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                                .padding(horizontal = 1.dp, vertical = 1.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (areaCode.isEmpty()) {
                                                Text(
                                                    text = "F",
                                                    fontSize = 35.sp,
                                                    fontWeight = FontWeight.Light,
                                                    color = Color.LightGray,
                                                    textAlign = TextAlign.Center,
                                                    fontFamily = SourceCodePro
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                BasicTextField(
                                    value = plateNumber,
                                    onValueChange = {
                                        if (it.length <= 4 && it.all { char -> char.isDigit() })
                                            plateNumber = it
                                    },
                                    textStyle = TextStyle(
                                        fontSize = 35.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        fontFamily = SourceCodePro
                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    singleLine = true,
                                    modifier = Modifier.width(120.dp),
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = Color.White,
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                                .padding(horizontal = 1.dp, vertical = 1.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (plateNumber.isEmpty()) {
                                                Text(
                                                    text = "8939",
                                                    fontSize = 35.sp,
                                                    fontWeight = FontWeight.Light,
                                                    color = Color.LightGray,
                                                    textAlign = TextAlign.Center,
                                                    fontFamily = SourceCodePro
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                BasicTextField(
                                    value = seriesCode,
                                    onValueChange = {
                                        if (it.length <= 3) seriesCode = it.uppercase()
                                    },
                                    textStyle = TextStyle(
                                        fontSize = 35.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        fontFamily = SourceCodePro
                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.Characters,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .width(100.dp),
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = Color.White,
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                                .padding(horizontal = 1.dp, vertical = 1.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (seriesCode.isEmpty()) {
                                                Text(
                                                    text = "ABC",
                                                    fontSize = 35.sp,
                                                    fontWeight = FontWeight.Light,
                                                    color = Color.LightGray,
                                                    textAlign = TextAlign.Center,
                                                    fontFamily = SourceCodePro
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    areaCode = ""
                                    plateNumber = ""
                                    seriesCode = ""
                                    viewModel.updatePlatKendaraan("")
                                    areaCodeFocusRequester.requestFocus()
                                },
                                modifier = Modifier
                                    .weight(0.2f)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Clear")
                            }
                            Button(
                                onClick = { viewModel.updatePlatKendaraan("$areaCode$plateNumber$seriesCode") },
                                modifier = Modifier
                                    .weight(0.8f),
                                shape = RoundedCornerShape(8.dp),
                                enabled = areaCode.isNotEmpty() && plateNumber.isNotEmpty() && seriesCode.isNotEmpty()
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text(
                                        text = "Check",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.generatedQRIS.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "QRIS Code Generated",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Plat: ${uiState.plateKendaraan}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = "Nominal: ${uiState.selectedNominal}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uiState.generatedQRIS,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                textAlign = TextAlign.Center,
                                fontFamily = SourceCodePro
                            )
                        }
                    }
                }
            }
            FooterSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.12f),
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }
    }
}

val mockUiState = MainUiState(
    currentUser = UserEntity.fakeUserEntity(),
    plateKendaraan = "",
    selectedNominal = "",
    generatedQRIS = "",
    isLoading = false,
    error = null
)

class MockMainViewModel : IMainViewModel {
    override val uiState: StateFlow<MainUiState> = MutableStateFlow(mockUiState)
    override fun updatePlatKendaraan(platKendaraan: String) {}
    override fun updateNominal(nominal: String) {}
    override fun generateQRIS() {}
    override fun logout() {}
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        isDarkTheme = false,
        onThemeToggle = {},
        onLogout = {},
        viewModel = MockMainViewModel()
    )
}