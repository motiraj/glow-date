package com.example.bs_addateconverteragecalculator.converter

import com.example.bs_addateconverteragecalculator.data.BsCalendarData
import com.example.bs_addateconverteragecalculator.data.NepaliDate
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object DateConverterEngine {

    fun convertBsToAd(bsDate: NepaliDate): LocalDate {
        if (!bsDate.isValid()) {
            throw IllegalArgumentException("Invalid Nepali date: ${bsDate.year}-${bsDate.month}-${bsDate.day}")
        }

        val baishakh1 = BsCalendarData.getBaishakh1Ad(bsDate.year)
            ?: throw IllegalArgumentException("Year ${bsDate.year} out of range (2000-2090 BS)")

        var daysElapsed = 0
        for (m in 1 until bsDate.month) {
            daysElapsed += BsCalendarData.getDaysInMonth(bsDate.year, m)
        }
        daysElapsed += (bsDate.day - 1)

        return baishakh1.plusDays(daysElapsed.toLong())
    }

    fun convertAdToBs(adDate: LocalDate): NepaliDate {
        val minDate = BsCalendarData.getBaishakh1Ad(2000) ?: LocalDate.of(1943, 4, 13)
        if (adDate.isBefore(minDate)) {
            throw IllegalArgumentException("Date is before reference date (13 April 1943 AD)")
        }

        val maxDate = BsCalendarData.getBaishakh1Ad(2090)?.plusDays(365) ?: LocalDate.of(2034, 4, 13)
        if (adDate.isAfter(maxDate)) {
            throw IllegalArgumentException("Date exceeds maximum supported calendar range (2090 BS)")
        }

        var targetYear = -1
        for (y in 2000..2090) {
            val start = BsCalendarData.getBaishakh1Ad(y) ?: continue
            val nextStart = BsCalendarData.getBaishakh1Ad(y + 1)
                ?: start.plusDays(365)

            if (!adDate.isBefore(start) && adDate.isBefore(nextStart)) {
                targetYear = y
                break
            }
        }

        if (targetYear == -1) {
            throw IllegalArgumentException("Date is out of supported calendar range")
        }

        val yearStartAd = BsCalendarData.getBaishakh1Ad(targetYear) ?: minDate
        val adDiff = ChronoUnit.DAYS.between(yearStartAd, adDate).toInt()

        var rem = adDiff
        var m = 1
        while (m <= 12) {
            val mDays = BsCalendarData.getDaysInMonth(targetYear, m)
            if (rem >= mDays) {
                rem -= mDays
                m++
            } else {
                break
            }
        }

        if (m > 12) {
            throw IllegalArgumentException("Date is out of supported calendar range")
        }

        return NepaliDate(targetYear, m, rem + 1)
    }
}
