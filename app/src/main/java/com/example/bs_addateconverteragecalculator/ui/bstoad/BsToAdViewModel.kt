package com.example.bs_addateconverteragecalculator.ui.bstoad

import androidx.lifecycle.ViewModel
import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.NepaliDate
import com.example.bs_addateconverteragecalculator.data.NepaliDays
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

data class BsToAdUiState(
    val selectedYear: Int = 2081,
    val selectedMonth: Int = 1,
    val selectedDay: Int = 1,
    val convertedAdDate: String = "",
    val dayOfWeekEn: String = "",
    val dayOfWeekNe: String = "",
    val dayOfWeekIndex: Int = 0,
    val isConverted: Boolean = false,
    val errorMessage: String? = null
)

class BsToAdViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        try {
            val todayBs = DateConverterEngine.convertAdToBs(LocalDate.now())
            BsToAdUiState(
                selectedYear = todayBs.year,
                selectedMonth = todayBs.month,
                selectedDay = todayBs.day
            )
        } catch (e: Exception) {
            BsToAdUiState()
        }
    )
    val uiState: StateFlow<BsToAdUiState> = _uiState.asStateFlow()

    init {
        convert()
    }

    fun updateDate(year: Int, month: Int, day: Int) {
        _uiState.value = _uiState.value.copy(
            selectedYear = year,
            selectedMonth = month,
            selectedDay = day,
            errorMessage = null
        )
        convert()
    }

    fun convert() {
        try {
            val bsDate = NepaliDate(_uiState.value.selectedYear, _uiState.value.selectedMonth, _uiState.value.selectedDay)
            val adDate = DateConverterEngine.convertBsToAd(bsDate)
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
            val formattedDate = adDate.format(formatter)
            val dayOfWeek = adDate.dayOfWeek
            val dayOfWeekIndex = dayOfWeek.value % 7

            _uiState.value = _uiState.value.copy(
                convertedAdDate = formattedDate,
                dayOfWeekEn = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                dayOfWeekNe = NepaliDays.getDayName(dayOfWeekIndex, true),
                dayOfWeekIndex = dayOfWeekIndex,
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
