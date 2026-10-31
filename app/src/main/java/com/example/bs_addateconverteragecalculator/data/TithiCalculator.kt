@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import java.time.LocalDate
import java.util.Locale
import kotlin.math.floor
import kotlin.math.roundToInt

data class TithiResult(
    val tithiNumber: Int,          // 1 to 30
    val paksha: String,            // शुक्ल पक्ष वा कृष्ण पक्ष
    val tithiName: String,         // प्रतिपदा, द्वितीया आदि
    val ghadi: Int,                // घडी
    val pala: Int,                 // पला
    val vipala: Int,               // विपला
    val totalGhadi: Double,        // कुल घडी
    val endTimeFormatted: String,  // घण्टा:मिनेट (सूर्योदय पछिको समय वा क्लक टाइम)
    val isTithiVriddhi: Boolean = false, // अहोरात्र (६० घडी वा सोभन्दा बढी)
    val isTithiKshaya: Boolean = false   // तिथि क्षय
)

data class TithiExactResult(
    val tithiIndex: Int,
    val tithiName: String,
    val paksha: String,
    val ghadi: Int,
    val pala: Int,
    val vipala: Int,
    val totalGhadi: Double,
    val endHourLocal: Double,
    val endTimeFormatted: String,
    val isAhoratra: Boolean = false,
    val isKshaya: Boolean = false
)

data class TithiKnowledge(
    val tithiNumber: Int,          // 1 to 15 (वा 30)
    val name: String,             // प्रतिपदा (परेवा), द्वितीया, आदि
    val deity: String,            // अग्नि, ब्रह्मा, आदि
    val category: String,         // नन्दा, भद्रा, जया, रिक्ता, पूर्णा
    val categoryMeaning: String,  // आनन्द दिने, कल्याण गर्ने, आदि
    val categoryUsage: String,    // शुभ कार्यका लागि राम्रो, आदि
    val mainFeature: String,      // महिना वा पक्षको पहिलो दिन...
    val isShubhaForWork: Boolean  // true / false
)

object TithiCalculator {
    val tithiNames = listOf(
        "शुक्ल प्रतिपदा", "शुक्ल द्वितीया", "शुक्ल तृतीया", "शुक्ल चतुर्थी", "शुक्ल पञ्चमी",
        "शुक्ल षष्ठी", "शुक्ल सप्तमी", "शुक्ल अष्टमी", "शुक्ल नवमी", "शुक्ल दशमी",
        "शुक्ल एकादशी", "शुक्ल द्वादशी", "शुक्ल त्रयोदशी", "शुक्ल चतुर्दशी", "पूर्णिमा",
        "कृष्ण प्रतिपदा", "कृष्ण द्वितीया", "कृष्ण तृतीया", "कृष्ण चतुर्थी", "कृष्ण पञ्चमी",
        "कृष्ण षष्ठी", "कृष्ण सप्तमी", "कृष्ण अष्टमी", "कृष्ण नवमी", "कृष्ण दशमी",
        "कृष्ण एकादशी", "कृष्ण द्वादशी", "कृष्ण त्रयोदशी", "कृष्ण चतुर्दशी", "औंसी"
    )

    private val TITHI_NAMES_SHORT = arrayOf(
        "प्रतिपदा", "द्वितीया", "तृतीया", "चतुर्थी", "पञ्चमी",
        "षष्ठी", "सप्तमी", "अष्टमी", "नवमी", "दशमी",
        "एकादशी", "द्वादशी", "त्रयोदशी", "चतुर्दशी", "पूर्णिमा", "औँसी"
    )

    private val movableKaranas = listOf("बव", "बालव", "कौलव", "तैतिल", "गर", "वणिज", "भद्रा (विष्टि)")

    private fun normalize360(deg: Double): Double {
        var d = deg % 360.0
        if (d < 0) d += 360.0
        return d
    }

    /**
     * Rashi-Amsha-Kala-Vikala to Decimal Degrees conversion helper:
     * Decimal Degrees = (Rashi * 30) + Degree + (Minute / 60) + (Second / 3600)
     */
    fun rashiDmsToDecimal(rashi: Int, deg: Int, min: Int, sec: Double): Double {
        return (rashi * 30.0) + deg + (min / 60.0) + (sec / 3600.0)
    }

    /**
     * Fast Tithi and Ghadi-Pala calculation using relative daily speeds.
     */
    fun calculateTithiAndGhadiPala(
        sunLon: Double,
        moonLon: Double,
        sunSpeed: Double = 0.9856,
        moonSpeed: Double = 13.1764,
        sunriseHours: Double = 6.0
    ): TithiResult {
        val diff = normalize360(moonLon - sunLon)
        val tithiIndex = floor(diff / 12.0).toInt().coerceIn(0, 29)
        val tithiNumber = tithiIndex + 1

        val paksha = if (tithiNumber <= 15) "शुक्ल पक्ष" else "कृष्ण पक्ष"
        val nameIndex = when {
            tithiNumber == 15 -> 14 // पूर्णिमा
            tithiNumber == 30 -> 15 // औँसी
            else -> (tithiIndex % 15)
        }
        val tithiName = TITHI_NAMES_SHORT[nameIndex]

        val elapsedDeg = diff % 12.0
        val remainingDeg = 12.0 - elapsedDeg

        val dailyRelativeSpeed = moonSpeed - sunSpeed
        val totalGhadi = (remainingDeg / dailyRelativeSpeed) * 60.0

        val ghadi = floor(totalGhadi).toInt()
        val remGhadi = (totalGhadi - ghadi) * 60.0
        val pala = floor(remGhadi).toInt()
        val vipala = ((remGhadi - pala) * 60.0).roundToInt()

        val isTithiVriddhi = totalGhadi >= 60.0

        val durationHours = totalGhadi * 0.4 // 1 ghadi = 24 mins = 0.4 hours
        val endClockHours = normalize360(sunriseHours + durationHours)
        val endHr = floor(endClockHours).toInt() % 24
        val endMin = floor((endClockHours - floor(endClockHours)) * 60.0).toInt()
        val endTimeStr = String.format(Locale.US, "%02d:%02d", endHr, endMin)

        return TithiResult(
            tithiNumber = tithiNumber,
            paksha = paksha,
            tithiName = tithiName,
            ghadi = ghadi,
            pala = pala,
            vipala = vipala,
            totalGhadi = totalGhadi,
            endTimeFormatted = endTimeStr,
            isTithiVriddhi = isTithiVriddhi
        )
    }

    /**
     * High-Precision Iterative Root Finding (Newton-Raphson / Secant Method)
     * For Exact Tithi End Time and Ghadi-Pala Calculation.
     * Uses 4 iterations for sub-second precision.
     */
    fun getExactTithiEnd(
        adDate: LocalDate,
        sunriseHour: Double,
        tzOffset: Double = 5.75
    ): TithiExactResult {
        val posStart = AstroCalculator.getPositions(adDate, sunriseHour, tzOffset)
        val diffStart = normalize360(posStart.moonLongitude - posStart.sunLongitude)
        val tithiIndex = floor(diffStart / 12.0).toInt().coerceIn(0, 29)
        val targetAngle = ((tithiIndex + 1) * 12.0) % 360.0

        var t = sunriseHour
        for (i in 0 until 4) {
            val pos = AstroCalculator.getPositions(adDate, t, tzOffset)
            val currentDiff = normalize360(pos.moonLongitude - pos.sunLongitude)
            var angleRemaining = targetAngle - currentDiff

            if (angleRemaining < -180.0) angleRemaining += 360.0
            if (angleRemaining > 180.0) angleRemaining -= 360.0

            // Relative orbital speed: average ~0.507945 degrees per hour
            val relativeSpeedDegPerHour = 0.507945
            t += angleRemaining / relativeSpeedDegPerHour
        }

        val hoursAfterSunrise = t - sunriseHour
        val totalGhadi = hoursAfterSunrise * 2.5 // 1 hr = 2.5 ghadi, 24 hrs = 60 ghadi

        val ghadi = floor(totalGhadi).toInt()
        val remPala = (totalGhadi - ghadi) * 60.0
        val pala = floor(remPala).toInt()
        val vipala = ((remPala - pala) * 60.0).roundToInt()

        val isAhoratra = totalGhadi >= 60.0

        var endH = t % 24.0
        if (endH < 0) endH += 24.0
        val hr = floor(endH).toInt() % 24
        val mn = floor((endH - floor(endH)) * 60.0).toInt()
        val endTimeStr = String.format(Locale.US, "%02d:%02d", hr, mn)

        val fullTithiName = tithiNames[tithiIndex]
        val pakshaStr = if (tithiIndex < 15) "शुक्ल पक्ष" else "कृष्ण पक्ष"

        return TithiExactResult(
            tithiIndex = tithiIndex,
            tithiName = fullTithiName,
            paksha = pakshaStr,
            ghadi = ghadi,
            pala = pala,
            vipala = vipala,
            totalGhadi = totalGhadi,
            endHourLocal = t,
            endTimeFormatted = endTimeStr,
            isAhoratra = isAhoratra
        )
    }

    fun getTithiValue(adDate: LocalDate, hour: Double, tzOffset: Double = 5.75): Double {
        val pos = AstroCalculator.getPositions(adDate, hour, tzOffset)
        val diff = normalize360(pos.moonLongitude - pos.sunLongitude)
        return diff / 12.0
    }

    fun getKarana(tithiValue: Double): String {
        val karanaIndex = (tithiValue * 2.0).toInt()
        return when (karanaIndex) {
            0 -> "किन्तुघ्न"
            57 -> "शकुनि"
            58 -> "चतुष्पद"
            59 -> "नाग"
            else -> movableKaranas[(karanaIndex - 1) % 7]
        }
    }

    fun findCrossingHour(
        adDate: LocalDate,
        startHour: Double,
        maxSearchHours: Double = 30.0,
        isTithi: Boolean = true,
        tzOffset: Double = 5.75
    ): Double {
        if (isTithi) {
            val exactRes = getExactTithiEnd(adDate, startHour, tzOffset)
            return exactRes.endHourLocal
        }
        var lowH = startHour
        var highH = startHour + maxSearchHours
        
        val startVal = NakshatraYogaCalculator.getNakshatraValue(adDate, lowH, tzOffset)
        val targetIdx = startVal.toInt() + 1.0

        for (i in 0..15) { 
            val midH = (lowH + highH) / 2.0
            val midVal = NakshatraYogaCalculator.getNakshatraValue(adDate, midH, tzOffset)
            
            var diff = midVal - targetIdx
            if (diff < -13.5) diff += 27.0
            if (diff > 13.5) diff -= 27.0

            if (diff < 0) lowH = midH else highH = midH
        }
        return (lowH + highH) / 2.0
    }

    fun findKaranaCrossingHour(adDate: LocalDate, startHour: Double, tzOffset: Double = 5.75): Double {
        var lowH = startHour
        var highH = startHour + 15.0
        
        val startVal = getTithiValue(adDate, lowH, tzOffset) * 2.0
        val targetIdx = startVal.toInt() + 1.0

        for (i in 0..15) { 
            val midH = (lowH + highH) / 2.0
            val midVal = getTithiValue(adDate, midH, tzOffset) * 2.0
            
            var diff = midVal - targetIdx
            if (diff < -30.0) diff += 60.0 
            if (diff > 30.0) diff -= 60.0

            if (diff < 0) lowH = midH else highH = midH
        }
        return (lowH + highH) / 2.0
    }

    fun getShortTithi(adDate: LocalDate, lat: Double, lon: Double, tzOffset: Double): String {
        val (sunriseMins, _) = PanchangaCalculator.getSunriseSunsetMinutes(adDate, lat, lon, tzOffset)
        val tValue = getTithiValue(adDate, sunriseMins / 60.0, tzOffset)
        val idx = tValue.toInt() % 30
        val full = tithiNames[idx]
        return full.replace("शुक्ल ", "").replace("कृष्ण ", "")
    }

    fun getShortTithi(year: Int, month: Int, day: Int): String {
        val adDate = try {
            DateConverterEngine.convertBsToAd(NepaliDate(year, month, day))
        } catch(e: Exception) { LocalDate.now() }
        return getShortTithi(adDate, 27.7172, 85.3240, 5.75)
    }

    fun getTithi(year: Int, month: Int, day: Int): String {
        val adDate = try {
            DateConverterEngine.convertBsToAd(NepaliDate(year, month, day))
        } catch(e: Exception) { LocalDate.now() }
        val (sunriseMins, _) = PanchangaCalculator.getSunriseSunsetMinutes(adDate, 27.7172, 85.3240, 5.75)
        val tValue = getTithiValue(adDate, sunriseMins / 60.0, 5.75)
        return tithiNames[tValue.toInt() % 30]
    }

    val tithiKnowledgeList = listOf(
        TithiKnowledge(1, "प्रतिपदा (परेवा)", "अग्नि", "नन्दा", "आनन्द दिने", "शुभ कार्यका लागि राम्रो", "महिना वा पक्षको पहिलो दिन। यो तिथिकी देवी अग्नि हुन्। शुभ कार्यको सुरुवातका लागि राम्रो मानिन्छ।", true),
        TithiKnowledge(2, "द्वितीया (दुतीया)", "ब्रह्मा", "भद्रा", "कल्याण गर्ने", "यात्रा र व्यापारका लागि राम्रो", "यस तिथिका देवता ब्रह्मा हुन्। नयाँ लुगा लगाउन, यात्रा गर्न वा विवाहका लागि यो तिथि उत्तम मानिन्छ।", true),
        TithiKnowledge(3, "तृतीया (तीज)", "गौरी/शिव", "जया", "विजय गराउने", "मुद्दा मामिला र पराक्रमका काममा राम्रो", "यस तिथिका देवता गौरी/शिव हुन्। विशेष गरी शुक्ल पक्षको तृतीयामा हरितालिका तीज र अक्षय तृतीया जस्ता ठूला पर्व पर्छन्।", true),
        TithiKnowledge(4, "चतुर्थी", "गणेश", "रिक्ता", "खाली हात हुने", "नयाँ/मांगलिक कार्य सुरु गर्न निषेध", "यस तिथिका देवता गणेश हुन्। यो तिथिलाई 'रिक्ता' (खाली) तिथि पनि भनिन्छ। गणेश चतुर्थी जस्ता पर्व यसै तिथिमा पर्छन्।", false),
        TithiKnowledge(5, "पञ्चमी", "नाग", "पूर्णा", "पूर्ण फल दिने", "गृहप्रवेश, विवाह तथा धार्मिक अनुष्ठानका लागि उत्तम", "यस तिथिका देवता नाग हुन्। ज्ञान र बुद्धिका लागि यो तिथि शुभ मानिन्छ। नागपञ्चमी र वसन्त पञ्चमी (सरस्वती पूजा) यसै तिथिमा पर्छन्।", true),
        TithiKnowledge(6, "षष्ठी", "कार्तिकेय (कुमार)", "नन्दा", "आनन्द दिने", "शुभ कार्यका लागि राम्रो", "यस तिथिका देवता कार्तिकेय (कुमार) हुन्। तराई क्षेत्रमा मनाइने प्रसिद्ध छठ पर्व शुक्ल पक्षको षष्ठी तिथिमा मनाइन्छ।", true),
        TithiKnowledge(7, "सप्तमी", "सूर्य", "भद्रा", "कल्याण गर्ने", "यात्रा र व्यापारका लागि राम्रो", "यस तिथिका देवता सूर्य हुन्। आरोग्यता र तेज प्राप्तिका लागि यो तिथि राम्रो मानिन्छ।", true),
        TithiKnowledge(8, "अष्टमी", "रुद्र (शिव/दुर्गा)", "जया", "विजय गराउने", "शक्ति पूजा र पराक्रमका काममा राम्रो", "यस तिथिका देवता रुद्र (शिव/दुर्गा) हुन्। बडादशैंको महाअष्टमी, कृष्ण जन्माष्टमी र गोरखाकाली पूजा जस्ता महत्वपूर्ण शक्ति पूजाका दिनहरू यसै तिथिमा पर्छन्।", true),
        TithiKnowledge(9, "नवमी", "दुर्गा", "रिक्ता", "खाली हात हुने", "नयाँ/मांगलिक कार्य सुरु गर्न निषेध", "यस तिथिकी देवी दुर्गा हुन्। रामनवमी र महानवमी यसै तिथिमा पर्छन्। यो तिथिलाई पनि धार्मिक कार्यका लागि बलियो मानिन्छ।", false),
        TithiKnowledge(10, "दशमी", "यमराज", "पूर्णा", "पूर्ण फल दिने", "गृहप्रवेश, विवाह तथा अनुष्ठानका लागि उत्तम", "यस तिथिका देवता यमराज हुन्। असत्यमाथि सत्यको विजयको प्रतीक विजयादशमी (दशैंको टीका) शुक्ल पक्षको दशमीमा पर्छ।", true),
        TithiKnowledge(11, "एकादशी", "विश्वेदेवा (विष्णु)", "नन्दा", "आनन्द दिने", "व्रत, जप र शुभ कार्यका लागि राम्रो", "यस तिथिका देवता विश्वेदेवा (विष्णु) हुन्। हिन्दू धर्ममा एकादशीको व्रत बस्ने विशेष महत्व छ। वर्षभरि २४ वटा एकादशी पर्छन् (मलमास परेमा २६ वटा)।", true),
        TithiKnowledge(12, "द्वादशी", "विष्णु", "भद्रा", "कल्याण गर्ने", "यात्रा र व्यापारका लागि राम्रो", "यस तिथिका देवता विष्णु हुन्। एकादशी व्रतको पारण (व्रत तोड्ने काम) यसै तिथिमा गरिन्छ। तुलसी रोप्ने र दामोदर पूजा यसैसँग जोडिएको छ।", true),
        TithiKnowledge(13, "त्रयोदशी", "कामदेव/शिव", "जया", "विजय गराउने", "शिव आराधना र प्रदोष व्रतका लागि उत्तम", "यस तिथिका देवता कामदेव/शिव हुन्। प्रत्येक महिनाको कृष्ण र शुक्ल पक्षको त्रयोदशीमा प्रदोष व्रत बसिन्छ, जुन शिव आराधनाका लागि उत्तम मानिन्छ।", true),
        TithiKnowledge(14, "चतुर्दशी", "शिव", "रिक्ता", "खाली हात हुने", "क्रुर/कठिन कार्य बाहेक नयाँ काममा निषेध", "यस तिथिका देवता शिव हुन्। महाशिवरात्रि (कृष्ण पक्ष) र गठेमङ्गल जस्ता पर्व यसै तिथिमा पर्छन्। यसलाई 'क्रुर' तिथि पनि भनिन्छ।", false),
        TithiKnowledge(15, "पूर्णिमा", "चन्द्रमा/सत्यनारायण", "पूर्णा", "पूर्ण फल दिने", "सत्यनारायण पूजा र धार्मिक अनुष्ठानका लागि उत्तम", "शुक्ल पक्षको १५ औं तथा अन्तिम दिन। यस दिन चन्द्रमा पूर्ण रूपमा गोलो र चम्किलो हुन्छ। पूर्णिमाका दिन सत्यनारायणको पूजा र व्रत बस्ने चलन छ। जनैपूर्णिमा, धान्यपूर्णिमा यसैमा पर्छन्।", true),
        TithiKnowledge(30, "औंसी (अमावास्या)", "पितृ", "पूर्णा", "पूर्ण फल दिने", "पितृ कार्य (श्राद्ध, तर्पण) का लागि सर्वोत्तम", "कृष्ण पक्षको १५ औं तथा अन्तिम दिन। यस दिन आकाशमा चन्द्रमा देखिँदैन। यो तिथि पितृ कार्य (श्राद्ध, तर्पण) का लागि सर्वोत्तम मानिन्छ। मातातीर्थ औंसी, कुशे औंसी र लक्ष्मीपूजा (तिहार) यसै तिथिमा पर्छन्।", true)
    )

    fun getTithiKnowledgeForIndex(tithiIndex: Int): TithiKnowledge {
        val tNum = tithiIndex + 1
        return when (tNum) {
            15 -> tithiKnowledgeList.find { it.tithiNumber == 15 }!!
            30 -> tithiKnowledgeList.find { it.tithiNumber == 30 }!!
            else -> {
                val rem = (tithiIndex % 15) + 1
                tithiKnowledgeList.find { it.tithiNumber == rem } ?: tithiKnowledgeList.first()
            }
        }
    }
}

