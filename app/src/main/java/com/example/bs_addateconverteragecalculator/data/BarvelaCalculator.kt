@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import java.time.DayOfWeek
import java.util.Locale
import kotlin.math.roundToInt

data class BarvelaSlot(
    val timeRange: String,
    val nameNepali: String,
    val type: String,
    val isCurrent: Boolean
)

object BarvelaCalculator {
    data class VelaInfo(val name: String, val type: String, val desc: String)

    private val allVela = listOf(
        VelaInfo("अमृत चौघडी", "शुभ", "उत्तम फलदायी, सबै कार्य शुभ"),
        VelaInfo("लाभ चौघडी", "शुभ", "व्यापार र धन लाभ"),
        VelaInfo("चर चौघडी", "शुभ", "यात्रा तथा सामान्य कार्य"),
        VelaInfo("रोग चौघडी", "अशुभ", "स्वास्थ्य सावधानी र बाधा"),
        VelaInfo("काल चौघडी", "अशुभ", "हानि, अशुभ कार्य वर्जित"),
        VelaInfo("उद्वेग चौघडी", "अशुभ", "चिन्ता, विवाद र असफलता"),
        VelaInfo("शुभ चौघडी", "शुभ", "मांगलिक तथा शुभ कार्य")
    )

    private fun getStartIndex(dayOfWeek: DayOfWeek): Int {
        return when (dayOfWeek) {
            DayOfWeek.SUNDAY -> 5     
            DayOfWeek.MONDAY -> 0     
            DayOfWeek.TUESDAY -> 3    
            DayOfWeek.WEDNESDAY -> 1  
            DayOfWeek.THURSDAY -> 6   
            DayOfWeek.FRIDAY -> 2     
            DayOfWeek.SATURDAY -> 4   
        }
    }

    fun getBarvelaSchedule(dayOfWeek: DayOfWeek, currentHour: Int, currentMinute: Int, sunriseMins: Int, sunsetMins: Int): List<BarvelaSlot> {
        val slots = mutableListOf<BarvelaSlot>()
        val startIndex = getStartIndex(dayOfWeek)

        val dayDuration = sunsetMins - sunriseMins
        val nightDuration = 1440 - dayDuration
        
        val daySlotDuration = dayDuration / 8.0
        val nightSlotDuration = nightDuration / 8.0

        val currentTimeMinutes = currentHour * 60 + currentMinute
        val normalizedCurrent = if (currentTimeMinutes < sunriseMins) currentTimeMinutes + 1440 else currentTimeMinutes

        // 8 Day Slots (Sunrise to Sunset)
        for (i in 0 until 8) {
            val slotStart = (sunriseMins + i * daySlotDuration).roundToInt()
            val slotEnd = (sunriseMins + (i + 1) * daySlotDuration).roundToInt()
            val timeStr = formatTimeRange(slotStart, slotEnd)
            val vela = allVela[(startIndex + i) % allVela.size]
            val isCurrent = normalizedCurrent >= slotStart && normalizedCurrent < slotEnd

            slots.add(BarvelaSlot("दिन: $timeStr", "${vela.name} (${vela.type})", vela.desc, isCurrent))
        }

        // 8 Night Slots (Sunset to Next Sunrise)
        for (i in 0 until 8) {
            val slotStart = (sunsetMins + i * nightSlotDuration).roundToInt()
            val slotEnd = (sunsetMins + (i + 1) * nightSlotDuration).roundToInt()
            val timeStr = formatTimeRange(slotStart % 1440, slotEnd % 1440)
            val vela = allVela[(startIndex + 8 + i) % allVela.size]

            val isCurrent = normalizedCurrent >= slotStart && normalizedCurrent < slotEnd

            slots.add(BarvelaSlot("रात: $timeStr", "${vela.name} (${vela.type})", vela.desc, isCurrent))
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
