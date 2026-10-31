package com.example.bs_addateconverteragecalculator.calculator

data class AgeResult(
    val years: Int = 0,
    val months: Int = 0,
    val days: Int = 0,
    val totalDays: Long = 0,
    val totalWeeks: Long = 0,
    val totalMonths: Int = 0,
    val bornOnDayOfWeek: String = "",
    val daysToNextBirthday: Long = 0,
    val zodiacSign: String = "",
    val leapYearsLived: Int = 0,
    val isCalculated: Boolean = false
)
