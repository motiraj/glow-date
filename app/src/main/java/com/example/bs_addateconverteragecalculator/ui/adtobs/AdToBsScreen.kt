package com.example.bs_addateconverteragecalculator.ui.adtobs

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import com.example.bs_addateconverteragecalculator.ui.components.ResultCard
import com.example.bs_addateconverteragecalculator.utils.AppStrings
import com.example.bs_addateconverteragecalculator.utils.ClipboardUtils
import com.example.bs_addateconverteragecalculator.utils.LocalAppLanguage
import com.example.bs_addateconverteragecalculator.utils.LocalIsNepali
import com.example.bs_addateconverteragecalculator.utils.ShareUtils
import com.example.bs_addateconverteragecalculator.data.NepaliDays
import com.example.bs_addateconverteragecalculator.data.NepaliMonths
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AdToBsScreen(
    viewModel: AdToBsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val lang = LocalAppLanguage.current
    val isNepali = LocalIsNepali.current
    val monthName = NepaliMonths.getMonthName(uiState.bsMonth, isNepali)
    val yearStr = NepaliNumerals.formatDigits(uiState.bsYear, isNepali)
    val dayStr = NepaliNumerals.formatDigits(uiState.bsDay, isNepali)
    val formattedBsDate = "$dayStr $monthName $yearStr"
    val dayOfWeekName = NepaliDays.getDayName(uiState.selectedAdDate.dayOfWeek.value % 7, isNepali)

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
                    text = AppStrings.get("select_ad_date", lang),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedButton(
                    onClick = {
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                                viewModel.convertAdDate(LocalDate.of(year, month + 1, dayOfMonth))
                            },
                            uiState.selectedAdDate.year,
                            uiState.selectedAdDate.monthValue - 1,
                            uiState.selectedAdDate.dayOfMonth
                        )
                        datePickerDialog.show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    val formattedSelectedAd = uiState.selectedAdDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH))
                    Text(
                        text = formattedSelectedAd,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.convertAdDate(uiState.selectedAdDate) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(AppStrings.get("btn_convert_bs", lang), fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                title = AppStrings.get("converted_bs_date", lang),
                onCopy = {
                    ClipboardUtils.copyText(
                        context,
                        "$formattedBsDate, $dayOfWeekName"
                    )
                },
                onShare = {
                    ShareUtils.shareText(
                        context,
                        "AD Date: ${uiState.selectedAdDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}\nConverted BS Date: $formattedBsDate, $dayOfWeekName"
                    )
                }
            ) {
                Text(
                    text = formattedBsDate,
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

                if (uiState.holidayTitle != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "🎉 ${uiState.holidayTitle}",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
