package com.example.bs_addateconverteragecalculator.ui.agecalc

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
import com.example.bs_addateconverteragecalculator.ui.components.DatePickerBs
import com.example.bs_addateconverteragecalculator.ui.components.ResultCard
import com.example.bs_addateconverteragecalculator.utils.AppStrings
import com.example.bs_addateconverteragecalculator.utils.ClipboardUtils
import com.example.bs_addateconverteragecalculator.utils.LocalAppLanguage
import com.example.bs_addateconverteragecalculator.utils.ShareUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AgeCalculatorScreen(
    viewModel: AgeCalculatorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val lang = LocalAppLanguage.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Input Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Calendar Type Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        selected = uiState.isBsInput,
                        onClick = { viewModel.setInputType(true) },
                        label = { Text("Bikram Sambat (BS)", fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                    FilterChip(
                        selected = !uiState.isBsInput,
                        onClick = { viewModel.setInputType(false) },
                        label = { Text("Gregorian (AD)", fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Date Picker Content
                if (uiState.isBsInput) {
                    Text(
                        text = AppStrings.get("label_birth_date", lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    DatePickerBs(
                        selectedYear = uiState.bsYear,
                        selectedMonth = uiState.bsMonth,
                        selectedDay = uiState.bsDay,
                        onDateSelected = { y, m, d -> viewModel.updateBsBirthDate(y, m, d) }
                    )
                } else {
                    Text(
                        text = if (lang == "en") "Date of Birth (AD)" else if (lang == "hi") "जन्म तिथि (ईस्वी)" else "जन्म मिति (इ.सं.)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    OutlinedButton(
                        onClick = {
                            val datePickerDialog = DatePickerDialog(
                                context,
                                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                                    viewModel.updateAdBirthDate(LocalDate.of(year, month + 1, dayOfMonth))
                                },
                                uiState.adBirthDate.year,
                                uiState.adBirthDate.monthValue - 1,
                                uiState.adBirthDate.dayOfMonth
                            )
                            datePickerDialog.show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = uiState.adBirthDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale.ENGLISH)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.calculate() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(AppStrings.get("btn_calculate", lang), fontSize = 16.sp, fontWeight = FontWeight.Bold)
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

        // Show result only if calculation has actually yielded years (not default empty init state)
        if (uiState.ageResult.years > 0 || uiState.ageResult.months > 0 || uiState.ageResult.days > 0) {
            Spacer(modifier = Modifier.height(24.dp))
            val age = uiState.ageResult
            val yearsStr = AppStrings.get("years", lang)
            val monthsStr = AppStrings.get("months", lang)
            val daysStr = AppStrings.get("days", lang)
            val ageString = "${age.years} $yearsStr, ${age.months} $monthsStr, ${age.days} $daysStr"

            ResultCard(
                title = AppStrings.get("result_age", lang),
                onCopy = { ClipboardUtils.copyText(context, ageString) },
                onShare = { ShareUtils.shareText(context, "My precise age is $ageString calculated using GlowDate App!") }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Big Hero Age Display
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${age.years}",
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = yearsStr,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Secondary Details Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${age.months}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(monthsStr, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${age.days}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(daysStr, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
