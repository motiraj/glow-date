@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale
import kotlin.math.*

data class MuhuratTiming(
    val title: String,
    val timeRange: String,
    val isAuspicious: Boolean,
    val description: String
)

object PanchangaCalculator {
    fun getTimePeriodName(hour24: Int): String {
        val h = (hour24 % 24 + 24) % 24
        return when (h) {
            in 0..3 -> "राति"
            in 4..11 -> "बिहान"
            in 12..15 -> "दिउँसो"
            in 16..19 -> "बेलुकी"
            else -> "राति"
        }
    }

    fun getCurrentTimeNepali(): String {
        val now = LocalTime.now()
        val hour = now.hour
        val minute = now.minute
        val second = now.second
        val period = getTimePeriodName(hour)
        val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        return "$period ${NepaliNumerals.toNepaliDigits(displayHour)}:${NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", minute))}:${NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", second))}"
    }

    /**
     * Calculates Tithi, Paksha, Ghadi-Pala, Vipala, and exact end time based on Sun and Moon longitudes and relative speeds.
     */
    fun calculateTithiAndGhadiPala(
        sunLon: Double,
        moonLon: Double,
        sunSpeed: Double = 0.9856,
        moonSpeed: Double = 13.1764,
        sunriseHours: Double = 6.0
    ): TithiResult {
        return TithiCalculator.calculateTithiAndGhadiPala(sunLon, moonLon, sunSpeed, moonSpeed, sunriseHours)
    }

    // High Precision Astronomical Sunrise/Sunset configured by Latitude, Longitude, and TimeZone
    fun getSunriseSunsetMinutes(
        adDate: LocalDate,
        lat: Double,
        lon: Double,
        tz: Double
    ): Pair<Int, Int> {
        val year = adDate.year
        val month = adDate.monthValue
        val day = adDate.dayOfMonth

        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = y / 100
        val b = 2 - a + (a / 4)
        val jd = (365.25 * (y + 4716)).toLong() + (30.6001 * (m + 1)).toLong() + day + b - 1524.5

        val t = (jd - 2451545.0) / 36525.0

        val l0 = (280.46646 + 36000.76983 * t) % 360.0
        val mSun = (357.52911 + 35999.05029 * t) % 360.0
        
        val cSun = (1.914602 - 0.004817 * t) * sin(Math.toRadians(mSun)) + (0.019993 - 0.000101 * t) * sin(Math.toRadians(2 * mSun))
        val trueLong = l0 + cSun
        
        val e = 0.016708634 - 0.000042037 * t
        
        val omega = 125.04 - 1934.136 * t
        val lambda = trueLong - 0.00569 - 0.00478 * sin(Math.toRadians(omega))

        var epsilon = 23.0 + 26.0 / 60.0 + 21.448 / 3600.0 - (46.815 / 3600.0) * t
        epsilon += 0.00256 * cos(Math.toRadians(omega))

        val sinDecl = sin(Math.toRadians(epsilon)) * sin(Math.toRadians(lambda))
        val decl = Math.toDegrees(asin(sinDecl))

        val varY = tan(Math.toRadians(epsilon / 2)) * tan(Math.toRadians(epsilon / 2))
        val eqTimeMins = 4.0 * Math.toDegrees(varY * sin(Math.toRadians(2 * l0)) 
                            - 2 * e * sin(Math.toRadians(mSun)) 
                            + 4 * e * varY * sin(Math.toRadians(mSun)) * cos(Math.toRadians(2 * l0)) 
                            - 0.5 * varY * varY * sin(Math.toRadians(4 * l0)) 
                            - 1.25 * e * e * sin(Math.toRadians(2 * mSun)))

        val cosHA = ((cos(Math.toRadians(90.833)) - sin(Math.toRadians(lat)) * sin(Math.toRadians(decl))) / (cos(Math.toRadians(lat)) * cos(Math.toRadians(decl)))).coerceIn(-1.0, 1.0)
        val ha = Math.toDegrees(acos(cosHA))

        val solarNoon = (720.0 - 4.0 * lon - eqTimeMins + tz * 60.0)
        
        val sunriseMins = (solarNoon - (ha * 4.0)).roundToInt()
        val sunsetMins = (solarNoon + (ha * 4.0)).roundToInt()

        return sunriseMins to sunsetMins
    }

    fun getSunriseSunset(adDate: LocalDate, lat: Double, lon: Double, tz: Double): Pair<String, String> {
        val (sunriseMins, sunsetMins) = getSunriseSunsetMinutes(adDate, lat, lon, tz)
        val sunriseStr = formatTime(sunriseMins)
        val sunsetStr = formatTime(sunsetMins)
        val sunrisePeriod = getTimePeriodName(sunriseMins / 60)
        val sunsetPeriod = getTimePeriodName(sunsetMins / 60)
        return "$sunrisePeriod $sunriseStr" to "$sunsetPeriod $sunsetStr"
    }

    // Dynamic Calculation of Abhijit Muhurat and Rahu Kaal
    fun getRahuKaalAndAbhijit(adDate: LocalDate, lat: Double, lon: Double, tz: Double): List<MuhuratTiming> {
        val (sunriseMins, sunsetMins) = getSunriseSunsetMinutes(adDate, lat, lon, tz)
        val dayDuration = sunsetMins - sunriseMins
        val slot8 = dayDuration / 8.0
        val slot15 = dayDuration / 15.0

        val dayOfWeek = adDate.dayOfWeek

        // Rahu Kaal Slot Index (0 to 7)
        val rahuSlotIndex = when (dayOfWeek) {
            DayOfWeek.SUNDAY -> 7
            DayOfWeek.MONDAY -> 1
            DayOfWeek.TUESDAY -> 6
            DayOfWeek.WEDNESDAY -> 4
            DayOfWeek.THURSDAY -> 5
            DayOfWeek.FRIDAY -> 3
            DayOfWeek.SATURDAY -> 2
        }

        val rahuStart = (sunriseMins + rahuSlotIndex * slot8).roundToInt()
        val rahuEnd = (sunriseMins + (rahuSlotIndex + 1) * slot8).roundToInt()

        val abhijitStart = (sunriseMins + 7 * slot15).roundToInt()
        val abhijitEnd = (sunriseMins + 8 * slot15).roundToInt()

        fun formatMins(mins: Int): String {
            val h = (mins / 60) % 24
            val m = mins % 60
            val period = getTimePeriodName(h)
            val dispH = if (h == 0) 12 else if (h > 12) h - 12 else h
            val hStr = NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", dispH))
            val mStr = NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", m))
            return "$period $hStr:$mStr"
        }

        val rahuStr = "${formatMins(rahuStart)} - ${formatMins(rahuEnd)}"
        val abhijitStr = "${formatMins(abhijitStart)} - ${formatMins(abhijitEnd)}"

        return listOf(
            MuhuratTiming(
                title = "अभिजित मुहूर्त",
                timeRange = abhijitStr,
                isAuspicious = true,
                description = "दिनको सबैभन्दा शुभ समय। नयाँ तथा महत्त्वपूर्ण काम सुरु गर्न अति उत्तम।"
            ),
            MuhuratTiming(
                title = "राहुकाल",
                timeRange = rahuStr,
                isAuspicious = false,
                description = "अशुभ समय। यो अवधिमा नयाँ कार्य सुरु नगर्नुहोला।"
            )
        )
    }

    data class MuhuratInterval(
        val title: String,
        val startMinutes: Int,
        val endMinutes: Int,
        val isAuspicious: Boolean,
        val description: String
    )

    fun getMuhuratIntervals(adDate: LocalDate, lat: Double, lon: Double, tz: Double): List<MuhuratInterval> {
        val (sunriseMins, sunsetMins) = getSunriseSunsetMinutes(adDate, lat, lon, tz)
        val dayDuration = sunsetMins - sunriseMins
        val slot8 = dayDuration / 8.0
        val slot15 = dayDuration / 15.0

        val dayOfWeek = adDate.dayOfWeek
        val rahuSlotIndex = when (dayOfWeek) {
            DayOfWeek.SUNDAY -> 7
            DayOfWeek.MONDAY -> 1
            DayOfWeek.TUESDAY -> 6
            DayOfWeek.WEDNESDAY -> 4
            DayOfWeek.THURSDAY -> 5
            DayOfWeek.FRIDAY -> 3
            DayOfWeek.SATURDAY -> 2
        }

        val rahuStart = (sunriseMins + rahuSlotIndex * slot8).roundToInt()
        val rahuEnd = (sunriseMins + (rahuSlotIndex + 1) * slot8).roundToInt()

        val abhijitStart = (sunriseMins + 7 * slot15).roundToInt()
        val abhijitEnd = (sunriseMins + 8 * slot15).roundToInt()

        return listOf(
            MuhuratInterval(
                title = "अभिजित मुहूर्त",
                startMinutes = abhijitStart,
                endMinutes = abhijitEnd,
                isAuspicious = true,
                description = "दिनको सबैभन्दा शुभ समय। नयाँ तथा महत्त्वपूर्ण काम सुरु गर्न अति उत्तम।"
            ),
            MuhuratInterval(
                title = "राहुकाल",
                startMinutes = rahuStart,
                endMinutes = rahuEnd,
                isAuspicious = false,
                description = "अशुभ समय। यो अवधिमा नयाँ कार्य सुरु नगर्नुहोला।"
            )
        )
    }

    private fun formatTime(totalMinutes: Int): String {
        var h = (totalMinutes / 60) % 24
        val m = totalMinutes % 60
        if (h > 12) h -= 12
        if (h == 0) h = 12
        
        val hStr = NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", h))
        val mStr = NepaliNumerals.toNepaliDigits(String.format(Locale.US, "%02d", m))
        return "$hStr:$mStr"
    }
}
