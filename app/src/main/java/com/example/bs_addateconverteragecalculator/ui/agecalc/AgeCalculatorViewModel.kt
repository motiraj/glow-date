package com.example.bs_addateconverteragecalculator.ui.agecalc

import androidx.lifecycle.ViewModel
import com.example.bs_addateconverteragecalculator.calculator.AgeCalculator
import com.example.bs_addateconverteragecalculator.calculator.AgeResult
import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.NepaliDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class AgeCalculatorViewModel : ViewModel() {
    private val defaultBsDate = try {
        DateConverterEngine.convertAdToBs(LocalDate.now().minusYears(20))
    } catch (_: Exception) {
        NepaliDate(2061, 1, 1)
    }

    private val _uiState = MutableStateFlow(
        AgeCalculatorUiState(
            bsYear = defaultBsDate.year,
            bsMonth = defaultBsDate.month,
            bsDay = defaultBsDate.day
        )
    )
    val uiState: StateFlow<AgeCalculatorUiState> = _uiState.asStateFlow()

    init {
        calculate()
    }

    fun setInputType(isBs: Boolean) {
        _uiState.value = _uiState.value.copy(isBsInput = isBs)
        calculate()
    }

    fun updateBsBirthDate(year: Int, month: Int, day: Int) {
        _uiState.value = _uiState.value.copy(
            bsYear = year,
            bsMonth = month,
            bsDay = day
        )
        calculate()
    }

    fun updateAdBirthDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(adBirthDate = date)
        calculate()
    }

    fun updateReferenceDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(referenceDate = date)
        calculate()
    }

    fun calculate() {
        try {
            val result = if (_uiState.value.isBsInput) {
                val bsDate = NepaliDate(_uiState.value.bsYear, _uiState.value.bsMonth, _uiState.value.bsDay)
                AgeCalculator.calculateFromBs(bsDate, _uiState.value.referenceDate)
            } else {
                AgeCalculator.calculateFromAd(_uiState.value.adBirthDate, _uiState.value.referenceDate)
            }

            _uiState.value = _uiState.value.copy(
                ageResult = result,
                errorMessage = null
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = e.message ?: "Calculation failed"
            )
        }
    }
}

data class AgeCalculatorUiState(
    val isBsInput: Boolean = true,
    val bsYear: Int = 2061,
    val bsMonth: Int = 1,
    val bsDay: Int = 1,
    val adBirthDate: LocalDate = LocalDate.now().minusYears(20),
    val referenceDate: LocalDate = LocalDate.now(),
    val ageResult: AgeResult = AgeResult(),
    val errorMessage: String? = null
)

