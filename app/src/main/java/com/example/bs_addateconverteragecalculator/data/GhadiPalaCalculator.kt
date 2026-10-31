@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import java.time.LocalTime
import kotlin.math.floor
import kotlin.math.roundToInt

object GhadiPalaCalculator {
    fun getCurrentGhadiPala(): String {
        val now = LocalTime.now()
        val sunriseMinutes = 6 * 60 // 06:00 AM
        val totalSeconds = now.hour * 3600 + now.minute * 60 + now.second
        var diffSeconds = totalSeconds - (sunriseMinutes * 60)
        if (diffSeconds < 0) diffSeconds += 24 * 3600

        val totalMinutes = diffSeconds / 60.0
        val ghadi = (totalMinutes / 24.0).toInt()
        val remainderMin = totalMinutes % 24.0
        val pala = (remainderMin * 2.5).toInt()
        val bipal = (((remainderMin * 2.5) % 1.0) * 60).toInt()

        return "${NepaliNumerals.toNepaliDigits(ghadi)} घडी ${NepaliNumerals.toNepaliDigits(pala)} पला ${NepaliNumerals.toNepaliDigits(bipal)} विपला"
    }

    fun formatGhadiPala(totalGhadi: Double): String {
        val ghadi = floor(totalGhadi).toInt()
        val remPala = (totalGhadi - ghadi) * 60.0
        val pala = floor(remPala).toInt()
        val vipala = ((remPala - pala) * 60.0).roundToInt()

        return "${NepaliNumerals.toNepaliDigits(ghadi)} घडी ${NepaliNumerals.toNepaliDigits(pala)} पला ${NepaliNumerals.toNepaliDigits(vipala)} विपला"
    }
}

