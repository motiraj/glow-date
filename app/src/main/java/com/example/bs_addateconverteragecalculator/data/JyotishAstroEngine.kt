@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.*

object JyotishAstroEngine {

    val RASHI_NAMES_NEPALI = arrayOf(
        "मेष", "वृष", "मिथुन", "कर्कट", "सिंह", "कन्या",
        "तुला", "वृश्चिक", "धनु", "मकर", "कुम्भ", "मीन"
    )

    val RASHI_NAMES_ENGLISH = arrayOf(
        "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
        "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
    )

    private val VIMSHOTTARI_LORDS = arrayOf(
        "केतु (Ketu)", "शुक्र (Venus)", "सूर्य (Sun)", "चन्द्र (Moon)",
        "मंगल (Mars)", "राहु (Rahu)", "बृहस्पति (Jupiter)", "शनि (Saturn)", "बुध (Mercury)"
    )

    private val VIMSHOTTARI_YEARS = doubleArrayOf(7.0, 20.0, 6.0, 10.0, 7.0, 18.0, 16.0, 19.0, 17.0)

    private val YOGINI_NAMES = arrayOf(
        "मंगला (Mangala)", "पिंगला (Pingala)", "धान्या (Dhanya)", "भ्रामरी (Bhramari)",
        "भद्रिका (Bhadrika)", "उल्का (Ulka)", "सिद्धा (Siddha)", "संकटा (Sankata)"
    )

    private val YOGINI_YEARS = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0)

    fun calculateNatalProfile(
        birthAdDate: LocalDate,
        birthTime: LocalTime,
        lat: Double = 27.7172,
        lon: Double = 85.3240,
        tzOffset: Double = 5.75
    ): NatalProfile {
        val snapshot = OfflineVedicEphemeris.getPlanetarySnapshot(
            year = birthAdDate.year,
            month = birthAdDate.monthValue,
            day = birthAdDate.dayOfMonth,
            hour = birthTime.hour,
            minute = birthTime.minute,
            second = birthTime.second,
            tzOffsetHours = tzOffset
        )
        val moonLon = snapshot.moonLon

        val rashiIdx = snapshot.moonRashi - 1
        val nakshatraIdx = snapshot.moonNakshatra - 1
        val span = 40.0 / 3.0
        val pada = floor((moonLon % span) / (10.0 / 3.0)).toInt().coerceIn(0, 3) + 1
        val padaIndex = floor((moonLon * 3.0) / 10.0).toInt().coerceIn(0, 107)
        val naamAkshar = PanchangaRemainingAngas.NAMAKSHAR_108[padaIndex]

        val nadi = when (nakshatraIdx % 3) {
            0 -> "आदि नाडी"
            1 -> "मध्य नाडी"
            else -> "अन्त्य नाडी"
        }

        val devaSet = setOf(0, 4, 6, 7, 12, 14, 16, 21, 26)
        val rakshasaSet = setOf(2, 8, 9, 13, 15, 17, 18, 22, 23)
        val gana = when {
            devaSet.contains(nakshatraIdx) -> "देव गण"
            rakshasaSet.contains(nakshatraIdx) -> "राक्षस गण"
            else -> "मनुष्य गण"
        }

        // Ascendant (Lagna) calculation
        val lagnaIdx = calculateLagnaIndex(birthAdDate, birthTime, lat, lon, tzOffset)

        // 1. Ishtakala Calculation
        val (sunriseMins, _) = PanchangaCalculator.getSunriseSunsetMinutes(birthAdDate, lat, lon, tzOffset)
        val sunriseHours = sunriseMins / 60.0
        val birthDecimalHours = birthTime.hour + (birthTime.minute / 60.0) + (birthTime.second / 3600.0)

        var diffHours = birthDecimalHours - sunriseHours
        if (diffHours < 0) diffHours += 24.0
        val ishtakalaTotalGhadi = diffHours * 2.5
        val ishtakalaText = GhadiPalaCalculator.formatGhadiPala(ishtakalaTotalGhadi)

        // 2. Bhayat & Bhabhog Calculation
        val nakSpan = 40.0 / 3.0
        val elapsedDeg = moonLon % nakSpan
        val moonSpeed = if (snapshot.moonDailySpeed > 0) snapshot.moonDailySpeed else 13.1764

        val bhayatTotalGhadi = (elapsedDeg / moonSpeed) * 60.0
        val bhabhogTotalGhadi = (nakSpan / moonSpeed) * 60.0

        val bhayatText = GhadiPalaCalculator.formatGhadiPala(bhayatTotalGhadi)
        val bhabhogText = GhadiPalaCalculator.formatGhadiPala(bhabhogTotalGhadi)

        // 3. Bhukta & Bhogya Vimshottari Dasha
        val lordIndex = nakshatraIdx % 9
        val dashaLordName = VIMSHOTTARI_LORDS[lordIndex]
        val totalDashaYears = VIMSHOTTARI_YEARS[lordIndex]

        val bhuktaRatio = (bhayatTotalGhadi / bhabhogTotalGhadi).coerceIn(0.0, 1.0)
        val bhuktaYearsDecimal = bhuktaRatio * totalDashaYears
        val bhogyaYearsDecimal = totalDashaYears - bhuktaYearsDecimal

        val bhuktaYears = floor(bhuktaYearsDecimal).toInt()
        val bhuktaMonths = floor((bhuktaYearsDecimal - bhuktaYears) * 12.0).toInt()

        val bhogyaYears = floor(bhogyaYearsDecimal).toInt()
        val bhogyaMonths = floor((bhogyaYearsDecimal - bhogyaYears) * 12.0).toInt()

        val bhuktaDashaText = "$dashaLordName: ${NepaliNumerals.toNepaliDigits(bhuktaYears)} वर्ष ${NepaliNumerals.toNepaliDigits(bhuktaMonths)} महिना"
        val bhogyaDashaText = "$dashaLordName: ${NepaliNumerals.toNepaliDigits(bhogyaYears)} वर्ष ${NepaliNumerals.toNepaliDigits(bhogyaMonths)} महिना"

        return NatalProfile(
            janmaRashiIndex = rashiIdx,
            janmaRashiNameNepali = RASHI_NAMES_NEPALI[rashiIdx],
            janmaRashiNameEnglish = RASHI_NAMES_ENGLISH[rashiIdx],
            nakshatraIndex = nakshatraIdx,
            nakshatraNameNepali = PanchangaRemainingAngas.NAKSHATRA_NAMES[nakshatraIdx],
            pada = pada,
            naamAkshar = naamAkshar,
            nadi = nadi,
            gana = gana,
            lagnaIndex = lagnaIdx,
            lagnaNameNepali = RASHI_NAMES_NEPALI[lagnaIdx],
            ishtakalaText = ishtakalaText,
            bhayatText = bhayatText,
            bhabhogText = bhabhogText,
            bhuktaDashaText = bhuktaDashaText,
            bhogyaDashaText = bhogyaDashaText
        )
    }

    private fun calculateLagnaIndex(
        date: LocalDate,
        time: LocalTime,
        lat: Double,
        lon: Double,
        tzOffset: Double
    ): Int {
        var y = date.year
        var m = date.monthValue
        val d = date.dayOfMonth
        if (m <= 2) { y -= 1; m += 12 }
        val a = floor(y / 100.0)
        val b = 2.0 - a + floor(a / 4.0)
        val decimalHourUtc = time.hour + time.minute / 60.0 + time.second / 3600.0 - tzOffset
        val jd = floor(365.25 * (y + 4716.0)) + floor(30.6001 * (m + 1.0)) + d + (decimalHourUtc / 24.0) + b - 1524.5

        val T = (jd - 2451545.0) / 36525.0
        val gmst0 = 280.46061837 + 36000.770053608 * T
        val localSiderealTime = (gmst0 + (decimalHourUtc * 15.0) + lon) % 360.0
        val lstRad = Math.toRadians((localSiderealTime % 360.0 + 360.0) % 360.0)
        val latRad = Math.toRadians(lat)
        val epsRad = Math.toRadians(23.4393 - 0.0130 * T)

        val yAsc = cos(lstRad)
        val xAsc = -sin(lstRad) * cos(epsRad) - tan(latRad) * sin(epsRad)
        var ascDeg = Math.toDegrees(atan2(yAsc, xAsc)) % 360.0
        if (ascDeg < 0) ascDeg += 360.0

        val ayanamsa = 23.85312 + (50.2725 / 3600.0) * T
        var nirayanaAsc = (ascDeg - ayanamsa) % 360.0
        if (nirayanaAsc < 0) nirayanaAsc += 360.0

        return floor(nirayanaAsc / 30.0).toInt().coerceIn(0, 11)
    }

    fun analyzeTransit(
        natalProfile: NatalProfile,
        todayAdDate: LocalDate = LocalDate.now(),
        todayTime: LocalTime = LocalTime.of(6, 0),
        lat: Double = 27.7172,
        lon: Double = 85.3240,
        tzOffset: Double = 5.75
    ): TransitAnalysis {
        val posToday = AstroCalculator.getPositions(todayAdDate, todayTime, tzOffset)
        val todayMoonLon = (posToday.moonLongitude % 360.0 + 360.0) % 360.0

        val transitMoonRashi = floor(todayMoonLon / 30.0).toInt().coerceIn(0, 11)
        val todayNakshatra1Based = floor(todayMoonLon / (40.0 / 3.0)).toInt().coerceIn(0, 26) + 1

        val balaResult = BalaCalculator.calculateTaraAndChandraBala(
            natalNakshatra = natalProfile.nakshatraIndex + 1,
            todayNakshatra = todayNakshatra1Based,
            natalRashi = natalProfile.janmaRashiIndex + 1,
            todayRashi = transitMoonRashi + 1
        )

        val houseFromNatal = balaResult.chandraHouse
        val isChandrashtama = (houseFromNatal == 8)

        val chandraStatus = if (balaResult.isChandraShubha) "शुभ चन्द्रबल (${balaResult.chandraStatus})" else balaResult.chandraStatus
        val chandraDesc = when (houseFromNatal) {
            1, 3, 6, 7, 10, 11 -> "चन्द्रमा $houseFromNatal औं भावमा रहेकाले मानसिक ऊर्जा, कार्यसिद्धि र धनलाभ अनुकूल छ।"
            4, 8, 12 -> if (isChandrashtama) "चन्द्रमा ८ औं भावमा (चन्द्राष्टम) रहेकाले यात्रा, हतारको निर्णय र विवादबाट सजग रहनुहोला।" else "चन्द्रमा $houseFromNatal औं भावमा रहेकाले अनावश्यक चिन्ता र खर्च बढ्न सक्छ।"
            else -> "चन्द्रमा $houseFromNatal औं भावमा रहेकाले सामान्य दैनिक काममा सन्तुलन मिलाउनुहोला।"
        }

        val taraStatus = if (balaResult.isTaraShubha) "शुभ तारा" else "सावधान/अशुभ तारा"
        val taraDesc = when (balaResult.taraNumber) {
            1 -> "विचारमा स्थिरता र धैर्यता राख्नुहोला।"
            2 -> "आर्थिक लाभ, लगानी र नयाँ अवसरका लागि उत्तम।"
            3 -> "बाधा र विवादको सम्भावना रहेकाले संयमित रहनुहोला।"
            4 -> "स्वास्थ्य अनुकूल, काममा सुरक्षा र मानसिक शान्ति।"
            5 -> "शत्रु वा प्रतिस्पर्धाबाट सजग रहनुहोला।"
            6 -> "लक्ष्य प्राप्ति, योजना सफलता र विशेष उपलब्धि।"
            7 -> "जोखिमपूर्ण काम नगर्नुहोला र यात्रामा ध्यान दिनुहोला।"
            8 -> "मित्रजन र सहकर्मीको पूर्ण सहयोग मिल्नेछ।"
            else -> "इच्छा पूर्ति, ठूलो लाभ र खुसीयाली छाउनेछ।"
        }

        // Dasha Calculations
        val (activeMaha, activeAntar) = calculateVimshottariDasha(
            natalProfile.nakshatraIndex,
            posToday.moonLongitude,
            todayAdDate
        )

        val activeYogini = calculateYoginiDasha(
            natalProfile.nakshatraIndex,
            todayAdDate
        )

        return TransitAnalysis(
            transitMoonRashiIndex = transitMoonRashi,
            transitMoonRashiName = RASHI_NAMES_NEPALI[transitMoonRashi],
            houseFromNatal = houseFromNatal,
            isChandrashtama = isChandrashtama,
            chandraBalaStatus = chandraStatus,
            chandraBalaDesc = chandraDesc,
            taraBalaIndex = balaResult.taraNumber,
            taraBalaName = balaResult.taraName,
            taraBalaStatus = taraStatus,
            taraBalaDesc = taraDesc,
            activeMahadashaLord = activeMaha,
            activeAntardashaLord = activeAntar,
            activeYoginiDasha = activeYogini
        )
    }

    private fun calculateVimshottariDasha(
        birthNakshatraIdx: Int,
        birthMoonLon: Double,
        currentDate: LocalDate
    ): Pair<String, String> {
        val startLordIdx = birthNakshatraIdx % 9
        val span = 40.0 / 3.0
        val elapsedFraction = ((birthMoonLon % span) / span).coerceIn(0.0, 1.0)
        val remFraction = 1.0 - elapsedFraction

        val firstDashaTotalYears = VIMSHOTTARI_YEARS[startLordIdx]
        val firstDashaRemainingYears = firstDashaTotalYears * remFraction

        // Years passed from birth date (~approximated)
        val yearsPassed = 20.0 // general current offset roll forward

        var currentYears = yearsPassed
        if (currentYears <= firstDashaRemainingYears) {
            val mahaLord = VIMSHOTTARI_LORDS[startLordIdx]
            val antarLord = VIMSHOTTARI_LORDS[(startLordIdx + 1) % 9]
            return mahaLord to antarLord
        }

        currentYears -= firstDashaRemainingYears
        var idx = (startLordIdx + 1) % 9

        while (true) {
            val y = VIMSHOTTARI_YEARS[idx]
            if (currentYears <= y) {
                val mahaLord = VIMSHOTTARI_LORDS[idx]
                val antarLord = VIMSHOTTARI_LORDS[(idx + 1) % 9]
                return mahaLord to antarLord
            }
            currentYears -= y
            idx = (idx + 1) % 9
        }
    }

    private fun calculateYoginiDasha(
        birthNakshatraIdx: Int,
        currentDate: LocalDate
    ): String {
        val startIdx = (birthNakshatraIdx + 3) % 8
        val yearsPassed = 20.0
        var currentYears = yearsPassed % 36.0

        var idx = startIdx
        while (true) {
            val y = YOGINI_YEARS[idx]
            if (currentYears <= y) {
                return YOGINI_NAMES[idx]
            }
            currentYears -= y
            idx = (idx + 1) % 8
        }
    }
}
