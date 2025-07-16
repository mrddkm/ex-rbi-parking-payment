package com.arkhe.rbi.presentation.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhe.rbi.R
import com.arkhe.rbi.core.ui.theme.AppTheme

@Composable
fun HeaderSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.client_kab_cianjur_ic),
                    contentDescription = "Client",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(2.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    "Kabupaten Cianjur",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun HeaderMainSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.client_kab_cianjur_ic),
                contentDescription = "Client",
                modifier = Modifier
                    .size(80.dp)
                    .padding(2.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                "Kabupaten Cianjur",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
fun HeaderSectionPreview() {
    AppTheme {
        HeaderSection()
    }
}