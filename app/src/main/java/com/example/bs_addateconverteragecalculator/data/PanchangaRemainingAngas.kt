@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import java.util.Locale
import kotlin.math.floor
import kotlin.math.roundToInt

data class AngaDetail(
    val number: Int,
    val name: String,
    val extraInfo: String = "",
    val ghadi: Int,
    val pala: Int,
    val vipala: Int,
    val endTimeClock: String
)

data class AngaExactResult(
    val index: Int,
    val name: String,
    val extraInfo: String = "",
    val ghadi: Int,
    val pala: Int,
    val vipala: Int,
    val totalGhadi: Double,
    val endHourLocal: Double,
    val endTimeFormatted: String
)

object PanchangaRemainingAngas {

    val NAKSHATRA_NAMES = arrayOf(
        "अश्विनी", "भरणी", "कृत्तिका", "रोहिणी", "मृगशिरा", "आर्द्रा", "पुनर्वसु", "तिष्य (पुष्य)", "अश्लेषा",
        "मघा", "पूर्वफाल्गुनी", "उत्तरफाल्गुनी", "हस्ता", "चित्रा", "स्वाती", "विशाखा", "अनुराधा", "ज्येष्ठा",
        "मूल", "पूर्वाषाढा", "उत्तराषाढा", "श्रवण", "धनिष्ठा", "शतभिषा", "पूर्वभाद्रपदा", "उत्तरभाद्रपदा", "रेवती"
    )

    val YOGA_NAMES = arrayOf(
        "विष्कुम्भ", "प्रीति", "आयुष्मान्", "सौभाग्य", "शोभन", "अतिगण्ड", "सुकर्मा", "धृति", "शूल",
        "गण्ड", "वृद्धि", "ध्रुव", "व्याघात", "हर्षण", "वज्र", "सिद्धि", "व्यतीपात", "वरीयान्",
        "परिघ", "शिव", "सिद्ध", "साध्य", "शुभ", "शुक्ल", "ब्रह्म", "ऐन्द्र (इन्द्र)", "वैधृति"
    )

    val CHARA_KARANA_NAMES = arrayOf(
        "बव", "बालव", "कौलव", "तैतिल", "गर", "वणिज", "विष्टि (भद्रा)"
    )

    // 108 Namakshars mapping for 27 Nakshatras x 4 Padas
    val NAMAKSHAR_108 = arrayOf(
        "चु", "चे", "चो", "ला", "ली", "लू", "ले", "लो", "अ", "ई", "उ", "ए",
        "ओ", "वा", "वी", "वू", "वे", "वो", "का", "की", "कू", "घ", "ङ", "छ",
        "के", "को", "हा", "ही", "हू", "हे", "हो", "डा", "डी", "डू", "डे", "डो",
        "मा", "मी", "मू", "मे", "मो", "टा", "टी", "टू", "टे", "टो", "पा", "पी",
        "पू", "ष", "ण", "ठ", "पे", "पो", "रा", "री", "रू", "रे", "रो", "ता",
        "ती", "तू", "ते", "तो", "ना", "नी", "नू", "ने", "नो", "या", "यी", "यू",
        "ये", "यो", "भा", "भी", "भू", "धा", "फा", "ढा", "भे", "भो", "जा", "जी",
        "खी", "खू", "खे", "खो", "गा", "गी", "गू", "गे", "गो", "सा", "सी", "सू",
        "से", "सो", "द", "दी", "दू", "थ", "झ", "ञ", "दे", "दो", "च", "ची"
    )

    private fun normalize(deg: Double): Double = ((deg % 360.0) + 360.0) % 360.0

    private fun computeTimeMetrics(
        remainingDeg: Double,
        dailySpeed: Double,
        sunriseHours: Double
    ): Quadruple<Int, Int, Int, String> {
        val totalGhadi = (remainingDeg / dailySpeed) * 60.0
        val ghadi = floor(totalGhadi).toInt()
        val remGhadi = (totalGhadi - ghadi) * 60.0
        val pala = floor(remGhadi).toInt()
        val vipala = ((remGhadi - pala) * 60.0).roundToInt()

        val endHours = (sunriseHours + (totalGhadi * 0.4)) % 24.0
        val hr = floor(endHours).toInt()
        val min = floor((endHours - hr) * 60.0).toInt()
        val clockStr = String.format(Locale.US, "%02d:%02d", hr, min)

        return Quadruple(ghadi, pala, vipala, clockStr)
    }

    // 1. NAKSHATRA & PADA
    fun calculateNakshatra(
        moonLon: Double,
        moonSpeed: Double = 13.1764,
        sunriseHours: Double = 6.0
    ): AngaDetail {
        val lon = normalize(moonLon)
        val span = 40.0 / 3.0

        val index = floor(lon / span).toInt().coerceIn(0, 26)
        val elapsed = lon % span
        val remaining = span - elapsed

        val pada = floor(elapsed / (10.0 / 3.0)).toInt().coerceIn(0, 3) + 1
        val padaIndex = floor((lon * 3.0) / 10.0).toInt().coerceIn(0, 107)
        val namakshar = NAMAKSHAR_108[padaIndex]

        val (ghadi, pala, vipala, clock) = computeTimeMetrics(remaining, moonSpeed, sunriseHours)

        return AngaDetail(
            number = index + 1,
            name = NAKSHATRA_NAMES[index],
            extraInfo = "पाउ: $pada (नामाक्षर: '$namakshar')",
            ghadi = ghadi,
            pala = pala,
            vipala = vipala,
            endTimeClock = clock
        )
    }

    // 2. YOGA
    fun calculateYoga(
        sunLon: Double,
        moonLon: Double,
        sunSpeed: Double = 0.9856,
        moonSpeed: Double = 13.1764,
        sunriseHours: Double = 6.0
    ): AngaDetail {
        val sumLon = normalize(sunLon + moonLon)
        val span = 40.0 / 3.0

        val index = floor(sumLon / span).toInt().coerceIn(0, 26)
        val elapsed = sumLon % span
        val remaining = span - elapsed

        val combinedSpeed = moonSpeed + sunSpeed
        val (ghadi, pala, vipala, clock) = computeTimeMetrics(remaining, combinedSpeed, sunriseHours)

        return AngaDetail(
            number = index + 1,
            name = YOGA_NAMES[index],
            ghadi = ghadi,
            pala = pala,
            vipala = vipala,
            endTimeClock = clock
        )
    }

    // 3. KARANA
    fun calculateKarana(
        sunLon: Double,
        moonLon: Double,
        sunSpeed: Double = 0.9856,
        moonSpeed: Double = 13.1764,
        sunriseHours: Double = 6.0
    ): AngaDetail {
        val diff = normalize(moonLon - sunLon)
        val span = 6.0

        val kIndex = floor(diff / span).toInt().coerceIn(0, 59)

        val karanaName = when (kIndex) {
            0 -> "किंस्तुघ्न"
            57 -> "शकुनि"
            58 -> "चतुष्पाद"
            59 -> "नाग"
            else -> CHARA_KARANA_NAMES[(kIndex - 1) % 7]
        }

        val elapsed = diff % span
        val remaining = span - elapsed

        val relativeSpeed = moonSpeed - sunSpeed
        val (ghadi, pala, vipala, clock) = computeTimeMetrics(remaining, relativeSpeed, sunriseHours)

        return AngaDetail(
            number = kIndex + 1,
            name = karanaName,
            extraInfo = if (karanaName.contains("भद्रा")) "भद्रा व्याप्त (अशुभ)" else "शुद्ध",
            ghadi = ghadi,
            pala = pala,
            vipala = vipala,
            endTimeClock = clock
        )
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
