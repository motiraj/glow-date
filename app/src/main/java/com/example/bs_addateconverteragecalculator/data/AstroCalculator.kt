@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale
import kotlin.math.*

@Immutable
data class GrahaTransit(
    val id: String,
    val nameDevanagari: String,
    val rashiName: String,
    val formattedDegree: String,
    val isRetrograde: Boolean,
    val isCombust: Boolean = false
)

@Immutable
data class PlanetaryTransitUiState(
    val isLoading: Boolean = false,
    val ayanamshaName: String = "चित्रापक्ष (लाहिरी) अयनांश",
    val planets: ImmutableList<GrahaTransit> = persistentListOf()
)

data class PlanetaryData(val sunLongitude: Double, val moonLongitude: Double, val ayanamsa: Double)

data class DetailedGrahaGochar(
    val id: String,
    val grahaName: String,
    val rashiName: String,
    val degreesText: String,
    val nakshatraName: String,
    val pada: Int,
    val formattedSummary: String,
    val isRetrograde: Boolean,
    val exactLongitude: Double,
    val isCombust: Boolean = false
)

object AstroCalculator {

    private fun getJulianDay(date: LocalDate, time: LocalTime, timeZoneOffset: Double = 5.75): Double {
        var hourUtc = time.hour + time.minute / 60.0 + time.second / 3600.0 - timeZoneOffset
        var y = date.year
        var m = date.monthValue
        var d = date.dayOfMonth.toDouble()

        while (hourUtc < 0.0) {
            hourUtc += 24.0
            d -= 1.0
        }
        while (hourUtc >= 24.0) {
            hourUtc -= 24.0
            d += 1.0
        }

        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2.0 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716.0)) + floor(30.6001 * (m + 1.0)) + d + (hourUtc / 24.0) + b - 1524.5
    }

    private fun dSin(deg: Double) = sin(Math.toRadians(deg))

    fun getPositions(date: LocalDate, time: LocalTime, timeZoneOffset: Double = 5.75): PlanetaryData {
        val jd = getJulianDay(date, time, timeZoneOffset)
        val t = (jd - 2451545.0) / 36525.0

        val l0 = (280.46646 + 36000.76983 * t) % 360.0
        val mSun = (357.52911 + 35999.05029 * t) % 360.0

        val cSun = (1.914602 - 0.004817 * t) * dSin(mSun) + (0.019993 - 0.000101 * t) * dSin(2 * mSun) + 0.000289 * dSin(3 * mSun)

        var lSun = (l0 + cSun) % 360.0
        if (lSun < 0) lSun += 360.0

        val lPrime = (218.3164477 + 481267.88123421 * t) % 360.0
        val dElong = (297.8501921 + 445267.1114034 * t) % 360.0
        val mMoon = (134.9633964 + 477198.8675055 * t) % 360.0
        val fArg = (93.2720950 + 483202.0175233 * t) % 360.0

        var lMoon = lPrime +
            6.288774 * dSin(mMoon) +
            1.274027 * dSin(2 * dElong - mMoon) +
            0.658314 * dSin(2 * dElong) +
            0.213618 * dSin(2 * mMoon) -
            0.185116 * dSin(mSun) -
            0.114332 * dSin(2 * fArg) +
            0.058793 * dSin(2 * dElong - 2 * mMoon) +
            0.057066 * dSin(2 * dElong - mSun - mMoon) +
            0.053322 * dSin(2 * dElong + mMoon) +
            0.045758 * dSin(2 * dElong - mSun) -
            0.040923 * dSin(mSun - mMoon) -
            0.034720 * dSin(dElong) -
            0.030383 * dSin(mSun + mMoon)
        lMoon %= 360.0
        if (lMoon < 0) lMoon += 360.0

        val ayanamsha = 23.857092 + (1.396971 * t) + (0.000308 * t * t)

        var sunNir = (lSun - ayanamsha) % 360.0
        if (sunNir < 0) sunNir += 360.0
        var moonNir = (lMoon - ayanamsha) % 360.0
        if (moonNir < 0) moonNir += 360.0

        return PlanetaryData(sunNir, moonNir, ayanamsha)
    }

    fun getPositions(date: LocalDate, hourLocal: Double, timeZoneOffset: Double = 5.75): PlanetaryData {
        var h = if (hourLocal.isNaN() || hourLocal.isInfinite()) 12.0 else hourLocal
        var dOffset = 0
        while (h >= 24.0) { h -= 24.0; dOffset++ }
        while (h < 0.0) { h += 24.0; dOffset-- }

        val targetDate = date.plusDays(dOffset.toLong())
        val hr = h.toInt()
        val mn = ((h - hr) * 60).toInt()
        val sc = ((((h - hr) * 60) - mn) * 60).toInt()

        val safeHr = hr.coerceIn(0, 23)
        val safeMn = mn.coerceIn(0, 59)
        val safeSc = sc.coerceIn(0, 59)

        val time = LocalTime.of(safeHr, safeMn, safeSc)
        return getPositions(targetDate, time, timeZoneOffset)
    }

    fun formatDegreeDms(nirayanaLon: Double): String {
        var n = nirayanaLon % 360.0
        if (n < 0) n += 360.0
        val degInRashi = n % 30.0
        val totalSeconds = (degInRashi * 3600.0).roundToInt()
        val deg = totalSeconds / 3600
        val min = (totalSeconds % 3600) / 60
        val sec = totalSeconds % 60

        val degStr = NepaliNumerals.toNepaliDigits(deg)
        val minStr = NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", min))
        val secStr = NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", sec))
        return "$degStr° $minStr' $secStr\""
    }

    fun getDetailedGochar(
        date: LocalDate,
        time: LocalTime,
        timeZoneOffset: Double = 5.75,
        ayanamsaMode: AyanamsaMode = AyanamsaMode.LAHIRI_DRIK
    ): List<DetailedGrahaGochar> {
        val snapshot = OfflineVedicEphemeris.getPlanetarySnapshot(
            year = date.year,
            month = date.monthValue,
            day = date.dayOfMonth,
            hour = time.hour,
            minute = time.minute,
            second = time.second,
            tzOffsetHours = timeZoneOffset,
            ayanamsaMode = ayanamsaMode
        )

        fun createDetail(id: String, grahaName: String, lon: Double, isRetrograde: Boolean, isCombust: Boolean): DetailedGrahaGochar {
            val rak = OfflineVedicEphemeris.toRashiAmshaKala(lon)
            val rashiName = rak.rashiName
            val formattedDegree = formatDegreeDms(lon)

            val nakshatraIdx = (lon / (360.0 / 27.0)).toInt().coerceIn(0, 26)
            val nakshatraName = NakshatraYogaCalculator.nakshatras[nakshatraIdx]
            val pada = (((lon % (360.0 / 27.0)) / (360.0 / 108.0)).toInt() % 4) + 1

            val summary = "$rashiName $formattedDegree ($nakshatraName पद ${NepaliNumerals.toNepaliDigits(pada)})"

            return DetailedGrahaGochar(
                id = id,
                grahaName = grahaName,
                rashiName = rashiName,
                degreesText = formattedDegree,
                nakshatraName = nakshatraName,
                pada = pada,
                formattedSummary = summary,
                isRetrograde = isRetrograde,
                exactLongitude = lon,
                isCombust = isCombust
            )
        }

        return listOf(
            createDetail("sun", "सूर्य", snapshot.sunLon, isRetrograde = false, isCombust = false),
            createDetail("moon", "चन्द्र", snapshot.moonLon, isRetrograde = false, isCombust = false),
            createDetail("mars", "मंगल", snapshot.marsLon, isRetrograde = snapshot.isMarsRetrograde, isCombust = snapshot.isMarsCombust),
            createDetail("mercury", "बुध", snapshot.mercuryLon, isRetrograde = snapshot.isMercuryRetrograde, isCombust = snapshot.isMercuryCombust),
            createDetail("jupiter", "बृहस्पति", snapshot.jupiterLon, isRetrograde = snapshot.isJupiterRetrograde, isCombust = snapshot.isJupiterCombust),
            createDetail("venus", "शुक्र", snapshot.venusLon, isRetrograde = snapshot.isVenusRetrograde, isCombust = snapshot.isVenusCombust),
            createDetail("saturn", "शनि", snapshot.saturnLon, isRetrograde = snapshot.isSaturnRetrograde, isCombust = snapshot.isSaturnCombust),
            createDetail("rahu", "राहु", snapshot.rahuLon, isRetrograde = true, isCombust = false),
            createDetail("ketu", "केतु", snapshot.ketuLon, isRetrograde = true, isCombust = false)
        )
    }

    fun getGochar(date: LocalDate, time: LocalTime, timeZoneOffset: Double = 5.75): List<Pair<String, String>> {
        val detailed = getDetailedGochar(date, time, timeZoneOffset)
        return detailed.map { it.grahaName to "${it.rashiName} ${it.degreesText}" }
    }

    fun formatTime24Short(totalH: Double): String {
        var h = totalH
        while (h >= 24.0) { h -= 24.0 }
        while (h < 0.0) { h += 24.0 }

        var hr = h.toInt()
        var m = ((h - hr) * 60).roundToInt()
        
        if (m == 60) {
            m = 0
            hr += 1
            if (hr == 24) {
                hr = 0
            }
        }

        val hStr = NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", hr))
        val mStr = NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", m))
        
        return "$hStr:$mStr"
    }
}
