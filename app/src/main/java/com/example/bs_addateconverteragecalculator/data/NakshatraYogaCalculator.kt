@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import java.time.LocalDate
import java.util.Locale
import kotlin.math.floor
import kotlin.math.roundToInt

object NakshatraYogaCalculator {
    val nakshatras = listOf(
        "अश्विनी", "भरणी", "कृत्तिका", "रोहिणी", "मृगशिरा", "आर्द्रा",
        "पुनर्वसु", "पुष्य", "आश्लेषा", "मघा", "पूर्वाफाल्गुनी", "उत्तराफाल्गुनी",
        "हस्त", "चित्रा", "स्वाति", "विशाखा", "अनुराधा", "ज्येष्ठा",
        "मूल", "पूर्वाषाढा", "उत्तराषाढा", "श्रवण", "धनिष्ठा", "शतभिषा",
        "पूर्वाभाद्रपद", "उत्तराभाद्रपद", "रेवती"
    )

    val yogas = listOf(
        "विष्कुम्भ", "प्रीति", "आयुष्मान", "सौभाग्य", "शोभन", "अतिगण्ड",
        "सुकर्म", "धृति", "शूल", "गण्ड", "वृद्धि", "ध्रुव",
        "व्याघात", "हर्षण", "वज्र", "सिद्धि", "व्यतीपात", "वरीयान",
        "परिघ", "शिव", "सिद्ध", "साध्य", "शुभ", "शुक्ल",
        "ब्रह्म", "ऐन्द्र", "वैधृति"
    )

    fun getNakshatraValue(adDate: LocalDate, hour: Double, tzOffset: Double = 5.75): Double {
        val pos = AstroCalculator.getPositions(adDate, hour, tzOffset)
        return pos.moonLongitude / (360.0 / 27.0)
    }

    fun getYogaValue(adDate: LocalDate, hour: Double, tzOffset: Double = 5.75): Double {
        val pos = AstroCalculator.getPositions(adDate, hour, tzOffset)
        val sum = (pos.sunLongitude + pos.moonLongitude) % 360.0
        return sum / (360.0 / 27.0)
    }

    /**
     * Iterative Newton-Raphson method for exact Nakshatra end time and Pada / Namakshar.
     */
    fun getExactNakshatraEnd(
        adDate: LocalDate,
        sunriseHour: Double,
        tzOffset: Double = 5.75
    ): AngaExactResult {
        val posStart = AstroCalculator.getPositions(adDate, sunriseHour, tzOffset)
        val lonStart = (posStart.moonLongitude % 360.0 + 360.0) % 360.0
        val span = 40.0 / 3.0
        val index = floor(lonStart / span).toInt().coerceIn(0, 26)
        val targetAngle = ((index + 1) * span) % 360.0

        var t = sunriseHour
        for (i in 0 until 4) {
            val pos = AstroCalculator.getPositions(adDate, t, tzOffset)
            val currentLon = (pos.moonLongitude % 360.0 + 360.0) % 360.0
            var angleRemaining = targetAngle - currentLon

            if (angleRemaining < -180.0) angleRemaining += 360.0
            if (angleRemaining > 180.0) angleRemaining -= 360.0

            val moonSpeedDegPerHour = 0.549016
            t += angleRemaining / moonSpeedDegPerHour
        }

        val hoursAfterSunrise = t - sunriseHour
        val totalGhadi = hoursAfterSunrise * 2.5

        val ghadi = floor(totalGhadi).toInt()
        val remPala = (totalGhadi - ghadi) * 60.0
        val pala = floor(remPala).toInt()
        val vipala = ((remPala - pala) * 60.0).roundToInt()

        val name = nakshatras[index]
        val pada = floor((lonStart % span) / (10.0 / 3.0)).toInt().coerceIn(0, 3) + 1
        val padaIndex = floor((lonStart * 3.0) / 10.0).toInt().coerceIn(0, 107)
        val namakshar = PanchangaRemainingAngas.NAMAKSHAR_108[padaIndex]

        val hr = floor((t % 24.0 + 24.0) % 24.0).toInt()
        val mn = floor(((t % 24.0 + 24.0) % 24.0 - hr) * 60.0).toInt()
        val endTimeStr = String.format(Locale.US, "%02d:%02d", hr, mn)

        return AngaExactResult(
            index = index,
            name = name,
            extraInfo = "पाउ: $pada (नामाक्षर: '$namakshar')",
            ghadi = ghadi,
            pala = pala,
            vipala = vipala,
            totalGhadi = totalGhadi,
            endHourLocal = t,
            endTimeFormatted = endTimeStr
        )
    }

    /**
     * Iterative Newton-Raphson method for exact Yoga end time.
     */
    fun getExactYogaEnd(
        adDate: LocalDate,
        sunriseHour: Double,
        tzOffset: Double = 5.75
    ): AngaExactResult {
        val posStart = AstroCalculator.getPositions(adDate, sunriseHour, tzOffset)
        val sumStart = ((posStart.sunLongitude + posStart.moonLongitude) % 360.0 + 360.0) % 360.0
        val span = 40.0 / 3.0
        val index = floor(sumStart / span).toInt().coerceIn(0, 26)
        val targetAngle = ((index + 1) * span) % 360.0

        var t = sunriseHour
        for (i in 0 until 4) {
            val pos = AstroCalculator.getPositions(adDate, t, tzOffset)
            val currentSum = ((pos.sunLongitude + pos.moonLongitude) % 360.0 + 360.0) % 360.0
            var angleRemaining = targetAngle - currentSum

            if (angleRemaining < -180.0) angleRemaining += 360.0
            if (angleRemaining > 180.0) angleRemaining -= 360.0

            val combinedSpeedDegPerHour = 0.590088
            t += angleRemaining / combinedSpeedDegPerHour
        }

        val hoursAfterSunrise = t - sunriseHour
        val totalGhadi = hoursAfterSunrise * 2.5

        val ghadi = floor(totalGhadi).toInt()
        val remPala = (totalGhadi - ghadi) * 60.0
        val pala = floor(remPala).toInt()
        val vipala = ((remPala - pala) * 60.0).roundToInt()

        val name = yogas[index]

        val hr = floor((t % 24.0 + 24.0) % 24.0).toInt()
        val mn = floor(((t % 24.0 + 24.0) % 24.0 - hr) * 60.0).toInt()
        val endTimeStr = String.format(Locale.US, "%02d:%02d", hr, mn)

        return AngaExactResult(
            index = index,
            name = name,
            extraInfo = "",
            ghadi = ghadi,
            pala = pala,
            vipala = vipala,
            totalGhadi = totalGhadi,
            endHourLocal = t,
            endTimeFormatted = endTimeStr
        )
    }

    fun findNakshatraCrossingHour(adDate: LocalDate, startHour: Double, tzOffset: Double = 5.75): Double {
        val exact = getExactNakshatraEnd(adDate, startHour, tzOffset)
        return exact.endHourLocal
    }

    fun findYogaCrossingHour(adDate: LocalDate, startHour: Double, tzOffset: Double = 5.75): Double {
        val exact = getExactYogaEnd(adDate, startHour, tzOffset)
        return exact.endHourLocal
    }

    fun getYoga(adDate: LocalDate, lat: Double, lon: Double, tzOffset: Double = 5.75): String {
        val (sunriseMins, _) = PanchangaCalculator.getSunriseSunsetMinutes(adDate, lat, lon, tzOffset)
        val yValue = getYogaValue(adDate, sunriseMins / 60.0, tzOffset)
        return yogas[yValue.toInt() % 27]
    }

    fun getYoga(adDate: LocalDate, hour: Double, tzOffset: Double = 5.75): String {
        val yValue = getYogaValue(adDate, hour, tzOffset)
        return yogas[yValue.toInt() % 27]
    }
}

