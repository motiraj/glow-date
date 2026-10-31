package com.example.bs_addateconverteragecalculator.calculator

import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.NepaliDate
import java.time.LocalDate
import java.time.Period
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

object AgeCalculator {

    fun calculateFromAd(birthDate: LocalDate, referenceDate: LocalDate = LocalDate.now()): AgeResult {
        if (birthDate.isAfter(referenceDate)) {
            throw IllegalArgumentException("Birth date cannot be after reference date")
        }

        val period = Period.between(birthDate, referenceDate)
        val totalDays = ChronoUnit.DAYS.between(birthDate, referenceDate)

        // Next birthday calculation
        var nextBirthday = birthDate.withYear(referenceDate.year)
        if (nextBirthday.isBefore(referenceDate) || nextBirthday.isEqual(referenceDate)) {
            nextBirthday = nextBirthday.plusYears(1)
        }
        val daysToNextBirthday = ChronoUnit.DAYS.between(referenceDate, nextBirthday)

        // Day of week
        val bornOnDayOfWeek = birthDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)

        // Zodiac sign
        val zodiacSign = getZodiacSign(birthDate.monthValue, birthDate.dayOfMonth)

        // Leap years lived
        var leapYears = 0
        for (y in birthDate.year..referenceDate.year) {
            if (java.time.Year.isLeap(y.toLong())) {
                leapYears++
            }
        }

        return AgeResult(
            years = period.years,
            months = period.months,
            days = period.days,
            totalDays = totalDays,
            totalWeeks = totalDays / 7,
            totalMonths = (period.years * 12) + period.months,
            bornOnDayOfWeek = bornOnDayOfWeek,
            daysToNextBirthday = daysToNextBirthday,
            zodiacSign = zodiacSign,
            leapYearsLived = leapYears,
            isCalculated = true
        )
    }

    fun calculateFromBs(birthDateBs: NepaliDate, referenceDate: LocalDate = LocalDate.now()): AgeResult {
        val birthAd = DateConverterEngine.convertBsToAd(birthDateBs)
        return calculateFromAd(birthAd, referenceDate)
    }

    private fun getZodiacSign(month: Int, day: Int): String {
        return when (month) {
            1 -> if (day <= 19) "Capricorn" else "Aquarius"
            2 -> if (day <= 18) "Aquarius" else "Pisces"
            3 -> if (day <= 20) "Pisces" else "Aries"
            4 -> if (day <= 19) "Aries" else "Taurus"
            5 -> if (day <= 20) "Taurus" else "Gemini"
            6 -> if (day <= 20) "Gemini" else "Cancer"
            7 -> if (day <= 22) "Cancer" else "Leo"
            8 -> if (day <= 22) "Leo" else "Virgo"
            9 -> if (day <= 22) "Virgo" else "Libra"
            10 -> if (day <= 22) "Libra" else "Scorpio"
            11 -> if (day <= 21) "Scorpio" else "Sagittarius"
            12 -> if (day <= 21) "Sagittarius" else "Capricorn"
            else -> ""
        }
    }
}
