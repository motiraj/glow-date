package com.example.bs_addateconverteragecalculator.ui.adtobs

import androidx.lifecycle.ViewModel
import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.HolidayData
import com.example.bs_addateconverteragecalculator.data.NepaliDays
import com.example.bs_addateconverteragecalculator.data.NepaliMonths
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class AdToBsUiState(
    val selectedAdDate: LocalDate = LocalDate.now(),
    val bsYear: Int = 2081,
    val bsMonth: Int = 1,
    val bsDay: Int = 1,
    val bsDateEnglish: String = "",
    val bsDateNepali: String = "",
    val dayOfWeekEn: String = "",
    val dayOfWeekNe: String = "",
    val holidayTitle: String? = null,
    val isConverted: Boolean = false,
    val errorMessage: String? = null
)

class AdToBsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdToBsUiState())
    val uiState: StateFlow<AdToBsUiState> = _uiState.asStateFlow()

    init {
        convertAdDate(LocalDate.now())
    }

    fun convertAdDate(date: LocalDate) {
        try {
            val bsDate = DateConverterEngine.convertAdToBs(date)
            val monthNameEn = NepaliMonths.getMonthName(bsDate.month, false)
            val monthNameNe = NepaliMonths.getMonthName(bsDate.month, true)

            val dayOfWeek = date.dayOfWeek
            val dayIndex = dayOfWeek.value % 7

            val holiday = HolidayData.getHoliday(bsDate.year, bsDate.month, bsDate.day)

            _uiState.value = _uiState.value.copy(
                selectedAdDate = date,
                bsYear = bsDate.year,
                bsMonth = bsDate.month,
                bsDay = bsDate.day,
                bsDateEnglish = "${bsDate.day} $monthNameEn ${bsDate.year}",
                bsDateNepali = "${NepaliNumerals.toNepaliDigits(bsDate.day)} $monthNameNe ${NepaliNumerals.toNepaliDigits(bsDate.year)}",
                dayOfWeekEn = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                dayOfWeekNe = NepaliDays.getDayName(dayIndex, true),
                holidayTitle = holiday?.let { "${it.title} (${it.titleNepali})" },
                isConverted = true,
                errorMessage = null
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = e.message ?: "Conversion failed",
                isConverted = false
            )
        }
    }
}
