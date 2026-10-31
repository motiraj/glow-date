package com.example.bs_addateconverteragecalculator.ui.bstoad

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bs_addateconverteragecalculator.ui.components.DatePickerBs
import com.example.bs_addateconverteragecalculator.ui.components.ResultCard
import com.example.bs_addateconverteragecalculator.utils.AppStrings
import com.example.bs_addateconverteragecalculator.utils.ClipboardUtils
import com.example.bs_addateconverteragecalculator.utils.LocalAppLanguage
import com.example.bs_addateconverteragecalculator.utils.LocalIsNepali
import com.example.bs_addateconverteragecalculator.utils.ShareUtils
import com.example.bs_addateconverteragecalculator.data.NepaliDays

@Composable
fun BsToAdScreen(
    viewModel: BsToAdViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val lang = LocalAppLanguage.current
    val isNepali = LocalIsNepali.current
    val dayOfWeekName = NepaliDays.getDayName(uiState.dayOfWeekIndex, isNepali)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = AppStrings.get("select_bs_date", lang),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                DatePickerBs(
                    selectedYear = uiState.selectedYear,
                    selectedMonth = uiState.selectedMonth,
                    selectedDay = uiState.selectedDay,
                    onDateSelected = { y, m, d -> viewModel.updateDate(y, m, d) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.convert() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(AppStrings.get("btn_convert_ad", lang), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (uiState.isConverted) {
            Spacer(modifier = Modifier.height(24.dp))
            ResultCard(
                title = AppStrings.get("converted_ad_date", lang),
                onCopy = {
                    ClipboardUtils.copyText(
                        context,
                        "${uiState.convertedAdDate}, ${uiState.dayOfWeekEn}"
                    )
                },
                onShare = {
                    ShareUtils.shareText(
                        context,
                        "BS Date: ${uiState.selectedYear}-${uiState.selectedMonth}-${uiState.selectedDay}\nConverted AD Date: ${uiState.convertedAdDate}, ${uiState.dayOfWeekEn}"
                    )
                }
            ) {
                Text(
                    text = uiState.convertedAdDate,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "$dayOfWeekName (${uiState.dayOfWeekEn})",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
