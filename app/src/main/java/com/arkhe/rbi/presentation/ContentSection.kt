package com.arkhe.rbi.presentation

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhe.rbi.R
import com.arkhe.rbi.ui.theme.AppTheme

val SourceCodePro = FontFamily(
    Font(
        resId = R.font.source_code_pro_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.source_code_pro_bold,
        weight = FontWeight.Bold
    )
)

@Composable
fun ContentSection(
    modifier: Modifier = Modifier,
    areaCode: String,
    plateNumber: String,
    seriesCode: String,
    outputText: String,
    isLoading: Boolean,
    hasError: Boolean,
    onAreaCodeChange: (String) -> Unit,
    onPlateNumberChange: (String) -> Unit,
    onSeriesCodeChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onCheckClick: () -> Unit
) {
    val areaCodeFocusRequester = remember { FocusRequester() }
    Column(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Status Kendaraan",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (hasError) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Memeriksa plat kendaraan...",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Text(
                        text = outputText,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        color = if (hasError) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        fontFamily = SourceCodePro
                    )
                }
            }
        }
        @Composable
        fun VehiclePlateInput(
            areaCode: String,
            plateNumber: String,
            seriesCode: String,
            onAreaCodeChange: (String) -> Unit,
            onPlateNumberChange: (String) -> Unit,
            onSeriesCodeChange: (String) -> Unit,
            areaCodeFocusRequester: FocusRequester
        ) {
            val areaCodeFocusRequester = areaCodeFocusRequester

            LaunchedEffect(Unit) {
                areaCodeFocusRequester.requestFocus()
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        onValueChange = onAreaCodeChange,
                        textStyle = TextStyle(
                            fontSize = 40.sp,
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
                                        fontSize = 40.sp,
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
                    Spacer(modifier = Modifier.width(4.dp))
                    BasicTextField(
                        value = plateNumber,
                        onValueChange = onPlateNumberChange,
                        textStyle = TextStyle(
                            fontSize = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontFamily = SourceCodePro
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        modifier = Modifier.width(150.dp),
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
                                        fontSize = 40.sp,
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
                    Spacer(modifier = Modifier.width(4.dp))
                    BasicTextField(
                        value = seriesCode,
                        onValueChange = onSeriesCodeChange,
                        textStyle = TextStyle(
                            fontSize = 40.sp,
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
                        modifier = Modifier.width(100.dp),
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
                                        fontSize = 40.sp,
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
        }
        VehiclePlateInput(
            areaCode = areaCode,
            plateNumber = plateNumber,
            seriesCode = seriesCode,
            onAreaCodeChange = { if (it.length <= 2) onAreaCodeChange(it.uppercase()) },
            onPlateNumberChange = {
                if (it.length <= 4 && it.all { char -> char.isDigit() })
                    onPlateNumberChange(it)
            },
            onSeriesCodeChange = { if (it.length <= 3) onSeriesCodeChange(it.uppercase()) },
            areaCodeFocusRequester = areaCodeFocusRequester
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    onClearClick()
                    areaCodeFocusRequester.requestFocus()
                },
                modifier = Modifier
                    .weight(0.25f)
                    .height(60.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Clear text"
                )
            }
            Button(
                onClick = onCheckClick,
                modifier = Modifier
                    .weight(0.75f)
                    .height(60.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Check",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentSectionPreview() {
    AppTheme {
        ContentSection(
            areaCode = "F",
            plateNumber = "1234",
            seriesCode = "ABC",
            outputText = "Plat kendaraan valid.",
            isLoading = false,
            hasError = false,
            onAreaCodeChange = {},
            onPlateNumberChange = {},
            onSeriesCodeChange = {},
            onClearClick = {},
            onCheckClick = {}
        )
    }
}