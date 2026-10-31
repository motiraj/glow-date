@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.*

/**
 * Information about a single planet in the Cheena Patrika
 */
data class CheenaPlanetaryPosition(
    val id: String,
    val nameDevanagari: String,
    val shortSymbol: String,
    val rashiIndex: Int,          // 1..12 (1 = मेष .. 12 = मीन)
    val rashiName: String,
    val degreesInRashi: Double,    // 0.0 .. 29.999
    val formattedDegrees: String,  // eg. १२° ३४' ५६"
    val navamshaRashiIndex: Int,  // 1..12
    val navamshaRashiName: String,
    val nakshatraIndex: Int,       // 1..27
    val nakshatraName: String,
    val pada: Int,                 // 1..4
    val dignityText: String,       // उच्च, नीच, स्वगृह, मूलत्रिकोण, मित्र, सम, शत्रु
    val isRetrograde: Boolean,     // वक्री
    val isCombust: Boolean,        // अस्त
    val longitude: Double
)

/**
 * Single Mahadasha in the 120-year Vimshottari cycle
 */
data class VimshottariDashaPeriod(
    val lordName: String,
    val lordSymbol: String,
    val durationYears: Double,
    val startAgeYears: Double,
    val endAgeYears: Double,
    val startAdDate: LocalDate,
    val endAdDate: LocalDate,
    val startBsDateText: String,
    val endBsDateText: String,
    val isCurrentlyActive: Boolean
)

/**
 * Auspicious stones, numbers, colors, mantras based on birth profile
 */
data class CheenaAuspiciousAttributes(
    val luckyStone: String,
    val luckyStoneNepali: String,
    val luckyRudraksha: String,
    val luckyDays: String,
    val luckyColor: String,
    val luckyNumber: String,
    val luckyDirection: String,
    val ishtaDevata: String,
    val grahaMantra: String,
    val gayatriMantra: String
)

/**
 * Detailed life prediction and analysis for an astrological topic
 */
data class LifePredictionTopic(
    val title: String,
    val englishTitle: String,
    val iconEmoji: String,
    val primaryHouseInfo: String,
    val analysisSummary: String,
    val favorableStrengths: List<String>,
    val precautionsAndChallenges: List<String>,
    val auspiciousGuidance: String
)

/**
 * Dedicated Vedic Remedies for Education, Health, Wealth & Bhagyodaya
 */
data class LifeRemediesTopic(
    val title: String,
    val educationRemedies: List<String>,
    val healthRemedies: List<String>,
    val wealthRemedies: List<String>,
    val fortuneRemedies: List<String>,
    val dailyVedicRituals: List<String>
)

/**
 * Complete set of Life Predictions across all life spheres
 */
data class CheenaLifePredictions(
    val personalityTraits: LifePredictionTopic,       // व्यक्तिगत विशेषता
    val wealthAndFinances: LifePredictionTopic,       // धन र सम्पत्ति
    val educationAndLearning: LifePredictionTopic,    // शिक्षा र विद्या
    val careerAndProfession: LifePredictionTopic,     // रोजगार तथा करियर
    val familyAndHome: LifePredictionTopic,           // परिवार र पारिवारिक सुख
    val healthAndLongevity: LifePredictionTopic,      // स्वास्थ्य र आरोग्यता
    val marriageAndLove: LifePredictionTopic,         // विवाह तथा दाम्पत्य जीवन
    val childrenAndProgeny: LifePredictionTopic,      // सन्तान र छोराछोरीसँगको सम्बन्ध
    val parentsRelationship: LifePredictionTopic,     // आमा बाबुसँगको सम्बन्ध
    val comprehensiveRemedies: LifeRemediesTopic      // शिक्षा, स्वास्थ्य, धन तथा भाग्योदयका उपायहरू
)

/**
 * Complete Cheena Patrika / Horoscope Data
 */
data class CheenaPatrikaData(
    // 1. Native Information
    val name: String,
    val genderText: String,
    val bsBirthDateText: String,
    val adBirthDateText: String,
    val birthTimeText: String,
    val birthTimeStringNepali: String,
    val birthPlaceText: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneOffset: Double,

    // 2. Birth Panchanga Details
    val vikramSambatText: String,
    val nepalSambatText: String,
    val shakaSambatText: String,
    val rituName: String,
    val ayanName: String,
    val maasName: String,
    val pakshaName: String,
    val tithiName: String,
    val vaarName: String,
    val vaarLord: String,
    val nakshatraName: String,
    val pada: Int,
    val yogaName: String,
    val karanaName: String,
    val sunriseText: String,
    val sunsetText: String,
    val ayanamsaDegreesText: String,

    // 3. Vedic Astrological Essentials (अष्टकूट / जन्म अङ्ग)
    val janmaRashiIndex: Int,     // 1..12
    val janmaRashiName: String,
    val rashiLord: String,
    val lagnaIndex: Int,          // 1..12
    val lagnaName: String,
    val lagnaLord: String,
    val lagnaDegreeFormatted: String,
    val navamshaLagnaIndex: Int,  // 1..12
    val navamshaLagnaName: String,
    val naamAkshar: String,
    val nakshatraLord: String,
    val varna: String,
    val vashya: String,
    val yoni: String,
    val gana: String,
    val nadi: String,

    // 4. Time Measures (इष्टकाल / भयात / भभोग)
    val ishtakalaGhadiPalaText: String,
    val bhayatGhadiPalaText: String,
    val bhabhogGhadiPalaText: String,
    val bhuktaDashaText: String,
    val bhogyaDashaText: String,

    // 5. Planetary Placements & Snapshot
    val snapshot: PlanetarySnapshot,
    val ascendantLongitude: Double,
    val planetaryPositions: List<CheenaPlanetaryPosition>,

    // 6. Lifespan Vimshottari Mahadasha Table (120 Years)
    val vimshottariDashas: List<VimshottariDashaPeriod>,

    // 7. Auspicious Attributes & Remedies
    val remedies: CheenaAuspiciousAttributes,

    // 8. Personality & Vedic Nature Summary
    val lagnaNatureDescription: String,
    val rashiNatureDescription: String,

    // 9. Comprehensive Life Predictions & Analysis
    val lifePredictions: CheenaLifePredictions
)

object CheenaCalculatorEngine {

    private val VIMSHOTTARI_LORDS = arrayOf(
        "केतु (Ketu)", "शुक्र (Venus)", "सूर्य (Sun)", "चन्द्र (Moon)",
        "मंगल (Mars)", "राहु (Rahu)", "बृहस्पति (Jupiter)", "शनि (Saturn)", "बुध (Mercury)"
    )

    private val VIMSHOTTARI_SYMBOLS = arrayOf("के", "शु", "सू", "च", "मं", "रा", "गु", "श", "बु")
    private val VIMSHOTTARI_YEARS = doubleArrayOf(7.0, 20.0, 6.0, 10.0, 7.0, 18.0, 16.0, 19.0, 17.0)

    private val RASHI_LORDS = arrayOf(
        "मंगल (Mars)", "शुक्र (Venus)", "बुध (Mercury)", "चन्द्र (Moon)",
        "सूर्य (Sun)", "बुध (Mercury)", "शुक्र (Venus)", "मंगल (Mars)",
        "बृहस्पति (Jupiter)", "शनि (Saturn)", "शनि (Saturn)", "बृहस्पति (Jupiter)"
    )

    private val RITU_NAMES = arrayOf("वसन्त", "ग्रीष्म", "वर्षा", "शरद", "हेमन्त", "शिशिर")

    /**
     * Calculates the complete Cheena Patrika data for a given birth profile
     */
    fun calculateCheenaPatrika(profile: BirthProfile): CheenaPatrikaData {
        val birthAdDate = try {
            LocalDate.parse(profile.adBirthDate)
        } catch (_: Exception) {
            LocalDate.of(2001, 4, 14)
        }

        val birthBsDate = try {
            DateConverterEngine.convertAdToBs(birthAdDate)
        } catch (_: Exception) {
            NepaliDate(profile.bsYear, profile.bsMonth, profile.bsDay)
        }

        val birthTime = LocalTime.of(profile.birthHour, profile.birthMinute)

        // 1. Planetary Snapshot from High Precision Ephemeris
        val snapshot = OfflineVedicEphemeris.getPlanetarySnapshot(
            year = birthAdDate.year,
            month = birthAdDate.monthValue,
            day = birthAdDate.dayOfMonth,
            hour = birthTime.hour,
            minute = birthTime.minute,
            second = 0,
            tzOffsetHours = profile.timezoneOffset
        )

        // 2. Ascendant (Lagna) Longitude
        val ascLonDeg = calculateNirayanaAscendantDeg(
            date = birthAdDate,
            time = birthTime,
            lat = profile.latitude,
            lon = profile.longitude,
            tzOffset = profile.timezoneOffset
        )
        val lagnaRashiIndex = (floor(ascLonDeg / 30.0).toInt() % 12) + 1
        val lagnaRashiName = JyotishAstroEngine.RASHI_NAMES_NEPALI[lagnaRashiIndex - 1]
        val lagnaLord = RASHI_LORDS[lagnaRashiIndex - 1]
        val lagnaRak = OfflineVedicEphemeris.toRashiAmshaKala(ascLonDeg)
        val lagnaDegFormatted = "${NepaliNumerals.toNepaliDigits(lagnaRak.degrees)}° ${NepaliNumerals.toNepaliDigits(String.format(java.util.Locale.US, "%02d", lagnaRak.arcMinutes))}' ${NepaliNumerals.toNepaliDigits(String.format(java.util.Locale.US, "%02d", lagnaRak.arcSeconds))}\""

        // Navamsha Lagna
        val navamshaLagnaIndex = (floor(ascLonDeg * 0.3).toInt() % 12) + 1
        val navamshaLagnaName = JyotishAstroEngine.RASHI_NAMES_NEPALI[navamshaLagnaIndex - 1]

        // 3. Moon & Nakshatra calculations
        val moonLon = snapshot.moonLon
        val janmaRashiIndex = snapshot.moonRashi
        val janmaRashiName = JyotishAstroEngine.RASHI_NAMES_NEPALI[janmaRashiIndex - 1]
        val rashiLord = RASHI_LORDS[janmaRashiIndex - 1]

        val nakshatraIndex = snapshot.moonNakshatra
        val nakshatraName = PanchangaRemainingAngas.NAKSHATRA_NAMES[nakshatraIndex - 1]
        val span = 40.0 / 3.0
        val pada = floor((moonLon % span) / (10.0 / 3.0)).toInt().coerceIn(0, 3) + 1
        val padaIndex = floor((moonLon * 3.0) / 10.0).toInt().coerceIn(0, 107)
        val naamAkshar = PanchangaRemainingAngas.NAMAKSHAR_108[padaIndex]
        val nakLordIndex = (nakshatraIndex - 1) % 9
        val nakshatraLord = VIMSHOTTARI_LORDS[nakLordIndex]

        // 4. Astakoota / Birth Essentials
        val varna = when (janmaRashiIndex) {
            4, 8, 12 -> "ब्राह्मण वर्ण"
            1, 5, 9 -> "क्षत्रिय वर्ण"
            2, 6, 10 -> "वैश्य वर्ण"
            else -> "शूद्र वर्ण"
        }

        val vashya = when (janmaRashiIndex) {
            1, 2 -> "चतुष्पाद"
            3, 6, 7, 11 -> "मानव / द्विपद"
            4, 12 -> "जलचर"
            8 -> "कीटक"
            5 -> "वनचर (सिंह)"
            9 -> "द्विपद पूर्वार्ध / चतुष्पाद उत्तरार्ध"
            else -> "चतुष्पाद पूर्वार्ध / जलचर उत्तरार्ध"
        }

        val yoni = when (nakshatraIndex - 1) {
            0, 23 -> "अश्व योनि (Horse)"
            1, 26 -> "गज योनि (Elephant)"
            2, 7 -> "मेष योनि (Ram/Sheep)"
            3, 4 -> "सर्प योनि (Serpent)"
            5, 18 -> "श्वान योनि (Dog)"
            6, 8 -> "मार्जार योनि (Cat)"
            9, 10 -> "मूषक योनि (Rat)"
            11, 25 -> "गौ योनि (Cow)"
            12, 14 -> "महिष योनि (Buffalo)"
            13, 15 -> "व्याघ्र योनि (Tiger)"
            16, 17 -> "मृग योनि (Deer)"
            19, 21 -> "वानर योनि (Monkey)"
            20 -> "नकुल योनि (Mongoose)"
            else -> "सिंह योनि (Lion)"
        }

        val devaSet = setOf(0, 4, 6, 7, 12, 14, 16, 21, 26)
        val rakshasaSet = setOf(2, 8, 9, 13, 15, 17, 18, 22, 23)
        val gana = when {
            devaSet.contains(nakshatraIndex - 1) -> "देव गण"
            rakshasaSet.contains(nakshatraIndex - 1) -> "राक्षस गण"
            else -> "मनुष्य गण"
        }

        val nadi = when ((nakshatraIndex - 1) % 3) {
            0 -> "आदि नाडी"
            1 -> "मध्य नाडी"
            else -> "अन्त्य नाडी"
        }

        // 5. Sunrise, Sunset and Ishtakala
        val (sunriseMins, sunsetMins) = PanchangaCalculator.getSunriseSunsetMinutes(
            birthAdDate, profile.latitude, profile.longitude, profile.timezoneOffset
        )
        val sunriseHours = sunriseMins / 60.0
        val birthDecimalHours = birthTime.hour + (birthTime.minute / 60.0)

        var diffHours = birthDecimalHours - sunriseHours
        if (diffHours < 0) diffHours += 24.0
        val ishtakalaTotalGhadi = diffHours * 2.5
        val ishtakalaText = GhadiPalaCalculator.formatGhadiPala(ishtakalaTotalGhadi)

        val elapsedDeg = moonLon % span
        val moonSpeed = if (snapshot.moonDailySpeed > 0) snapshot.moonDailySpeed else 13.1764
        val bhayatTotalGhadi = (elapsedDeg / moonSpeed) * 60.0
        val bhabhogTotalGhadi = (span / moonSpeed) * 60.0
        val bhayatText = GhadiPalaCalculator.formatGhadiPala(bhayatTotalGhadi)
        val bhabhogText = GhadiPalaCalculator.formatGhadiPala(bhabhogTotalGhadi)

        val totalDashaYears = VIMSHOTTARI_YEARS[nakLordIndex]
        val bhuktaRatio = (bhayatTotalGhadi / bhabhogTotalGhadi).coerceIn(0.0, 1.0)
        val bhuktaYearsDecimal = bhuktaRatio * totalDashaYears
        val bhogyaYearsDecimal = totalDashaYears - bhuktaYearsDecimal

        val bhuktaY = floor(bhuktaYearsDecimal).toInt()
        val bhuktaM = floor((bhuktaYearsDecimal - bhuktaY) * 12.0).toInt()
        val bhogyaY = floor(bhogyaYearsDecimal).toInt()
        val bhogyaM = floor((bhogyaYearsDecimal - bhogyaY) * 12.0).toInt()

        val bhuktaDashaText = "$nakshatraLord: ${NepaliNumerals.toNepaliDigits(bhuktaY)} वर्ष ${NepaliNumerals.toNepaliDigits(bhuktaM)} महिना"
        val bhogyaDashaText = "$nakshatraLord: ${NepaliNumerals.toNepaliDigits(bhogyaY)} वर्ष ${NepaliNumerals.toNepaliDigits(bhogyaM)} महिना"

        // 6. Panchanga elements at birth
        val tithiResult = PanchangaCalculator.calculateTithiAndGhadiPala(
            snapshot.sunLon, snapshot.moonLon, snapshot.sunDailySpeed, snapshot.moonDailySpeed, sunriseHours
        )
        val yogaIndex = floor(((snapshot.sunLon + snapshot.moonLon) % 360.0) / span).toInt().coerceIn(0, 26)
        val yogaName = PanchangaRemainingAngas.YOGA_NAMES[yogaIndex]

        val karanaIndex = floor(((snapshot.moonLon - snapshot.sunLon + 360.0) % 360.0) / 6.0).toInt().coerceIn(0, 59)
        val karanaName = when {
            karanaIndex == 0 -> "किंस्तुघ्न (स्थिर)"
            karanaIndex in 1..56 -> PanchangaRemainingAngas.CHARA_KARANA_NAMES[(karanaIndex - 1) % 7]
            karanaIndex == 57 -> "शकुनि (स्थिर)"
            karanaIndex == 58 -> "चतुष्पाद (स्थिर)"
            else -> "नाग (स्थिर)"
        }

        val dayOfWeekIndex = birthAdDate.dayOfWeek.value % 7
        val vaarName = NepaliDays.getDayName(dayOfWeekIndex, true)
        val vaarLord = when (dayOfWeekIndex) {
            0 -> "सूर्य (Sun)"
            1 -> "चन्द्र (Moon)"
            2 -> "मंगल (Mars)"
            3 -> "बुध (Mercury)"
            4 -> "बृहस्पति (Jupiter)"
            5 -> "शुक्र (Venus)"
            else -> "शनि (Saturn)"
        }

        val sunLonNorm = (snapshot.sunLon % 360.0 + 360.0) % 360.0
        val ayanName = if (sunLonNorm in 270.0..360.0 || sunLonNorm in 0.0..90.0) "उत्तरायण" else "दक्षिणायन"
        val rituIndex = floor((sunLonNorm + 30.0) % 360.0 / 60.0).toInt().coerceIn(0, 5)
        val rituName = RITU_NAMES[rituIndex]
        val maasName = NepaliMonths.getMonthName(birthBsDate.month, true)

        val nepalSambatText = NepalSambatCalculator.getNepalSambatDetails(
            birthBsDate.year, birthBsDate.month, birthBsDate.day, tithiResult.paksha, tithiResult.tithiName
        )
        val shakaSambatText = "${NepaliNumerals.toNepaliDigits(birthAdDate.year - 78)} शक संवत्"
        val vikramSambatText = "${NepaliNumerals.toNepaliDigits(birthBsDate.year)} वि.सं."

        val srH = sunriseMins / 60
        val srM = sunriseMins % 60
        val ssH = sunsetMins / 60
        val ssM = sunsetMins % 60
        val sunriseText = "बिहान ${NepaliNumerals.toNepaliDigits(srH)}:${NepaliNumerals.toNepaliDigits(String.format(java.util.Locale.US, "%02d", srM))}"
        val sunsetText = "साँझ ${NepaliNumerals.toNepaliDigits(if (ssH > 12) ssH - 12 else ssH)}:${NepaliNumerals.toNepaliDigits(String.format(java.util.Locale.US, "%02d", ssM))}"

        val ayanRak = OfflineVedicEphemeris.toRashiAmshaKala(snapshot.ayanamsaDegrees)
        val ayanamsaText = "${NepaliNumerals.toNepaliDigits(ayanRak.degrees)}° ${NepaliNumerals.toNepaliDigits(String.format(java.util.Locale.US, "%02d", ayanRak.arcMinutes))}' ${NepaliNumerals.toNepaliDigits(String.format(java.util.Locale.US, "%02d", ayanRak.arcSeconds))}\" (लाहिरी)"

        // 7. Planetary Positions List (Lagna + 9 Grahas)
        val planetaryList = mutableListOf<CheenaPlanetaryPosition>()

        // Lagna Entry
        planetaryList.add(
            createPlanetaryPosition(
                id = "lagna",
                nameDevanagari = "लग्न (Lagna)",
                shortSymbol = "ल",
                lon = ascLonDeg,
                isRetrograde = false,
                isCombust = false
            )
        )

        // 9 Vedic Grahas
        planetaryList.add(createPlanetaryPosition("sun", "सूर्य (Sun)", "सू", snapshot.sunLon, false, false))
        planetaryList.add(createPlanetaryPosition("moon", "चन्द्र (Moon)", "च", snapshot.moonLon, false, false))
        planetaryList.add(createPlanetaryPosition("mars", "मंगल (Mars)", "मं", snapshot.marsLon, snapshot.isMarsRetrograde, snapshot.isMarsCombust))
        planetaryList.add(createPlanetaryPosition("mercury", "बुध (Mercury)", "बु", snapshot.mercuryLon, snapshot.isMercuryRetrograde, snapshot.isMercuryCombust))
        planetaryList.add(createPlanetaryPosition("jupiter", "बृहस्पति (Jupiter)", "गु", snapshot.jupiterLon, snapshot.isJupiterRetrograde, snapshot.isJupiterCombust))
        planetaryList.add(createPlanetaryPosition("venus", "शुक्र (Venus)", "शु", snapshot.venusLon, snapshot.isVenusRetrograde, snapshot.isVenusCombust))
        planetaryList.add(createPlanetaryPosition("saturn", "शनि (Saturn)", "श", snapshot.saturnLon, snapshot.isSaturnRetrograde, snapshot.isSaturnCombust))
        planetaryList.add(createPlanetaryPosition("rahu", "राहु (Rahu)", "रा", snapshot.rahuLon, true, false))
        planetaryList.add(createPlanetaryPosition("ketu", "केतु (Ketu)", "के", snapshot.ketuLon, true, false))

        // 8. 120-Year Vimshottari Mahadasha Timeline
        val dashaList = generateVimshottariTimeline(
            birthAdDate = birthAdDate,
            nakLordIndex = nakLordIndex,
            bhogyaYearsDecimal = bhogyaYearsDecimal
        )

        // 9. Auspicious Attributes & Remedies
        // 9. Auspicious Attributes & Remedies
        val remedies = generateRemedies(lagnaRashiIndex, janmaRashiIndex)

        // 10. Lagna & Moon Rashi Nature Summary
        val lagnaNature = getLagnaNature(lagnaRashiIndex)
        val rashiNature = getRashiNature(janmaRashiIndex)

        // 11. Comprehensive Life Predictions
        val lifePredictions = generateLifePredictions(
            lagnaIndex = lagnaRashiIndex,
            janmaRashiIndex = janmaRashiIndex,
            planetaryPositions = planetaryList
        )

        val genderText = when (profile.gender) {
            UserGender.MALE -> "पुरुष"
            UserGender.FEMALE -> "महिला"
            else -> "उल्लेख नभएको"
        }

        val bsDateStr = "${NepaliNumerals.toNepaliDigits(birthBsDate.year)} ${NepaliMonths.getMonthName(birthBsDate.month, true)} ${NepaliNumerals.toNepaliDigits(birthBsDate.day)} गते"
        val adDateStr = birthAdDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy MMMM dd", java.util.Locale.ENGLISH))

        val periodNe = PanchangaCalculator.getTimePeriodName(birthTime.hour)
        val displayH = if (birthTime.hour == 0) 12 else if (birthTime.hour > 12) birthTime.hour - 12 else birthTime.hour
        val birthTimeStringNe = "$periodNe ${NepaliNumerals.toNepaliDigits(displayH)}:${NepaliNumerals.toNepaliDigits(String.format(java.util.Locale.US, "%02d", birthTime.minute))} बजे"

        return CheenaPatrikaData(
            name = profile.name.ifEmpty { "जातक" },
            genderText = genderText,
            bsBirthDateText = bsDateStr,
            adBirthDateText = adDateStr,
            birthTimeText = String.format(java.util.Locale.US, "%02d:%02d", birthTime.hour, birthTime.minute),
            birthTimeStringNepali = birthTimeStringNe,
            birthPlaceText = profile.locationName,
            latitude = profile.latitude,
            longitude = profile.longitude,
            timezoneOffset = profile.timezoneOffset,
            vikramSambatText = vikramSambatText,
            nepalSambatText = nepalSambatText,
            shakaSambatText = shakaSambatText,
            rituName = rituName,
            ayanName = ayanName,
            maasName = maasName,
            pakshaName = "${tithiResult.paksha} पक्ष",
            tithiName = tithiResult.tithiName,
            vaarName = vaarName,
            vaarLord = vaarLord,
            nakshatraName = nakshatraName,
            pada = pada,
            yogaName = yogaName,
            karanaName = karanaName,
            sunriseText = sunriseText,
            sunsetText = sunsetText,
            ayanamsaDegreesText = ayanamsaText,
            janmaRashiIndex = janmaRashiIndex,
            janmaRashiName = janmaRashiName,
            rashiLord = rashiLord,
            lagnaIndex = lagnaRashiIndex,
            lagnaName = lagnaRashiName,
            lagnaLord = lagnaLord,
            lagnaDegreeFormatted = lagnaDegFormatted,
            navamshaLagnaIndex = navamshaLagnaIndex,
            navamshaLagnaName = navamshaLagnaName,
            naamAkshar = naamAkshar,
            nakshatraLord = nakshatraLord,
            varna = varna,
            vashya = vashya,
            yoni = yoni,
            gana = gana,
            nadi = nadi,
            ishtakalaGhadiPalaText = ishtakalaText,
            bhayatGhadiPalaText = bhayatText,
            bhabhogGhadiPalaText = bhabhogText,
            bhuktaDashaText = bhuktaDashaText,
            bhogyaDashaText = bhogyaDashaText,
            snapshot = snapshot,
            ascendantLongitude = ascLonDeg,
            planetaryPositions = planetaryList,
            vimshottariDashas = dashaList,
            remedies = remedies,
            lagnaNatureDescription = lagnaNature,
            rashiNatureDescription = rashiNature,
            lifePredictions = lifePredictions
        )
    }

    private fun createPlanetaryPosition(
        id: String,
        nameDevanagari: String,
        shortSymbol: String,
        lon: Double,
        isRetrograde: Boolean,
        isCombust: Boolean
    ): CheenaPlanetaryPosition {
        val normLon = (lon % 360.0 + 360.0) % 360.0
        val rashiIndex = (floor(normLon / 30.0).toInt() % 12) + 1
        val rashiName = JyotishAstroEngine.RASHI_NAMES_NEPALI[rashiIndex - 1]
        val degInRashi = normLon % 30.0
        val rak = OfflineVedicEphemeris.toRashiAmshaKala(normLon)
        val formattedDeg = "${NepaliNumerals.toNepaliDigits(rak.degrees)}° ${NepaliNumerals.toNepaliDigits(String.format(java.util.Locale.US, "%02d", rak.arcMinutes))}'"

        val navamshaRashiIndex = (floor(normLon * 0.3).toInt() % 12) + 1
        val navamshaRashiName = JyotishAstroEngine.RASHI_NAMES_NEPALI[navamshaRashiIndex - 1]

        val nakIdx = (floor(normLon / (40.0 / 3.0)).toInt() % 27) + 1
        val nakName = PanchangaRemainingAngas.NAKSHATRA_NAMES[nakIdx - 1]
        val p = floor((normLon % (40.0 / 3.0)) / (10.0 / 3.0)).toInt().coerceIn(0, 3) + 1

        val dignity = getDignityText(id, rashiIndex)

        return CheenaPlanetaryPosition(
            id = id,
            nameDevanagari = nameDevanagari,
            shortSymbol = shortSymbol,
            rashiIndex = rashiIndex,
            rashiName = rashiName,
            degreesInRashi = degInRashi,
            formattedDegrees = formattedDeg,
            navamshaRashiIndex = navamshaRashiIndex,
            navamshaRashiName = navamshaRashiName,
            nakshatraIndex = nakIdx,
            nakshatraName = nakName,
            pada = p,
            dignityText = dignity,
            isRetrograde = isRetrograde,
            isCombust = isCombust,
            longitude = normLon
        )
    }

    private fun getDignityText(planetId: String, rashiIndex: Int): String {
        return when (planetId) {
            "sun" -> when (rashiIndex) {
                1 -> "उच्च (Exalted)"
                7 -> "नीच (Debilitated)"
                5 -> "स्वगृह (Own House)"
                else -> "सम/मित्र"
            }
            "moon" -> when (rashiIndex) {
                2 -> "उच्च (Exalted)"
                8 -> "नीच (Debilitated)"
                4 -> "स्वगृह (Own House)"
                else -> "सम/मित्र"
            }
            "mars" -> when (rashiIndex) {
                10 -> "उच्च (Exalted)"
                4 -> "नीच (Debilitated)"
                1, 8 -> "स्वगृह (Own House)"
                else -> "सम/मित्र"
            }
            "mercury" -> when (rashiIndex) {
                6 -> "उच्च/मूलत्रिकोण"
                12 -> "नीच (Debilitated)"
                3 -> "स्वगृह (Own House)"
                else -> "सम/मित्र"
            }
            "jupiter" -> when (rashiIndex) {
                4 -> "उच्च (Exalted)"
                10 -> "नीच (Debilitated)"
                9, 12 -> "स्वगृह (Own House)"
                else -> "सम/मित्र"
            }
            "venus" -> when (rashiIndex) {
                12 -> "उच्च (Exalted)"
                6 -> "नीच (Debilitated)"
                2, 7 -> "स्वगृह (Own House)"
                else -> "सम/मित्र"
            }
            "saturn" -> when (rashiIndex) {
                7 -> "उच्च (Exalted)"
                1 -> "नीच (Debilitated)"
                10, 11 -> "स्वगृह (Own House)"
                else -> "सम/मित्र"
            }
            "rahu" -> when (rashiIndex) {
                2, 3 -> "उच्च"
                8, 9 -> "नीच"
                11 -> "मूलत्रिकोण"
                else -> "सम"
            }
            "ketu" -> when (rashiIndex) {
                8, 9 -> "उच्च"
                2, 3 -> "नीच"
                5 -> "मूलत्रिकोण"
                else -> "सम"
            }
            else -> "लग्न"
        }
    }

    private fun generateVimshottariTimeline(
        birthAdDate: LocalDate,
        nakLordIndex: Int,
        bhogyaYearsDecimal: Double
    ): List<VimshottariDashaPeriod> {
        val list = mutableListOf<VimshottariDashaPeriod>()
        val today = LocalDate.now()

        var currentStartAd = birthAdDate
        var currentAgeYears = 0.0

        for (i in 0 until 9) {
            val lordIdx = (nakLordIndex + i) % 9
            val lordName = VIMSHOTTARI_LORDS[lordIdx]
            val lordSymbol = VIMSHOTTARI_SYMBOLS[lordIdx]

            val dashaYears = if (i == 0) bhogyaYearsDecimal else VIMSHOTTARI_YEARS[lordIdx]
            val endAge = currentAgeYears + dashaYears

            val daysToAdd = (dashaYears * 365.25).toLong()
            val endAd = currentStartAd.plusDays(daysToAdd)

            val startBsStr = try {
                val bs = DateConverterEngine.convertAdToBs(currentStartAd)
                "${NepaliNumerals.toNepaliDigits(bs.year)}/${NepaliNumerals.toNepaliDigits(bs.month)}"
            } catch (_: Exception) { currentStartAd.year.toString() }

            val endBsStr = try {
                val bs = DateConverterEngine.convertAdToBs(endAd)
                "${NepaliNumerals.toNepaliDigits(bs.year)}/${NepaliNumerals.toNepaliDigits(bs.month)}"
            } catch (_: Exception) { endAd.year.toString() }

            val isActive = (!today.isBefore(currentStartAd) && today.isBefore(endAd))

            list.add(
                VimshottariDashaPeriod(
                    lordName = lordName,
                    lordSymbol = lordSymbol,
                    durationYears = dashaYears,
                    startAgeYears = currentAgeYears,
                    endAgeYears = endAge,
                    startAdDate = currentStartAd,
                    endAdDate = endAd,
                    startBsDateText = startBsStr,
                    endBsDateText = endBsStr,
                    isCurrentlyActive = isActive
                )
            )

            currentStartAd = endAd
            currentAgeYears = endAge
        }

        return list
    }

    private fun generateRemedies(lagnaIndex: Int, rashiIndex: Int): CheenaAuspiciousAttributes {
        val stoneInfo = when (lagnaIndex) {
            1, 8 -> "मूँगा (Red Coral)" to "मूँगा (रातो मुङ्गा)"
            2, 7 -> "हीरा (Diamond) / ओपल" to "हीरा वा सेतो ओपल"
            3, 6 -> "पन्ना (Emerald)" to "हरियो पन्ना"
            4 -> "मोती (Pearl)" to "सच्चा मोती"
            5 -> "माणिक्य (Ruby)" to "रातो माणिक्य"
            9, 12 -> "पुखराज (Yellow Sapphire)" to "पहेंलो पुखराज"
            else -> "नीलम (Blue Sapphire)" to "नीलम वा जामुनिया"
        }

        val rudraksha = when (lagnaIndex) {
            1, 8 -> "३ मुखी वा ५ मुखी रुद्राक्ष"
            2, 7 -> "६ मुखी रुद्राक्ष"
            3, 6 -> "४ मुखी रुद्राक्ष"
            4 -> "२ मुखी रुद्राक्ष"
            5 -> "१ मुखी वा १२ मुखी रुद्राक्ष"
            9, 12 -> "५ मुखी रुद्राक्ष"
            else -> "७ मुखी वा १४ मुखी रुद्राक्ष"
        }

        val days = when (lagnaIndex) {
            1, 8 -> "मंगलबार र आइतबार"
            2, 7 -> "शुक्रबार र शनिबार"
            3, 6 -> "बुधबार र शुक्रबार"
            4 -> "सोमबार र बिहिबार"
            5 -> "आइतबार र मंगलबार"
            9, 12 -> "बिहिबार र सोमबार"
            else -> "शनिबार र बुधबार"
        }

        val colors = when (lagnaIndex) {
            1, 8 -> "रातो, सिन्दुरे, गुलाबी"
            2, 7 -> "सेतो, चम्किलो, हल्का निलो"
            3, 6 -> "हरियो, काँचो रङ्ग"
            4 -> "सेतो, दुधिलो, चाँदी रङ्ग"
            5 -> "रातो, सुन्तला, सुनौलो"
            9, 12 -> "पहेंलो, केसरिया, सुनौलो"
            else -> "निलो, कालो, बैजनी"
        }

        val numbers = when (lagnaIndex) {
            1, 8 -> "९, १, ३"
            2, 7 -> "६, ५, ८"
            3, 6 -> "५, ६, १"
            4 -> "२, ४, ९"
            5 -> "१, ३, ५"
            9, 12 -> "३, १, ९"
            else -> "८, ५, ६"
        }

        val directions = when (lagnaIndex) {
            1, 8 -> "दक्षिण दिशा"
            2, 7 -> "दक्षिण-पूर्व (आग्नेय)"
            3, 6 -> "उत्तर दिशा"
            4 -> "उत्तर-पश्चिम (वायव्य)"
            5 -> "पूर्व दिशा"
            9, 12 -> "उत्तर-पूर्व (ईशान)"
            else -> "पश्चिम दिशा"
        }

        val ishta = when (lagnaIndex) {
            1, 8 -> "भगवान हनुमान जी तथा कार्तिकेय"
            2, 7 -> "माता महालक्ष्मी तथा सन्तोषी माता"
            3, 6 -> "भगवान गणेश जी तथा भगवान विष्णु"
            4 -> "भगवान शिव (भोलेनाथ)"
            5 -> "भगवान सूर्य नारायण तथा गायत्री माता"
            9, 12 -> "भगवान विष्णु तथा दत्तात्रेय"
            else -> "भगवान शनिदेव तथा भैरव बाबा"
        }

        val mantra = when (lagnaIndex) {
            1, 8 -> "ॐ क्रां क्रीं क्रौं सः भौमाय नमः"
            2, 7 -> "ॐ द्रां द्रीं द्रौं सः शुक्राय नमः"
            3, 6 -> "ॐ ब्रां ब्रीं ब्रौं सः बुधाय नमः"
            4 -> "ॐ श्रां श्रीं श्रौं सः चन्द्राय नमः"
            5 -> "ॐ ह्रां ह्रीं ह्रौं सः सूर्याय नमः"
            9, 12 -> "ॐ ग्रां ग्रीं ग्रौं सः गुरवे नमः"
            else -> "ॐ प्रां प्रीं प्रौं सः शनैश्चराय नमः"
        }

        return CheenaAuspiciousAttributes(
            luckyStone = stoneInfo.first,
            luckyStoneNepali = stoneInfo.second,
            luckyRudraksha = rudraksha,
            luckyDays = days,
            luckyColor = colors,
            luckyNumber = numbers,
            luckyDirection = directions,
            ishtaDevata = ishta,
            grahaMantra = mantra,
            gayatriMantra = "ॐ भूर्भुवः स्वः तत्सवितुर्वरेण्यं भर्गो देवस्य धीमहि धियो यो नः प्रचोदयात्।"
        )
    }

    private fun getLagnaNature(lagnaIndex: Int): String {
        return when (lagnaIndex) {
            1 -> "मेष लग्न (अग्नि तत्व, चर): साहसी, दृढ निश्चयी, नेतृत्व क्षमतायुक्त र स्वाभिमानी स्वभाव।"
            2 -> "वृष लग्न (पृथ्वी तत्व, स्थिर): धैर्यवान्, शान्त, सौन्दर्य र कला प्रेमी, आर्थिक व्यवस्थापनमा कुशल।"
            3 -> "मिथुन लग्न (वायु तत्व, द्विस्वभाव): बुद्धिमान्, जिज्ञासु, वाकपटु, अध्ययनशील र सामाजिक स्वभाव।"
            4 -> "कर्कट लग्न (जल तत्व, चर): भावुक, संवेदनशील, परिवार प्रेमी, कल्पनाशील र परोपकारी व्यक्तित्व।"
            5 -> "सिंह लग्न (अग्नि तत्व, स्थिर): तेजस्वी, राजसी व्यक्तित्व, उदार, आत्मविश्वासी र प्रभावशाली।"
            6 -> "कन्या लग्न (पृथ्वी तत्व, द्विस्वभाव): व्यावहारिक, विश्लेषणात्मक, मिहिनेती, सफा र व्यवस्थित जीवनशैली।"
            7 -> "तुला लग्न (वायु तत्व, चर): न्यायप्रिय, सन्तुलित, आकर्षक व्यक्तित्व, शिष्ट र सहकार्यमा कुशल।"
            8 -> "वृश्चिक लग्न (जल तत्व, स्थिर): गम्भीर, दृढ संकल्पी, गूढ रहस्य प्रेमी, साहसी र ऊर्जावान्।"
            9 -> "धनु लग्न (अग्नि तत्व, द्विस्वभाव): धार्मिक, दार्शनिक, आशावादी, न्यायप्रिय र सत्यवादी स्वभाव।"
            10 -> "मकर लग्न (पृथ्वी तत्व, चर): कर्तव्यनिष्ठ, कर्मठ, धैर्यवान्, लक्ष्यप्रति समर्पित र व्यावहारिक।"
            11 -> "कुम्भ लग्न (वायु तत्व, स्थिर): नवीन विचारक, मानवीय कल्याण प्रेमी, स्वतन्त्र र गहिरो चिन्तनशील।"
            else -> "मीन लग्न (जल तत्व, द्विस्वभाव): दयालु, आध्यात्मिक, करुणामयी, संवेदनशील र शान्तिप्रिय स्वभाव।"
        }
    }

    private fun getRashiNature(rashiIndex: Int): String {
        return when (rashiIndex) {
            1 -> "मेष राशि: स्फूर्तिवान्, हतारमा निर्णय लिने बानी, पराक्रमी र नयाँ कार्यको थालनी गर्न सिपालु।"
            2 -> "वृष राशि: स्थिर सोच, विलासी जीवनशैली प्रेमी, स्वादिष्ट भोजन र सङ्गीत मन पराउने।"
            3 -> "मिथुन राशि: बहुमुखी प्रतिभा, चञ्चल मन, छिटो कुरा बुझ्ने र मित्रता गाँस्न सिपालु।"
            4 -> "कर्कट राशि: दयालु, आमाको ममतायुक्त, चञ्चल स्वभाव र स्मृतिक्षमता उच्च।"
            5 -> "सिंह राशि: नेतृत्व गर्न रुचाउने, मान-सम्मानको चाहना, दृढ विचार र स्वाभिमानी।"
            6 -> "कन्या राशि: गहिरो अध्ययनशील, स्वास्थ्यप्रति सचेत, तर्कशील र सहयोगी मन।"
            7 -> "तुला राशि: शान्त स्वभाव, कला-संस्कृति प्रेमी, विवाद मिलाउन सिपालु र न्यायप्रिय।"
            8 -> "वृश्चिक राशि: दृढ निश्चयी, रहस्यमयी, स्वाभिमानमा आँच आउन नदिने र प्रतिशोध भावनाबाट बच्नुपर्ने।"
            9 -> "धनु राशि: धार्मिक यात्रामा रुचि, सत्यवादी, उदार विचार र ज्ञान आर्जनमा सदैव तत्पर।"
            10 -> "मकर राशि: सङ्घर्षशील, ढिलो तर दिगो सफलता पाउने, मितव्ययी र अनुशासित।"
            11 -> "कुम्भ राशि: समाज सुधारक, मित्रमण्डली फराकिलो, परोपकारी र अनपेक्षित विचारयुक्त।"
            else -> "मीन राशि: भावनाप्रधान, कला-साहित्यमा रुचि, अरूको दुःखमा द्रवित हुने र दानी स्वभाव।"
        }
    }

    /**
     * Generates comprehensive Vedic astrological life predictions covering 9 key domains and remedies
     */
    fun generateLifePredictions(
        lagnaIndex: Int,
        janmaRashiIndex: Int,
        planetaryPositions: List<CheenaPlanetaryPosition>
    ): CheenaLifePredictions {
        return CheenaLifePredictions(
            personalityTraits = generatePersonalityPrediction(lagnaIndex, janmaRashiIndex),
            wealthAndFinances = generateWealthPrediction(lagnaIndex, janmaRashiIndex),
            educationAndLearning = generateEducationPrediction(lagnaIndex, janmaRashiIndex),
            careerAndProfession = generateCareerPrediction(lagnaIndex, janmaRashiIndex),
            familyAndHome = generateFamilyPrediction(lagnaIndex, janmaRashiIndex),
            healthAndLongevity = generateHealthPrediction(lagnaIndex, janmaRashiIndex),
            marriageAndLove = generateMarriagePrediction(lagnaIndex, janmaRashiIndex),
            childrenAndProgeny = generateChildrenPrediction(lagnaIndex, janmaRashiIndex),
            parentsRelationship = generateParentsPrediction(lagnaIndex, janmaRashiIndex),
            comprehensiveRemedies = generateComprehensiveRemedies(lagnaIndex, janmaRashiIndex)
        )
    }

    private fun generatePersonalityPrediction(lagnaIndex: Int, rashiIndex: Int): LifePredictionTopic {
        val lagnaName = JyotishAstroEngine.RASHI_NAMES_NEPALI[lagnaIndex - 1]
        val rashiName = JyotishAstroEngine.RASHI_NAMES_NEPALI[rashiIndex - 1]
        val lord = RASHI_LORDS[lagnaIndex - 1]

        val summary = when (lagnaIndex) {
            1, 5, 9 -> "तपाईंको जन्म कुण्डलीमा $lagnaName लग्न (अग्नि तत्व) र लग्नेश $lord रहेका छन्। चन्द्र राशि $rashiName भएकाले तपाईंमा स्वाभाविक रूपमा दृढ आत्मविश्वास, नेतृत्व क्षमता, स्पष्टवादिता र उच्च आत्मसम्मान रहनेछ। तपाईं जीवनमा कुनै पनि नयाँ कार्यको अग्रसरता लिन कहिल्यै पछि पर्नुहुन्न। सत्य र न्यायको पक्षमा दृढतापूर्वक उभिने तपाईंको विशेष स्वभाव हो।"
            2, 6, 10 -> "तपाईंको जन्म कुण्डलीमा $lagnaName लग्न (पृथ्वी तत्व) र लग्नेश $lord रहेका छन्। चन्द्र राशि $rashiName भएको कारण तपाईंमा स्वाभाविक धैर्यता, व्यावहारिक सोच, मिहिनेती स्वभाव र स्थायित्वको गुण रहेको छ। भावनामा बहकिनुभन्दा वस्तुस्थिति र तथ्यको विश्लेषण गरेर मात्र निर्णय लिने तपाईंको विशेषता हो। तपाईं शान्त र सुव्यवस्थित जीवनशैली रुचाउनुहुन्छ।"
            3, 7, 11 -> "तपाईंको जन्म कुण्डलीमा $lagnaName लग्न (वायु तत्व) र लग्नेश $lord रहेका छन्। चन्द्र राशि $rashiName भएकाले तपाईं तीव्र बौद्धिक क्षमता, वाकपटुता, जिज्ञासु स्वभाव र सामाजिक सद्भावका धनी हुनुहुन्छ। नयाँ ज्ञान सिक्ने, मानिसहरूसँग सहज सम्बन्ध गाँस्ने र नवीन योजनाहरू निर्माण गर्ने कार्यमा तपाईंको प्रतिभा अद्वितीय रहनेछ।"
            else -> "तपाईंको जन्म कुण्डलीमा $lagnaName लग्न (जल तत्व) र लग्नेश $lord रहेका छन्। चन्द्र राशि $rashiName भएकाले तपाईं गहिरो संवेदनशील, परोपकारी, कल्पनाशील र आध्यात्मिक प्रवृत्तिका हुनुहुन्छ। अरूको मनको भावना सजिलै बुझ्न सक्ने र निष्कपट सहयोग गर्ने तपाईंको कोमल स्वभावले समाजमा विशेष स्नेह प्राप्त हुन्छ।"
        }

        val strengths = when (lagnaIndex) {
            1, 5, 9 -> listOf(
                "उच्च आत्मविश्वास, निर्णय लिने क्षमता र स्वाभाविक नेतृत्व गुण",
                "कठिन परिस्थितिमा पनि हिम्मत नहार्ने र अग्रपंक्तिमा रहेर काम गर्ने स्वभाव",
                "स्पष्टवादी र स्वाभिमानी व्यक्तित्व, सत्य र न्यायप्रति गहिरो निष्ठा"
            )
            2, 6, 10 -> listOf(
                "असीम धैर्य, लगनशीलता र योजनाबद्ध कार्यशैली",
                "व्यावहारिक विवेक र आर्थिक तथा सामाजिक दायित्व कुशलतापूर्वक पूरा गर्ने क्षमता",
                "विश्वासपात्र मित्र र संकटको समयमा स्थिर रहन सक्ने आत्मबल"
            )
            3, 7, 11 -> listOf(
                "उत्कृष्ट वाकपटुता, प्रभावकारी संवाद र सम्झाउने-बुझाउने क्षमता",
                "बहुमुखी प्रतिभा र नयाँ परिस्थिति तथा प्रविधिसँग छिट्टै घुलमिल हुने गुण",
                "न्यायप्रिय सोच, सहकार्य र फराकिलो सामाजिक मित्रमण्डली"
            )
            else -> listOf(
                "गहिरो अन्तरज्ञान (Intuition), संवेदनशीलता र दयालु हृदय",
                "सृजनात्मक कल्पनाशीलता, कला तथा साहित्यमा स्वाभाविक रुचि",
                "शान्तिप्रिय, निष्कपट र अरूको दुःखमा साथ दिने परोपकारी भावना"
            )
        }

        val challenges = when (lagnaIndex) {
            1, 5, 9 -> listOf(
                "हतारमा निर्णय लिने बानी र रिस वा आवेगलाई नियन्त्रण गर्नुपर्ने आवश्यकता",
                "अरूको सल्लाह सुन्न नचाहने र अत्यधिक जिद्दीपनबाट बच्नुपर्ने"
            )
            2, 6, 10 -> listOf(
                "कहिलेकाहीँ अत्यधिक सतर्कता वा ढिलासुस्तीले अवसर गुम्न सक्ने जोखिम",
                "परिवर्तनलाई छिटो स्वीकार गर्न हिचकिचाउने सोचाइमा लचकता ल्याउनु पर्ने"
            )
            3, 7, 11 -> listOf(
                "मनको चञ्चलता र एकैपटक धेरै काम सुरु गर्दा काम अधुरो रहने सम्भावना",
                "द्विविधा वा निर्णयहीनतामा लामो समय बिताउने बानी सुधार्नुपर्ने"
            )
            else -> listOf(
                "अत्यधिक भावुकता र अरूको कुरामा छिट्टै विश्वास गरेर धोका पाउने जोखिम",
                "विगतका तीता अनुभवहरूलाई बारम्बार सम्झेर मन खिन्न बनाउने बानी"
            )
        }

        val guidance = "दैनिक बिहान सूर्य नमस्कार र आफ्ना इष्टदेवताको स्मरण गर्नुहोस्। आफ्नो दृढ इच्छाशक्तिलाई रचनात्मक कर्ममा लगाउँदै क्रोध र अधैर्यतालाई संयम राख्नु नै तपाईंको व्यक्तित्व विकासको मुख्य सूत्र हो।"

        return LifePredictionTopic(
            title = "व्यक्तिगत विशेषता तथा स्वभाव",
            englishTitle = "Personality & Vedic Nature",
            iconEmoji = "👤",
            primaryHouseInfo = "प्रथम भाव (लग्न / तनु भाव)",
            analysisSummary = summary,
            favorableStrengths = strengths,
            precautionsAndChallenges = challenges,
            auspiciousGuidance = guidance
        )
    }

    private fun generateWealthPrediction(lagnaIndex: Int, rashiIndex: Int): LifePredictionTopic {
        val lagnaName = JyotishAstroEngine.RASHI_NAMES_NEPALI[lagnaIndex - 1]
        val house2Index = ((lagnaIndex - 1 + 1) % 12) + 1
        val house2Name = JyotishAstroEngine.RASHI_NAMES_NEPALI[house2Index - 1]
        val house2Lord = RASHI_LORDS[house2Index - 1]
        val house11Index = ((lagnaIndex - 1 + 10) % 12) + 1
        val house11Name = JyotishAstroEngine.RASHI_NAMES_NEPALI[house11Index - 1]
        val house11Lord = RASHI_LORDS[house11Index - 1]

        val summary = "तपाईंको कुण्डलीमा धन भाव (द्वितीय भाव) मा $house2Name राशि (स्वामी: $house2Lord) र लाभ भाव (एकादश भाव) मा $house11Name राशि (स्वामी: $house11Lord) रहेका छन्। $lagnaName लग्नका लागि आर्थिक स्थिति क्रमशः उन्नतिको दिशामा जाने योग छ। प्रारम्भिक जीवनमा केही उतारचढाव भए पनि उमेरको परिपक्वतासँगै स्थिर सम्पत्ति, बचत र लगानीबाट उल्लेखनीय धनलाभ हुनेछ। आफ्नै मिहिनेत र सुझबुझले आर्जित धन दीर्घकालीन रूपमा सुरक्षित रहनेछ।"

        val strengths = listOf(
            "आफ्नै पुरुषार्थ, सीप र परिश्रमबाट धन आर्जन गर्ने सबल योग",
            "दीर्घकालीन लगानी, जग्गा-जमिन वा अचल सम्पत्ति जोड्न सक्ने क्षमता",
            "जीवनको उत्तरार्धमा आर्थिक स्वतन्त्रता र आम्दानीका एकभन्दा बढी स्रोतहरू"
        )

        val challenges = listOf(
            "विलासिता, देखावटी वा अनावश्यक खर्चमा नियन्त्रण गर्नुपर्ने",
            "छिटो धनी बन्ने प्रलोभन, जोखिमपूर्ण सट्टेबाजी वा बिना सम्झौता ऋण लेनदेनबाट जोगिनुपर्ने"
        )

        val guidance = "शुक्रबार माता महालक्ष्मीको आराधना र श्री सूक्तको पाठ गर्नुहोस्। उत्तर दिशा (कुबेर स्थान) सफा राख्नुहोस् र आफ्नो कुल आम्दानीको केही अंश परोपकारमा लगाउँदा धनको स्थायी वृद्धि हुन्छ।"

        return LifePredictionTopic(
            title = "धन, सम्पत्ति तथा आर्थिक स्थिति",
            englishTitle = "Wealth, Finances & Prosperity",
            iconEmoji = "💰",
            primaryHouseInfo = "द्वितीय भाव (धन) र एकादश भाव (आय/लाभ)",
            analysisSummary = summary,
            favorableStrengths = strengths,
            precautionsAndChallenges = challenges,
            auspiciousGuidance = guidance
        )
    }

    private fun generateEducationPrediction(lagnaIndex: Int, rashiIndex: Int): LifePredictionTopic {
        val house4Index = ((lagnaIndex - 1 + 3) % 12) + 1
        val house4Name = JyotishAstroEngine.RASHI_NAMES_NEPALI[house4Index - 1]
        val house5Index = ((lagnaIndex - 1 + 4) % 12) + 1
        val house5Name = JyotishAstroEngine.RASHI_NAMES_NEPALI[house5Index - 1]
        val house5Lord = RASHI_LORDS[house5Index - 1]

        val faculty = when (lagnaIndex) {
            1, 8 -> "इन्जिनियरिङ, प्राविधिक विषय, व्यवस्थापन, कानुन, सुरक्षा निकाय वा विज्ञान"
            2, 7 -> "कला, सङ्गीत, वाणिज्य/बैंकिङ, साहित्य, फेसन, आर्किटेक्चर वा व्यवस्थापन"
            3, 6 -> "सूचना प्रविधि (IT), सञ्चार, पत्रकारिता, अर्थशास्त्र, तथ्याङ्क वा अनुसन्धान"
            4 -> "चिकित्सा/नर्सिङ, वातावरण, समाजशास्त्र, प्रशासन वा प्राज्ञिक अध्ययन"
            5 -> "प्रशासनिक सेवा, राजनीति शास्त्र, व्यवस्थापन, चिकित्सा वा नेतृत्व विकास"
            9, 12 -> "उच्च शिक्षा, दर्शनशास्त्र, कानुन, धर्म-संस्कृति, प्राध्यापन वा बैंक/वित्त"
            else -> "अनुसन्धान, विज्ञान, कानुन, उद्योग व्यवस्थापन, भू-गर्भ वा श्रम अध्ययन"
        }

        val summary = "तपाईंको कुण्डलीमा विद्या भाव (चतुर्थ भाव) मा $house4Name राशि र बुद्धि तथा उच्च शिक्षा भाव (पञ्चम भाव) मा $house5Name राशि (स्वामी: $house5Lord) रहेका छन्। तपाईंको बौद्धिक क्षमता ग्रहणशील र विश्लेषणात्मक छ। $faculty क्षेत्रमा तपाईंको अध्ययन विशेष फलदायी र यशदायी रहनेछ। प्रतिस्पर्धात्मक परीक्षा तथा उच्च अध्ययनमा दृढ संकल्प गर्दा सफलता सुनिश्चित छ।"

        val strengths = listOf(
            "तीव्र स्मरणशक्ति, गहिरो अध्ययनशीलता र जटिल विषय सजिलै बुझ्न सक्ने क्षमता",
            "प्रतिस्पर्धात्मक परीक्षा, शैक्षिक वादविवाद र प्राविधिक ज्ञानमा उच्च सफलता",
            "व्यावहारिक ज्ञान र सैद्धान्तिक ज्ञानको कुशल संयोजन"
        )

        val challenges = listOf(
            "अध्ययनको बीचमा मन विकेन्द्रित हुनु वा परीक्षाको समयमा अनावश्यक चिन्ता लिनु",
            "एकैसाथ धेरै विषयमा हात हाल्दा मुख्य लक्ष्यबाट ध्यान हट्न सक्ने"
        )

        val guidance = "दैनिक अध्ययनपूर्व 'ॐ ऐं सरस्वत्यै नमः' मन्त्र जप गर्नुहोस्। अध्ययन कोठामा पूर्व वा उत्तर दिशा तर्फ फर्केर बस्ने बानी बसाल्दा एकाग्रता र ज्ञानार्जन क्षमतामा अभूतपूर्व वृद्धि हुन्छ।"

        return LifePredictionTopic(
            title = "शिक्षा, विद्या तथा बौद्धिक क्षमता",
            englishTitle = "Education, Knowledge & Intellect",
            iconEmoji = "🎓",
            primaryHouseInfo = "चतुर्थ भाव (आधारभूत विद्या) र पञ्चम भाव (उच्च बुद्धि)",
            analysisSummary = summary,
            favorableStrengths = strengths,
            precautionsAndChallenges = challenges,
            auspiciousGuidance = guidance
        )
    }

    private fun generateCareerPrediction(lagnaIndex: Int, rashiIndex: Int): LifePredictionTopic {
        val house10Index = ((lagnaIndex - 1 + 9) % 12) + 1
        val house10Name = JyotishAstroEngine.RASHI_NAMES_NEPALI[house10Index - 1]
        val house10Lord = RASHI_LORDS[house10Index - 1]

        val field = when (house10Index) {
            1, 8 -> "प्रशासन, सुरक्षा निकाय, इन्जिनियरिङ, शल्यचिकित्सा वा रियल स्टेट व्यवसाय"
            2, 7 -> "बैंकिङ, वित्तीय क्षेत्र, कला-मनोरञ्जन, आयात-निर्यात वा होटल-पर्यटन व्यवसाय"
            3, 6 -> "सञ्चार, मिडिया, सूचना प्रविधि, लेखन, लेखा परीक्षण (CA) वा परामर्श सेवा"
            4 -> "शिक्षा, जल-स्रोत, चिकित्सा, सामाजिक क्षेत्र वा सार्वजनिक प्रशासन"
            5 -> "सरकारी उच्च पद, राजनीति, नेतृत्व, व्यवस्थापकीय भूमिका वा ठूलो व्यापार"
            9, 12 -> "न्यायिक सेवा, कानुन, प्राध्यापन, धार्मिक-आध्यात्मिक कार्य वा अन्तर्राष्ट्रिय संस्था"
            else -> "उद्योग, कलकारखाना, निर्माण कार्य, खनिज, श्रम व्यवस्थापन वा प्राविधिक सेवा"
        }

        val summary = "कर्म भाव (दशम भाव) मा $house10Name राशि र दशमेश $house10Lord रहेका छन्। तपाईंको कार्यक्षेत्रमा $field अनुकूल रहनेछन्। तपाईं जागिर वा स्वतन्त्र व्यापार दुवैमा आफ्नो छुट्टै पहिचान बनाउन सक्नुहुन्छ। तपाईंको कामप्रतिको समर्पण र उत्तरदायित्व बोधले कार्यस्थलमा वरिष्ठ व्यक्तिहरूको विश्वास र समाजमा मानसम्मान दिलाउनेछ।"

        val strengths = listOf(
            "नेतृत्व क्षमता, कार्यसम्पादनमा दक्षता र जिम्मेवारीलाई निष्ठापूर्वक पूरा गर्ने बानी",
            "व्यावसायिक विश्वसनीयता, संकट समाधान गर्ने कौशल र समाजमा प्रतिष्ठा",
            "मिहिनेतको जगमा निरन्तर पदोन्नति र करियरमा स्थायित्व"
        )

        val challenges = listOf(
            "कार्यस्थलमा सहकर्मीहरूसँग अनावश्यक अहंता वा वैचारिक द्वन्द्वबाट जोगिनुपर्ने",
            "अधैर्य भई बारम्बार कार्यक्षेत्र वा जागिर परिवर्तन गर्ने सोचबाट बच्नुपर्ने"
        )

        val guidance = "दैनिक बिहान सूर्य नारायणलाई जल अर्पण गरी कार्य प्रारम्भ गर्नुहोस्। आफ्नो कर्ममा सधैं इमानदार र निष्पक्ष रहनुहोस्; दशमेश ग्रहको अनुकूलताका लागि आफ्नो पेशागत मर्यादा उच्च राख्नुहोस्।"

        return LifePredictionTopic(
            title = "रोजगार, करियर तथा कार्यक्षेत्र",
            englishTitle = "Career, Job & Profession",
            iconEmoji = "💼",
            primaryHouseInfo = "दशम भाव (कर्म भाव) र षष्ठ भाव (सेवा/उद्योग)",
            analysisSummary = summary,
            favorableStrengths = strengths,
            precautionsAndChallenges = challenges,
            auspiciousGuidance = guidance
        )
    }

    private fun generateFamilyPrediction(lagnaIndex: Int, rashiIndex: Int): LifePredictionTopic {
        val summary = "कुण्डलीको द्वितीय (कुटुम्ब) र चतुर्थ (गृह सुख) भावको विश्लेषण अनुसार तपाईं परिवारको सुख र समृद्धिका लागि समर्पित रहने व्यक्ति हुनुहुन्छ। पारिवारिक जमघट, चाडपर्व र परम्परालाई तपाईं मनैदेखि माया गर्नुहुन्छ। कहिलेकाहीँ ससाना कुरामा मतभेद देखिए पनि आपसी प्रेम र समझदारीले परिवारलाई एकताबद्ध राख्न तपाईंको भूमिका अहम् रहनेछ।"

        val strengths = listOf(
            "परिवारप्रतिको गहिरो उत्तरदायित्व र आत्मीय जनहरूको संरक्षण गर्ने क्षमता",
            "पुर्ख्यौली संस्कार, मर्यादा र सामाजिक परम्पराको सम्मान",
            "कठिन घडीमा सम्पूर्ण परिवारलाई ढाडस दिने र सम्हाल्ने नेतृत्वदायी गुण"
        )

        val challenges = listOf(
            "कहिलेकाहीँ स्पष्ट वा कडा बोलीले परिवारका सदस्यहरूको मनमा चोट पर्न सक्ने",
            "घरपरिवारका विषयमा बाहिरी मानिसको सल्लाहमा बढी भर पर्दा उत्पन्न हुने भ्रम"
        )

        val guidance = "परिवारमा खुला संवादको वातावरण बनाउनुहोस्। घरको कुलदेवताको नियमित पूजा गर्नुहोस् र चाडपर्वमा ज्येष्ठजनको आशीर्वाद लिनुहोस्। घरमा नियमित शान्ति पाठ र दियो बाल्दा पारिवारिक सद्भाव बढ्छ।"

        return LifePredictionTopic(
            title = "परिवार तथा पारिवारिक सुख",
            englishTitle = "Family Harmony & Domestic Life",
            iconEmoji = "🏡",
            primaryHouseInfo = "द्वितीय भाव (कुटुम्ब) र चतुर्थ भाव (गृह सुख)",
            analysisSummary = summary,
            favorableStrengths = strengths,
            precautionsAndChallenges = challenges,
            auspiciousGuidance = guidance
        )
    }

    private fun generateHealthPrediction(lagnaIndex: Int, rashiIndex: Int): LifePredictionTopic {
        val dosha = when (lagnaIndex) {
            1, 5, 9 -> "पित्त प्रधान प्रवृत्तिका कारण टाउको दुखाइ, रक्तचाप, आँखा वा पाचन प्रणालीमा गर्मी"
            2, 6, 10 -> "वात र कफ मिश्रित प्रवृत्तिका कारण जोर्नी दुखाइ, घाँटी वा छालाको संवेदनशीलता"
            3, 7, 11 -> "वात प्रधान प्रवृत्तिका कारण स्नायु प्रणाली, अनिद्रा, श्वासप्रश्वास वा मानसिक तनाव"
            else -> "कफ प्रधान प्रवृत्तिका कारण रुघाखोकी, छाती/फोक्सो, जल सन्तुलन वा मौसम परिवर्तनमा संवेदनशीलता"
        }

        val summary = "तपाईंको लग्न र षष्ठ भावको विश्लेषण गर्दा प्राकृतिक रूपमा शारीरिक बल र स्फूर्ति राम्रो देखिन्छ। यद्यपि $dosha प्रति समयमै सचेत रहनु बुद्धिमानी हुनेछ। नियमित दिनचर्या, सन्तुलित खानपान र मानसिक शान्तिको अभ्यासले तपाईंलाई दीर्घायु र निरोगी जीवन प्रदान गर्नेछ।"

        val strengths = listOf(
            "रोगसँग लड्ने प्राकृतिक रोग प्रतिरोध क्षमता (Immunity) राम्रो",
            "सकारात्मक सोच र आत्मबलका कारण शारीरिक समस्याबाट छिट्टै मुक्ति पाउने शक्ति",
            "नियमित योग र व्यायामबाट शरीरलाई सधैं तन्दुरुस्त राख्न सक्ने क्षमता"
        )

        val challenges = listOf(
            "अत्यधिक मानसिक तनाव, कामको चाप र समयमा नसुत्ने बानीले स्वास्थ्यमा असर",
            "खानपानमा लापरवाही, बासी वा अत्यधिक चिल्लो-पिरो खानाबाट पाचन गडबडी"
        )

        val guidance = "दैनिक कम्तीमा १५ मिनेट योग, प्राणायाम र ध्यान गर्नुहोस्। भगवान शिवको पञ्चाक्षरी मन्त्र 'ॐ नमः शिवाय' वा महामृत्युञ्जय जप गर्नु आरोग्यताका लागि परम कल्याणकारी हुन्छ।"

        return LifePredictionTopic(
            title = "स्वास्थ्य, आरोग्यता तथा आयु",
            englishTitle = "Health, Vitality & Longevity",
            iconEmoji = "🌿",
            primaryHouseInfo = "प्रथम भाव (शारीरिक बल), षष्ठ भाव (रोग) र अष्टम भाव (आयु)",
            analysisSummary = summary,
            favorableStrengths = strengths,
            precautionsAndChallenges = challenges,
            auspiciousGuidance = guidance
        )
    }

    private fun generateMarriagePrediction(lagnaIndex: Int, rashiIndex: Int): LifePredictionTopic {
        val house7Index = ((lagnaIndex - 1 + 6) % 12) + 1
        val house7Name = JyotishAstroEngine.RASHI_NAMES_NEPALI[house7Index - 1]
        val house7Lord = RASHI_LORDS[house7Index - 1]

        val summary = "सप्तम भाव (कलत्र तथा वैवाहिक भाव) मा $house7Name राशि र सप्तमेश $house7Lord रहेका छन्। वैवाहिक जीवनमा समझदार, सुसंस्कृत र सहयोगी जीवनसाथी प्राप्त हुने योग छ। विवाहपश्चात् तपाईंको भाग्य र सामाजिक प्रतिष्ठामा थप वृद्धि हुनेछ। दुवै पक्षबीच खुला संवाद, आपसी आदर र सहकार्य रहँदा गृहस्थ जीवन सुमधुर र दीर्घकालीन रूपमा सुखमय बन्नेछ।"

        val strengths = listOf(
            "जीवनसाथीबाट पूर्ण भावनात्मक र नैतिक समर्थन प्राप्त हुने योग",
            "विवाहपश्चात् पारिवारिक उन्नति, आर्थिक स्थायित्व र भाग्योदय",
            "गृहस्थीलाई कुशलतापूर्वक सञ्चालन गर्ने परस्पर समझदारी"
        )

        val challenges = listOf(
            "सानातिना कुरामा अहंता (Ego) को टकराव वा जिद्दीपनबाट बच्नुपर्ने",
            "जीवनसाथीमाथि अनावश्यक शंका वा अत्यधिक अपेक्षा राख्न नहुने"
        )

        val guidance = "वैवाहिक सुख र दाम्पत्य शान्तिका लागि माता पार्वती र भगवान शिवको आराधना गर्नुहोस्। शुक्रबार सेतो फूल वा मिठाई देवी मन्दिरमा अर्पण गर्नुहोस् र परस्पर सम्मानलाई सधैं प्राथमिकता दिनुहोस्।"

        return LifePredictionTopic(
            title = "विवाह तथा दाम्पत्य जीवन",
            englishTitle = "Marriage & Conjugal Life",
            iconEmoji = "💍",
            primaryHouseInfo = "सप्तम भाव (कलत्र / दाम्पत्य भाव)",
            analysisSummary = summary,
            favorableStrengths = strengths,
            precautionsAndChallenges = challenges,
            auspiciousGuidance = guidance
        )
    }

    private fun generateChildrenPrediction(lagnaIndex: Int, rashiIndex: Int): LifePredictionTopic {
        val house5Index = ((lagnaIndex - 1 + 4) % 12) + 1
        val house5Name = JyotishAstroEngine.RASHI_NAMES_NEPALI[house5Index - 1]
        val house5Lord = RASHI_LORDS[house5Index - 1]

        val summary = "सन्तान भाव (पञ्चम भाव) मा $house5Name राशि (स्वामी: $house5Lord) र सन्तानकारक देवगुरु बृहस्पतिको स्थिति अनुसार तपाईंलाई योग्य, बुद्धिमान् र संस्कारी सन्तानको सुख प्राप्त हुनेछ। छोराछोरीको शिक्षा र करियरमा उत्कृष्ट प्रगति हुनेछ र उनीहरूले कुलको नाम उज्यालो बनाउनेछन्। सन्तानसँग मित्रतापूर्ण व्यवहार गर्नाले सम्बन्ध अझ प्रगाढ र आत्मीय बन्नेछ।"

        val strengths = listOf(
            "सन्तानको तीव्र बौद्धिक विकास र शैक्षिक तथा व्यावसायिक सफलता",
            "छोराछोरीबाट आमाबाबुप्रति आदरभाव, प्रेम र वृद्धावस्थामा भरपर्दो सहारा",
            "सन्तानको आगमनसँगै पारिवारिक सुख-समृद्धिमा विशेष बढोत्तरी"
        )

        val challenges = listOf(
            "पुस्तान्तरणका कारण सोचाइमा आउने भिन्नतालाई लिएर अनावश्यक कडा नियन्त्रण गर्न नहुने",
            "छोराछोरीको अध्ययन वा करियरमा आफ्नो इच्छा जबरजस्ती थोपर्नबाट बच्नुपर्ने"
        )

        val guidance = "सन्तानको उन्नतिको लागि भगवान गणेशको आराधना गर्नुहोस् र 'सन्तान गोपाल मन्त्र' जप गर्नुहोस्। छोराछोरीलाई सानैदेखि नैतिक संस्कार, अनुशासन र दयाभावको शिक्षा दिनुहोस्।"

        return LifePredictionTopic(
            title = "सन्तान तथा छोराछोरीसँगको सम्बन्ध",
            englishTitle = "Children & Progeny Relationship",
            iconEmoji = "👶",
            primaryHouseInfo = "पञ्चम भाव (पुत्र / सन्तान भाव) र देवगुरु",
            analysisSummary = summary,
            favorableStrengths = strengths,
            precautionsAndChallenges = challenges,
            auspiciousGuidance = guidance
        )
    }

    private fun generateParentsPrediction(lagnaIndex: Int, rashiIndex: Int): LifePredictionTopic {
        val house4Index = ((lagnaIndex - 1 + 3) % 12) + 1
        val house4Name = JyotishAstroEngine.RASHI_NAMES_NEPALI[house4Index - 1]
        val house4Lord = RASHI_LORDS[house4Index - 1]
        val house9Index = ((lagnaIndex - 1 + 8) % 12) + 1
        val house9Name = JyotishAstroEngine.RASHI_NAMES_NEPALI[house9Index - 1]
        val house9Lord = RASHI_LORDS[house9Index - 1]

        val summary = "कुण्डलीमा मातृ भाव (चतुर्थ भाव: $house4Name, स्वामी: $house4Lord) र पितृ भाव (नवम/दशम भाव: $house9Name, स्वामी: $house9Lord) अनुसार मातापितासँगको सम्बन्ध अत्यन्त शुभ र आदरयुक्त रहनेछ। आमाको निष्कपट स्नेह र आशीर्वाद तपाईंको जीवनको सबैभन्दा ठूलो ढाल हो भने बाबुको प्रेरणा, मार्गदर्शन र आदर्शले तपाईंलाई जीवनको सही मार्गमा डोहोर्‍याउनेछ। मातापिताको सेवाबाट तपाईंको भाग्य चम्किनेछ।"

        val strengths = listOf(
            "आमाको अनन्य ममता, आशीर्वाद र मानसिक शान्ति प्राप्त हुने योग",
            "बाबुको अनुभव, मार्गदर्शन र पैतृक परम्पराको गर्विलो विरासत",
            "मातापिताको सेवा र सम्मान गर्दा सम्पूर्ण ग्रहदोष स्वतः शान्त हुने शक्ति"
        )

        val challenges = listOf(
            "कहिलेकाहीँ वैचारिक भिन्नतामा चर्को बहस वा चित्त दुखाउने वचन बोल्नबाट जोगिनुपर्ने",
            "आमाबाबुको स्वास्थ्यप्रति समयमै संवेदनशील भई हेरचाहमा ध्यान दिनुपर्ने"
        )

        val guidance = "प्रत्येक बिहान उठेर मातापिताको चरण स्पर्श गरी आशीर्वाद लिनुहोस्। पितृकार्य तथा श्राद्ध श्रद्धापूर्वक सम्पन्न गर्नुहोस्। आमालाई सोमबार वा पूर्णिमामा सेतो वस्तु र बाबुलाई आइतबार पहेंलो वा तामाको वस्तु उपहार दिनु अत्यन्त शुभ मानिन्छ।"

        return LifePredictionTopic(
            title = "आमा बाबुसँगको सम्बन्ध",
            englishTitle = "Relationship with Parents",
            iconEmoji = "👨‍👩‍👧",
            primaryHouseInfo = "चतुर्थ भाव (माता) र नवम भाव (पिता / पितृ धर्म)",
            analysisSummary = summary,
            favorableStrengths = strengths,
            precautionsAndChallenges = challenges,
            auspiciousGuidance = guidance
        )
    }

    private fun generateComprehensiveRemedies(lagnaIndex: Int, rashiIndex: Int): LifeRemediesTopic {
        val eduRemedies = listOf(
            "दैनिक अध्ययन सुरु गर्नुपूर्व 'ॐ ऐं सरस्वत्यै नमः' मन्त्र कम्तीमा ११ वा २१ पटक श्रद्धापूर्वक जप गर्नुहोस्।",
            "अध्ययन गर्दा सदैव पूर्व वा उत्तर दिशा तर्फ फर्केर बस्ने बानी बसाल्नुहोस्; यसले एकाग्रता र स्मरणशक्तिमा वृद्धि गर्छ।",
            "विद्यार्थीहरूका लागि ४ मुखी वा ५ मुखी रुद्राक्ष वा हरियो पन्ना धारण गर्नु तथा बुध ग्रहको आराधना गर्नु उत्तम हुन्छ।",
            "प्रत्येक बुधबार वा बिहिबार असहाय बालबालिकालाई कापी, कलम वा पुस्तक दान गर्नुहोस्।"
        )

        val healthRemedies = listOf(
            "दैनिक बिहान सूर्य नमस्कार गरी तामाको लोटाबाट 'ॐ घृणिः सूर्याय नमः' भन्दै सूर्य नारायणलाई अर्घ्य दिनुहोस्।",
            "आरोग्यता र दीर्घायुका लागि भगवान शिवको पञ्चाक्षरी मन्त्र 'ॐ नमः शिवाय' वा 'महामृत्युञ्जय मन्त्र' नियमित जप गर्नुहोस्।",
            "ऋतु अनुकूल सात्विक खानपान, नियमित प्राणायाम र पर्याप्त जल सेवनलाई दैनिक नियम बनाउनुहोस्।",
            "हरेक सोमबार वा पूर्णिमामा शिवलिंगमा जल र काँचो दूध अर्पण गर्दा मानसिक तनाव र शारीरिक कष्ट शान्त हुन्छ।"
        )

        val wealthRemedies = listOf(
            "प्रत्येक शुक्रबार माता महालक्ष्मीको मन्दिरमा वा घरको पूजाकोठामा घिउको दियो बालेर 'श्री सूक्त' वा 'कनकधारा स्तोत्र' पाठ गर्नुहोस्।",
            "घरको उत्तर दिशा (कुबेरको स्थान) सधैं सफा, उज्यालो र हलुका राख्नुहोस्; त्यहाँ तुलसी वा जलपात्र राख्नु मंगलकारी हुन्छ।",
            "आफ्नो शुद्ध आम्दानीको केही अंश (दशांश) गरिब, दीनदुःखी वा सामाजिक सेवामा लगाउनाले धनको स्थायी बरकत रहन्छ।",
            "व्यापार वा नयाँ आर्थिक लगानी गर्दा शुभ तिथि (द्वितीया, पञ्चमी, दशमी वा पूर्णिमा) र शुभ बार रोज्नुहोस्।"
        )

        val fortuneRemedies = listOf(
            "दैनिक बिहान उठ्नासाथ सर्वप्रथम आफ्ना दुवै हत्केलाको दर्शन गरी मातापिता र गुरुजनको चरण स्पर्श गर्नुहोस्।",
            "आफ्नो कुलदेवता र इष्टदेवताको नित्य स्मरण गर्नुहोस् र वर्षको एकपटक विशेष कुलपूजा अनिवार्य सम्पन्न गर्नुहोस्।",
            "प्रत्येक बिहान तुलसीको मोठमा जल चढाउनुहोस् र बिहिबार पीपलको रुखमा जलार्पण गरी ७ पटक परिक्रमा गर्नुहोस्।",
            "एकादशी, औंशी र पूर्णिमा जस्ता पर्वहरूमा व्रत, मन्त्र जप र दान-पुण्य गर्नाले सम्पूर्ण बाधा-व्यवधान हटेर भाग्योदय हुन्छ।"
        )

        val dailyRituals = listOf(
            "बिहान-साँझ घरमा दियो-धूप बालेर महा गायत्री मन्त्र कम्तीमा १०८ वा २७ पटक जप गर्नुहोस्।",
            "घरको मुख्य ढोकामा स्वस्तिक वा ॐ को शुभ चिन्ह अंकित गरी बिहान सफा जल छर्कने बानी बसाल्नुहोस्।",
            "दैनिक भोजन पूर्व पहिलो ग्रास वा रोटी गाईलाई, चराचुरुङ्गीलाई अन्न र कुकुरलाई आहार दिनाले नवग्रह प्रसन्न रहन्छन्।"
        )

        return LifeRemediesTopic(
            title = "शिक्षा, स्वास्थ्य, धन सम्पत्ति तथा भाग्योदयका उपायहरू",
            educationRemedies = eduRemedies,
            healthRemedies = healthRemedies,
            wealthRemedies = wealthRemedies,
            fortuneRemedies = fortuneRemedies,
            dailyVedicRituals = dailyRituals
        )
    }

    fun calculateNirayanaAscendantDeg(
        date: LocalDate,
        time: LocalTime,
        lat: Double,
        lon: Double,
        tzOffset: Double
    ): Double {
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

        return nirayanaAsc
    }

    /**
     * Formats full Cheena Patrika text for easy sharing via SMS, WhatsApp, or Notes
     */
    fun generateCheenaShareText(data: CheenaPatrikaData): String {
        return buildString {
            appendLine("॥ श्री गणेशाय नमः ॥")
            appendLine("📜 जन्म चिना पत्रिका (वैदिक कुण्डली)")
            appendLine("═════════════════════════════")
            appendLine("👤 जातक परिचय:")
            appendLine("• नाम: ${data.name}")
            appendLine("• लिङ्ग: ${data.genderText}")
            appendLine("• जन्म मिति (वि.सं.): ${data.bsBirthDateText}")
            appendLine("• जन्म मिति (इ.सं.): ${data.adBirthDateText}")
            appendLine("• जन्म समय: ${data.birthTimeStringNepali} (${data.birthTimeText} बजे)")
            appendLine("• जन्म स्थान: ${data.birthPlaceText}")
            appendLine()
            appendLine("🌟 पञ्चाङ्ग साधन:")
            appendLine("• संवत्: ${data.vikramSambatText} | ${data.nepalSambatText} | ${data.shakaSambatText}")
            appendLine("• अयन / ऋतु: ${data.ayanName} / ${data.rituName} ऋतु")
            appendLine("• तिथि: ${data.tithiName} (${data.pakshaName})")
            appendLine("• बार: ${data.vaarName} (वारेश: ${data.vaarLord})")
            appendLine("• नक्षत्र: ${data.nakshatraName} (पाउ ${data.pada})")
            appendLine("• योग / करण: ${data.yogaName} / ${data.karanaName}")
            appendLine("• सूर्योदय / सूर्यास्त: ${data.sunriseText} / ${data.sunsetText}")
            appendLine()
            appendLine("🪐 जन्म अङ्ग तथा अष्टकूट आधार:")
            appendLine("• जन्म राशि: ${data.janmaRashiName} (राशीश: ${data.rashiLord})")
            appendLine("• जन्म लग्न: ${data.lagnaName} (${data.lagnaDegreeFormatted}) (लग्नेश: ${data.lagnaLord})")
            appendLine("• नवमांश लग्न: ${data.navamshaLagnaName}")
            appendLine("• नाम जुराउने अक्षर: '${data.naamAkshar}'")
            appendLine("• नक्षत्र स्वामी: ${data.nakshatraLord}")
            appendLine("• वर्ण: ${data.varna} | वश्य: ${data.vashya}")
            appendLine("• योनि: ${data.yoni} | गण: ${data.gana} | नाडी: ${data.nadi}")
            appendLine()
            appendLine("⏳ इष्टकाल तथा भयात/भभोग:")
            appendLine("• इष्टकाल: ${data.ishtakalaGhadiPalaText}")
            appendLine("• भयात: ${data.bhayatGhadiPalaText} | भभोग: ${data.bhabhogGhadiPalaText}")
            appendLine("• जन्मकालीन भोग्य दशा: ${data.bhogyaDashaText}")
            appendLine()
            appendLine("💎 शुभ तत्व र उपचारात्मक मार्गदर्शन:")
            appendLine("• शुभ रत्न: ${data.remedies.luckyStoneNepali}")
            appendLine("• शुभ रुद्राक्ष: ${data.remedies.luckyRudraksha}")
            appendLine("• शुभ बार: ${data.remedies.luckyDays}")
            appendLine("• शुभ रङ्ग: ${data.remedies.luckyColor}")
            appendLine("• शुभ अंक: ${data.remedies.luckyNumber}")
            appendLine("• इष्ट देवता: ${data.remedies.ishtaDevata}")
            appendLine("• ग्रह मन्त्र: ${data.remedies.grahaMantra}")
            appendLine()
            appendLine("🔮 मुख्य जीवन फलादेश विचार:")
            appendLine("• व्यक्तित्व: ${data.lifePredictions.personalityTraits.analysisSummary.take(130)}...")
            appendLine("• धन/सम्पत्ति: ${data.lifePredictions.wealthAndFinances.analysisSummary.take(130)}...")
            appendLine("• शिक्षा/विद्या: ${data.lifePredictions.educationAndLearning.analysisSummary.take(130)}...")
            appendLine("• रोजगार/करियर: ${data.lifePredictions.careerAndProfession.analysisSummary.take(130)}...")
            appendLine("• वैवाहिक जीवन: ${data.lifePredictions.marriageAndLove.analysisSummary.take(130)}...")
            appendLine("• सन्तान सम्बन्ध: ${data.lifePredictions.childrenAndProgeny.analysisSummary.take(130)}...")
            appendLine("• आमाबुबा सम्बन्ध: ${data.lifePredictions.parentsRelationship.analysisSummary.take(130)}...")
            appendLine()
            appendLine("🌟 भाग्योदय र कल्याणका वैदिक उपायहरू:")
            data.lifePredictions.comprehensiveRemedies.fortuneRemedies.take(2).forEach {
                appendLine("• $it")
            }
            appendLine("═════════════════════════════")
            appendLine("— ग्लोडेट (GlowDate) नेपाली पात्रो तथा जन्म कुण्डली द्वारा तयार पारिएको")
        }
    }
}
