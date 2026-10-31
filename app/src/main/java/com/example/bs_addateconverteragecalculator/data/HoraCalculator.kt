@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import java.time.DayOfWeek
import java.util.Locale
import kotlin.math.roundToInt

object HoraCalculator {
    private val horaLords = listOf(
        Pair("सूर्य", "शुभ/अशुभ"), // 0
        Pair("शुक्र", "शुभ"),      // 1
        Pair("बुध", "शुभ"),        // 2
        Pair("चन्द्र", "शुभ"),       // 3
        Pair("शनि", "अशुभ"),       // 4
        Pair("बृहस्पति", "शुभ"),   // 5
        Pair("मंगल", "अशुभ")       // 6
    )

    private fun getStartIndex(dayOfWeek: DayOfWeek): Int {
        return when (dayOfWeek) {
            DayOfWeek.SUNDAY -> 0
            DayOfWeek.MONDAY -> 3
            DayOfWeek.TUESDAY -> 6
            DayOfWeek.WEDNESDAY -> 2
            DayOfWeek.THURSDAY -> 5
            DayOfWeek.FRIDAY -> 1
            DayOfWeek.SATURDAY -> 4
        }
    }

    fun getHoraSchedule(dayOfWeek: DayOfWeek, currentHour: Int, currentMinute: Int, sunriseMins: Int, sunsetMins: Int): List<BarvelaSlot> {
        val slots = mutableListOf<BarvelaSlot>()
        val startIndex = getStartIndex(dayOfWeek)
        
        val dayDuration = sunsetMins - sunriseMins
        val nightDuration = 1440 - dayDuration
        
        val daySlotDuration = dayDuration / 12.0
        val nightSlotDuration = nightDuration / 12.0

        val currentTimeMinutes = currentHour * 60 + currentMinute
        val normalizedCurrent = if (currentTimeMinutes < sunriseMins) currentTimeMinutes + 1440 else currentTimeMinutes

        // 12 Day Horas
        for (i in 0 until 12) {
            val slotStart = (sunriseMins + i * daySlotDuration).roundToInt()
            val slotEnd = (sunriseMins + (i + 1) * daySlotDuration).roundToInt()
            val hora = horaLords[(startIndex + i) % 7]
            
            val isCurrent = normalizedCurrent >= slotStart && normalizedCurrent < slotEnd
            val timeStr = formatTimeRange(slotStart % 1440, slotEnd % 1440)
            
            slots.add(BarvelaSlot("दिन: $timeStr", "${hora.first} ग्रह होरा", hora.second, isCurrent))
        }

        // 12 Night Horas
        for (i in 0 until 12) {
            val slotStart = (sunsetMins + i * nightSlotDuration).roundToInt()
            val slotEnd = (sunsetMins + (i + 1) * nightSlotDuration).roundToInt()
            val hora = horaLords[(startIndex + 12 + i) % 7]
            
            val isCurrent = normalizedCurrent >= slotStart && normalizedCurrent < slotEnd
            val timeStr = formatTimeRange(slotStart % 1440, slotEnd % 1440)
            
            slots.add(BarvelaSlot("रात: $timeStr", "${hora.first} ग्रह होरा", hora.second, isCurrent))
        }

        return slots
    }

    private fun formatTimeRange(startMins: Int, endMins: Int): String {
        val startH = (startMins / 60) % 24
        val startM = startMins % 60
        val endH = (endMins / 60) % 24
        val endM = endMins % 60

        val periodStart = PanchangaCalculator.getTimePeriodName(startH)
        val dispStartH = if (startH == 0) 12 else if (startH > 12) startH - 12 else startH
        val periodEnd = PanchangaCalculator.getTimePeriodName(endH)
        val dispEndH = if (endH == 0) 12 else if (endH > 12) endH - 12 else endH

        return "$periodStart ${NepaliNumerals.toNepaliDigits(dispStartH)}:${NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", startM))} - $periodEnd ${NepaliNumerals.toNepaliDigits(dispEndH)}:${NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", endM))}"
    }
}
