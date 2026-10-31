@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import java.time.DayOfWeek
import java.util.Locale
import kotlin.math.roundToInt

enum class TimingType { AUSPICIOUS, NEUTRAL, INAUSPICIOUS }

data class TimingSlot(
    val name: String,
    val lord: String,
    val type: TimingType,
    val startMins: Int,
    val endMins: Int,
    val isNight: Boolean,
    val chipText: String
) {
    fun formatTimeRange(isNepali: Boolean = true, lang: String = "ne"): String {
        return "${formatTime(startMins, isNepali, lang)} - ${formatTime(endMins, isNepali, lang)}"
    }

    private fun formatTime(mins: Int, isNepali: Boolean, lang: String): String {
        val h24 = (mins / 60) % 24
        val m = mins % 60
        val h12 = if (h24 == 0) 12 else if (h24 > 12) h24 - 12 else h24

        val rawTime = String.format(Locale.US, "%02d:%02d", h12, m)
        val timeDigits = NepaliNumerals.formatDigits(rawTime, isNepali)

        val period = if (isNepali) {
            when (h24) {
                in 0..3 -> "राति"
                in 4..11 -> "बिहान"
                in 12..15 -> "दिउँसो"
                in 16..19 -> "बेलुकी"
                else -> "राति"
            }
        } else if (lang == "hi") {
            if (h24 < 12) "पूर्वाह्न" else "अपराह्न"
        } else {
            if (h24 < 12) "AM" else "PM"
        }

        return "$timeDigits $period"
    }
}

object VedicTimingCalculator {
    private val horaLords = listOf("सूर्य", "शुक्र", "बुध", "चन्द्र", "शनि", "बृहस्पति", "मंगल")
    
    data class ChoghadiyaInfo(val name: String, val lord: String, val type: TimingType, val chipText: String)
    private val choghadiyaSequence = listOf(
        ChoghadiyaInfo("उद्वेग", "सूर्य", TimingType.INAUSPICIOUS, "उद्वेग / Anxiety"),
        ChoghadiyaInfo("चर", "शुक्र", TimingType.NEUTRAL, "सामान्य / Neutral"),
        ChoghadiyaInfo("लाभ", "बुध", TimingType.AUSPICIOUS, "लाभ / Profitable"),
        ChoghadiyaInfo("अमृत", "चन्द्र", TimingType.AUSPICIOUS, "सर्वोत्तम / Best"),
        ChoghadiyaInfo("काल", "शनि", TimingType.INAUSPICIOUS, "अशुभ / Avoid"),
        ChoghadiyaInfo("शुभ", "बृहस्पति", TimingType.AUSPICIOUS, "शुभ / Good"),
        ChoghadiyaInfo("रोग", "मंगल", TimingType.INAUSPICIOUS, "रोग / Disease")
    )

    private fun getDayStartIndex(dayOfWeek: DayOfWeek): Int {
        return when(dayOfWeek) {
            DayOfWeek.SUNDAY -> 0
            DayOfWeek.FRIDAY -> 1
            DayOfWeek.WEDNESDAY -> 2
            DayOfWeek.MONDAY -> 3
            DayOfWeek.SATURDAY -> 4
            DayOfWeek.THURSDAY -> 5
            DayOfWeek.TUESDAY -> 6
        }
    }

    fun getChoghadiyaSchedule(dayOfWeek: DayOfWeek, sunriseMins: Int, sunsetMins: Int): List<TimingSlot> {
        val slots = mutableListOf<TimingSlot>()
        val startIndex = getDayStartIndex(dayOfWeek)
        val dayDuration = sunsetMins - sunriseMins
        val nightDuration = 1440 - dayDuration
        val daySlotLen = dayDuration / 8.0
        val nightSlotLen = nightDuration / 8.0

        for (i in 0 until 8) {
            val c = choghadiyaSequence[(startIndex + i) % 7]
            val start = (sunriseMins + i * daySlotLen).roundToInt()
            val end = (sunriseMins + (i + 1) * daySlotLen).roundToInt()
            slots.add(TimingSlot(c.name, c.lord, c.type, start, end, false, c.chipText))
        }
        
        val nightStartIndex = (startIndex + 4) % 7
        for (i in 0 until 8) {
            val c = choghadiyaSequence[(nightStartIndex + i) % 7]
            val start = (sunsetMins + i * nightSlotLen).roundToInt()
            val end = (sunsetMins + (i + 1) * nightSlotLen).roundToInt()
            slots.add(TimingSlot(c.name, c.lord, c.type, start, end, true, c.chipText))
        }
        return slots
    }

    fun getHoraSchedule(dayOfWeek: DayOfWeek, sunriseMins: Int, sunsetMins: Int): List<TimingSlot> {
        val slots = mutableListOf<TimingSlot>()
        val startIndex = getDayStartIndex(dayOfWeek)
        val dayDuration = sunsetMins - sunriseMins
        val nightDuration = 1440 - dayDuration
        val daySlotLen = dayDuration / 12.0
        val nightSlotLen = nightDuration / 12.0

        for (i in 0 until 12) {
            val lord = horaLords[(startIndex + i) % 7]
            val type = getHoraType(lord)
            val chipText = if(type == TimingType.AUSPICIOUS) "शुभ / Auspicious" else if(type == TimingType.INAUSPICIOUS) "अशुभ / Inauspicious" else "सामान्य / Neutral"
            val start = (sunriseMins + i * daySlotLen).roundToInt()
            val end = (sunriseMins + (i + 1) * daySlotLen).roundToInt()
            slots.add(TimingSlot("होरा", lord, type, start, end, false, chipText))
        }

        for (i in 0 until 12) {
            val lord = horaLords[(startIndex + 12 + i) % 7]
            val type = getHoraType(lord)
            val chipText = if(type == TimingType.AUSPICIOUS) "शुभ / Auspicious" else if(type == TimingType.INAUSPICIOUS) "अशुभ / Inauspicious" else "सामान्य / Neutral"
            val start = (sunsetMins + i * nightSlotLen).roundToInt()
            val end = (sunsetMins + (i + 1) * nightSlotLen).roundToInt()
            slots.add(TimingSlot("होरा", lord, type, start, end, true, chipText))
        }
        return slots
    }

    private fun getHoraType(lord: String): TimingType {
        return when (lord) {
            "सूर्य", "मंगल", "शनि" -> TimingType.INAUSPICIOUS
            "चन्द्र", "शुक्र", "बुध", "बृहस्पति" -> TimingType.AUSPICIOUS
            else -> TimingType.NEUTRAL
        }
    }
}