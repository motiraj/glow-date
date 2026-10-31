@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import com.example.bs_addateconverteragecalculator.ui.rashifal.RashifalType
import java.time.LocalDate
import kotlin.math.floor

data class BirthInput(
    val adYear: Int,
    val adMonth: Int,
    val adDay: Int,
    val hour: Int,
    val minute: Int,
    val latitude: Double,
    val longitude: Double,
    val timezoneOffsetHours: Double = 5.75
)

data class NatalChartProfile(
    val moonLongitude: Double,
    val sunLongitude: Double,
    val lagnaLongitude: Double,
    val janmaRashi: Int,          // 1..12
    val janmaRashiName: String,
    val nakshatra: Int,           // 1..27
    val nakshatraName: String,
    val pada: Int,                // 1..4
    val naamAkshar: String,
    val lagnaRashi: Int,          // 1..12
    val lagnaRashiName: String,
    val activeVimshottariLord: String,
    val activeYoginiName: String
)

data class PersonalizedRashifalOutput(
    val timeframe: RashifalType,
    val headlineBadge: String,         // e.g., "चन्द्रबल शुभ • सम्पत् तारा" or "बृहस्पति नवम भावमा"
    val overallScorePercent: Int,      // 1..100
    val generalSummary: String,
    val careerAndFinance: String,
    val healthAndFamily: String,
    val dashaInfluenceNote: String,
    val remedyAndLuckyTip: String
)

object MultiTimeframeRashifalEngine {

    private val RASHI_NAMES = arrayOf(
        "मेष", "वृष", "मिथुन", "कर्कट", "सिंह", "कन्या",
        "तुला", "वृश्चिक", "धनु", "मकर", "कुम्भ", "मीन"
    )

    private val NAKSHATRA_NAMES = arrayOf(
        "अश्विनी", "भरणी", "कृत्तिका", "रोहिणी", "मृगशिरा", "आर्द्रा", "पुनर्वसु", "तिष्य", "अश्लेषा",
        "मघा", "पूर्वफाल्गुनी", "उत्तरफाल्गुनी", "हस्ता", "चित्रा", "स्वाती", "विशाखा", "अनुराधा", "ज्येष्ठा",
        "मूल", "पूर्वाषाढा", "उत्तराषाढा", "श्रवण", "धनिष्ठा", "शतभिषा", "पूर्वभाद्रपदा", "उत्तरभाद्रपदा", "रेवती"
    )

    private val TARA_NAMES = arrayOf(
        "जन्म तारा (सामान्य/शारीरिक थकान)",
        "सम्पत् तारा (अति शुभ/धन लाभ)",
        "विपत् तारा (सजग रहनुहोस्/बाधा)",
        "क्षेम तारा (शुभ/कार्य सिद्धि)",
        "प्रत्यरि तारा (प्रतिकूल/विवादबाट बच्नुहोस्)",
        "साधक तारा (उत्तम/लक्ष्य प्राप्ति)",
        "वध तारा (अति प्रतिकूल/नयाँ काम थालनी नगर्नुहोस्)",
        "मित्र तारा (शुभ/सहयोग प्राप्ति)",
        "अतिमित्र तारा (परम शुभ/विशेष सफलता)"
    )

    private val VIMSHOTTARI_LORDS = arrayOf(
        "केतु" to 7.0, "शुक्र" to 20.0, "सूर्य" to 6.0, "चन्द्र" to 10.0, "मङ्गल" to 7.0,
        "राहु" to 18.0, "बृहस्पति" to 16.0, "शनि" to 19.0, "बुध" to 17.0
    )

    private val YOGINI_LORDS = arrayOf(
        "मङ्गला (चन्द्र)" to 1.0, "पिङ्गला (सूर्य)" to 2.0, "धान्या (बृहस्पति)" to 3.0,
        "भ्रामरी (मङ्गल)" to 4.0, "भद्रिका (बुध)" to 5.0, "उल्का (शनि)" to 6.0,
        "सिद्धा (शुक्र)" to 7.0, "सङ्कटा (राहु)" to 8.0
    )

    private val NAAM_AKSHARS = arrayOf(
        "चु","चे","चो","ला", "लि","लु","ले","लो", "अ","इ","उ","ए",
        "ओ","वा","वि","वु", "वे","वो","का","कि", "कु","घ","ङ","छ",
        "के","को","हा","हि", "हु","हे","हो","डा", "डि","डु","डे","डो",
        "मा","मि","मु","मे", "मो","टा","टि","टु", "टे","टो","पा","पि",
        "पु","ष","ण","ठ", "पे","पो","रा","रि", "रु","रे","रो","ता",
        "ति","तु","ते","तो", "ना","नि","नु","ने", "नो","या","यि","यु",
        "ये","यो","भा","भि", "भु","ध","फा","ढ", "भे","भो","जा","जि",
        "जु","जे","जो","खा", "खि","खु","खे","खो", "गा","गि","गु","गे",
        "गो","सा","सि","सु", "से","सो","दा","दि", "दु","थ","झ","ञ",
        "दे","दो","चा","चि"
    )

    private fun normalize(deg: Double): Double = ((deg % 360.0) + 360.0) % 360.0

    fun houseFrom(baseRashi: Int, targetRashi: Int): Int {
        return ((targetRashi - baseRashi + 12) % 12) + 1
    }

    fun getPlanetarySnapshot(date: LocalDate, tzOffset: Double = 5.75, ayanamsaMode: AyanamsaMode = AyanamsaMode.LAHIRI_DRIK): PlanetarySnapshot {
        return OfflineVedicEphemeris.getPlanetarySnapshot(
            year = date.year,
            month = date.monthValue,
            day = date.dayOfMonth,
            hour = 6,
            minute = 0,
            second = 0,
            tzOffsetHours = tzOffset,
            ayanamsaMode = ayanamsaMode
        )
    }

    fun computeNatalProfile(
        birth: BirthInput,
        birthMoonLon: Double,
        birthSunLon: Double,
        yearsElapsedSinceBirth: Double
    ): NatalChartProfile {
        val normMoon = normalize(birthMoonLon)
        val normSun = normalize(birthSunLon)

        val rashiIdx = floor(normMoon / 30.0).toInt().coerceIn(0, 11)
        val nakIdx = floor(normMoon / (40.0 / 3.0)).toInt().coerceIn(0, 26)
        val pada = floor((normMoon % (40.0 / 3.0)) / (10.0 / 3.0)).toInt().coerceIn(0, 3) + 1
        val aksharIdx = floor(normMoon / (10.0 / 3.0)).toInt().coerceIn(0, 107)

        val lagnaLon = calculateAscendant(birth, normSun)
        val lagnaIdx = floor(lagnaLon / 30.0).toInt().coerceIn(0, 11)

        val nakSpan = 40.0 / 3.0
        val remFraction = 1.0 - ((normMoon % nakSpan) / nakSpan)

        val activeVimshottari = findActiveDashaLord(
            startIdx = nakIdx % 9,
            remFraction = remFraction,
            yearsElapsed = yearsElapsedSinceBirth,
            cycleTable = VIMSHOTTARI_LORDS,
            cycleLength = 120.0
        )

        val activeYogini = findActiveDashaLord(
            startIdx = (nakIdx + 3) % 8,
            remFraction = remFraction,
            yearsElapsed = yearsElapsedSinceBirth,
            cycleTable = YOGINI_LORDS,
            cycleLength = 36.0
        )

        return NatalChartProfile(
            moonLongitude = normMoon,
            sunLongitude = normSun,
            lagnaLongitude = lagnaLon,
            janmaRashi = rashiIdx + 1,
            janmaRashiName = RASHI_NAMES[rashiIdx],
            nakshatra = nakIdx + 1,
            nakshatraName = NAKSHATRA_NAMES[nakIdx],
            pada = pada,
            naamAkshar = NAAM_AKSHARS[aksharIdx],
            lagnaRashi = lagnaIdx + 1,
            lagnaRashiName = RASHI_NAMES[lagnaIdx],
            activeVimshottariLord = activeVimshottari,
            activeYoginiName = activeYogini
        )
    }

    /**
     * 1. DAILY RASHIFAL (दैनिक राशिफल)
     */
    fun generateDailyRashifal(
        natal: NatalChartProfile,
        todayPlanets: PlanetarySnapshot
    ): PersonalizedRashifalOutput {
        val moonHouse = houseFrom(natal.janmaRashi, todayPlanets.moonRashi)
        val taraIndex = ((todayPlanets.moonNakshatra - natal.nakshatra + 27) % 9)
        val taraText = TARA_NAMES[taraIndex]
        val moonFromLagna = houseFrom(natal.lagnaRashi, todayPlanets.moonRashi)

        val transitMap = mapOf(
            "sun" to todayPlanets.sunRashi,
            "moon" to todayPlanets.moonRashi,
            "mars" to todayPlanets.marsRashi,
            "mercury" to todayPlanets.mercuryRashi,
            "jupiter" to todayPlanets.jupiterRashi,
            "venus" to todayPlanets.venusRashi,
            "saturn" to todayPlanets.saturnRashi,
            "rahu" to todayPlanets.rahuRashi,
            "ketu" to todayPlanets.ketuRashi
        )

        val specialConds = GocharaVedhaRuleEngine.evaluateSpecialConditions(natal.janmaRashi, transitMap)
        val sunVedha = GocharaVedhaRuleEngine.checkVedha("sun", natal.janmaRashi, transitMap)
        val mercVedha = GocharaVedhaRuleEngine.checkVedha("mercury", natal.janmaRashi, transitMap)

        val vedhaBadge = if (sunVedha.isObstructedByVedha || mercVedha.isObstructedByVedha) " • [वेध युक्त]" else ""
        val guruBadge = if (specialConds.hasGuruBala) " • [गुरु बल]" else ""
        val shaniBadge = if (specialConds.isSadeSati) " • [साढेसाती]" else if (specialConds.isDhaiya) " • [अढैया]" else ""

        val (rawScore, summary, career, health) = when (moonHouse) {
            1 -> Quad(85, "चन्द्रमा आज तपाईँकै जन्म राशिमा भ्रमण गर्दैछ। मन प्रसन्न रहने र व्यक्तित्वको प्रभाव बढ्ने दिन छ।",
                "नयाँ कार्य थालनी र नेतृत्वदायी भूमिकामा सफलता मिल्नेछ।", "स्वास्थ्य उत्तम रहनेछ, मिष्ठान्न भोजनको योग छ।")
            2 -> Quad(65, "द्वितीय भावको चन्द्रमाले बोलिको प्रभाव बढाउनेछ तर खर्चमा केही नियन्त्रण आवश्यक छ।",
                "पारिवारिक व्यवसाय र सञ्चित धन सम्बन्धी काममा मध्यम लाभ हुनेछ।", "आँखा वा घाँटीको सामान्य समस्या देखिन सक्छ।")
            3 -> Quad(90, "तृतीय भावको शुभ चन्द्रमाले पराक्रम, उत्साह र कार्यक्षमतामा उल्लेखनीय वृद्धि गर्नेछ।",
                "छोटो यात्रा, सञ्चार र सहकर्मीको सहयोगबाट महत्त्वपूर्ण काम बन्नेछ।", "शारीरिक स्फूर्ति उच्च रहनेछ।")
            4 -> Quad(50, "चतुर्थ भावको चन्द्रमाले मनमा केही चञ्चलता र घरायसी कार्यमा व्यस्तता बढाउन सक्छ।",
                "जग्गा-जमिन वा सवारी साधन सम्बन्धी काममा धैर्यता अपनाउनुहोला।", "छाती वा निद्रामा सामान्य गडबडी हुन सक्छ।")
            5 -> Quad(75, "पञ्चम भावको चन्द्रमाले बौद्धिक तीक्ष्णता, अध्ययन र सिर्जनात्मक कार्यमा प्रगति दिलाउनेछ।",
                "सन्तान र शैक्षिक क्षेत्रबाट शुभ समाचार प्राप्त हुनेछ।", "पाचन प्रणालीमा सामान्य ध्यान दिनुहोला।")
            6 -> Quad(88, "षष्ठ भावको बलियो चन्द्रमाले प्रतिस्पर्धीहरूलाई परास्त गर्दै रोकिएका काम पूरा गराउनेछ।",
                "ऋण मुक्ति, मुद्दा-मामिला वा प्रतिस्पर्धामा विजय प्राप्त हुने योग छ।", "पुराना रोगबाट आराम मिल्नेछ।")
            7 -> Quad(86, "सप्तम भावको चन्द्रमाले दाम्पत्य सुख, साझेदारी र सामाजिक प्रतिष्ठामा वृद्धि गर्नेछ।",
                "व्यापारिक साझेदारी र दैनिक आम्दानीमा राम्रो सुधार आउनेछ।", "मानसिक शान्ति र पारिवारिक रमाइलो रहनेछ।")
            8 -> Quad(35, "आज चन्द्रमा अष्टम भावमा (चन्द्राष्टम) रहेकोले विशेष सावधानी र धैर्यता अपनाउनुपर्ने दिन छ।",
                "ठूलो आर्थिक जोखिम वा नयाँ सम्झौता आज नगर्नु नै बेस हुनेछ।", "स्वास्थ्य र खानपानमा विशेष सतर्कता अपनाउनुहोला।")
            9 -> Quad(78, "नवम (भाग्य) भावको चन्द्रमाले धर्म, कर्म र परोपकारी कार्यमा रुचि जगाउनेछ।",
                "उच्च अधिकारी वा गुरुजनको आशीर्वादले रोकिएको काम अघि बढ्नेछ।", "स्वास्थ्य सबल रहनेछ, लामो यात्राको योग छ।")
            10 -> Quad(92, "दशम (कर्म) भावको चन्द्रमाले कार्यक्षेत्रमा उच्च सफलता, मान-सम्मान र पदोन्नतिको वातावरण बनाउनेछ।",
                "तपाईँको जन्म लग्न (${natal.lagnaRashiName}) र राशि दुवैका दृष्टिले आज कार्यसिद्धि योग छ।", "जोश र जाँगर दिनभर कायम रहनेछ।")
            11 -> Quad(95, "एकादश (आय) भावको चन्द्रमाले आकस्मिक धन लाभ, मित्र मिलन र मनोकांक्षा पूर्ण गराउनेछ।",
                "लगानी र व्यापारबाट अपेक्षाकृत उच्च प्रतिफल प्राप्त हुनेछ।", "स्वास्थ्य र पारिवारिक वातावरण अत्यन्त सुखद रहनेछ।")
            else -> Quad(45, "द्वादश (व्यय) भावको चन्द्रमाले अनावश्यक दौडधुप र आकस्मिक खर्च बढाउन सक्छ।",
                "वैदेशिक वा टाढाको काममा सफलता मिले पनि स्थानीय लेनदेनमा सजग रहनुहोला।", "शारीरिक थकान र निद्राको कमी महसुस हुन सक्छ।")
        }

        val weightedFinalScore = GocharaVedhaRuleEngine.calculateWeightedScore(
            dashaScore = 80.0,
            gocharaScore = rawScore.toDouble(),
            balaScore = if (specialConds.isAshtamaChandra) 30.0 else 85.0
        )

        val lagnaNote = if (moonFromLagna in listOf(1, 4, 7, 10, 5, 9, 11)) {
            "तपाईँको जन्म लग्न (${natal.lagnaRashiName}) बाट चन्द्रमा $moonFromLagna औँ शुभ भावमा रहेकोले मनोबल उच्च रहनेछ।"
        } else ""

        return PersonalizedRashifalOutput(
            timeframe = RashifalType.DAILY,
            headlineBadge = "गोचर चन्द्र: $moonHouse औँ भाव • $taraText$guruBadge$shaniBadge$vedhaBadge",
            overallScorePercent = weightedFinalScore,
            generalSummary = "$summary $lagnaNote",
            careerAndFinance = career,
            healthAndFamily = health,
            dashaInfluenceNote = "वर्तमानमा ${natal.activeVimshottariLord} को महादशा र ${natal.activeYoginiName} योगिनीको प्रभाव छ।",
            remedyAndLuckyTip = if (moonHouse in listOf(4, 8, 12)) {
                "आज शिवजीको स्मरण वा सेतो वस्तुको दान गर्नाले चन्द्र दोष शान्त हुनेछ।"
            } else {
                "आजको ताराबल ($taraText) अनुसार शुभ कार्यको थालनी गर्न उत्तम छ।"
            }
        )
    }

    /**
     * 2. WEEKLY RASHIFAL (साप्ताहिक राशिफल)
     */
    fun generateWeeklyRashifal(
        natal: NatalChartProfile,
        day1Planets: PlanetarySnapshot,
        day4Planets: PlanetarySnapshot,
        day7Planets: PlanetarySnapshot
    ): PersonalizedRashifalOutput {
        val hStart = houseFrom(natal.janmaRashi, day1Planets.moonRashi)
        val hMid = houseFrom(natal.janmaRashi, day4Planets.moonRashi)
        val hEnd = houseFrom(natal.janmaRashi, day7Planets.moonRashi)
        val mercHouse = houseFrom(natal.janmaRashi, day4Planets.mercuryRashi)
        val venusHouse = houseFrom(natal.janmaRashi, day4Planets.venusRashi)

        fun phaseLabel(h: Int): String = when (h) {
            8 -> "चन्द्राष्टम (विशेष सजगता आवश्यक)"
            4, 12 -> "खर्च र व्यस्तता बढ्ने"
            1, 3, 6, 7, 10, 11 -> "अत्यन्त शुभ र फलदायी"
            else -> "मध्यम र सन्तुलित"
        }

        val summary = "यो साता चन्द्रमाले तपाईँको जन्म राशि (${natal.janmaRashiName}) बाट क्रमशः $hStart, $hMid र $hEnd औँ भावमा भ्रमण गर्नेछ। " +
                "साताको प्रारम्भ (${phaseLabel(hStart)}), मध्य भाग (${phaseLabel(hMid)}) र साताको अन्त्य (${phaseLabel(hEnd)}) रहनेछ।"

        val career = if (mercHouse in listOf(2, 4, 6, 8, 10, 11)) {
            "बुध ग्रहको अनुकूल गोचर ($mercHouse औँ भाव) ले बौद्धिक, शैक्षिक र व्यापारिक निर्णयहरूमा यो साता विशेष लाभ दिलाउनेछ।"
        } else {
            "कार्यक्षेत्रमा नियमित मिहिनेतबाट काम बन्नेछ; कागजी प्रक्रिया र सम्झौता गर्दा ध्यान दिनुहोला।"
        }

        val family = if (venusHouse in listOf(1, 2, 3, 4, 5, 8, 9, 11, 12)) {
            "शुक्रको शुभ प्रभावले पारिवारिक प्रेम, दाम्पत्य समझदारी र सामाजिक जमघटमा रमाइलो वातावरण बन्नेछ।"
        } else {
            "पारिवारिक संवादमा धैर्यता राख्नुहोला र स्वास्थ्यको नियमित ख्याल गर्नुहोला।"
        }

        val avgScore = listOf(hStart, hMid, hEnd).map { if (it in listOf(1,3,6,7,10,11)) 85 else if (it == 8) 40 else 65 }.average().toInt()

        return PersonalizedRashifalOutput(
            timeframe = RashifalType.WEEKLY,
            headlineBadge = "साप्ताहिक चन्द्र गोचर: भाव $hStart → $hMid → $hEnd",
            overallScorePercent = avgScore,
            generalSummary = summary,
            careerAndFinance = career,
            healthAndFamily = family,
            dashaInfluenceNote = "${natal.activeVimshottariLord} दशा र ${natal.activeYoginiName} योगिनीको संयोगले साताको मुख्य निर्णयलाई निर्देशित गर्नेछ।",
            remedyAndLuckyTip = if (8 in listOf(hStart, hMid, hEnd)) {
                "यो साता चन्द्रमा अष्टम भावमा पर्ने दिनहरूमा ठूला आर्थिक जोखिम नलिनुहोला।"
            } else {
                "साताभरि चन्द्रबल अनुकूल रहेकाले महत्वपूर्ण भेटघाट र कार्ययोजना अघि बढाउन सक्नुहुन्छ।"
            }
        )
    }

    /**
     * 3. MONTHLY RASHIFAL (मासिक राशिफल)
     */
    fun generateMonthlyRashifal(
        natal: NatalChartProfile,
        monthPlanets: PlanetarySnapshot
    ): PersonalizedRashifalOutput {
        val sunHouse = houseFrom(natal.janmaRashi, monthPlanets.sunRashi)
        val marsHouse = houseFrom(natal.janmaRashi, monthPlanets.marsRashi)
        val venusHouse = houseFrom(natal.janmaRashi, monthPlanets.venusRashi)

        val (sunScore, sunEffect) = when (sunHouse) {
            3, 6, 10, 11 -> 90 to "यो महिना सूर्य तपाईँको राशिबाट $sunHouse औँ शुभ भावमा भ्रमण गर्दैछ, जसले पदोन्नति, सरकारी कार्यमा सफलता र नेतृत्व क्षमता वृद्धि गर्नेछ।"
            1, 2, 7, 9 -> 70 to "सूर्यको $sunHouse औँ भावको गोचरले कार्यक्षेत्रमा जिम्मेवारी थप्नेछ; धैर्यता र मिहिनेतले लक्ष्यमा पुगिनेछ।"
            else -> 55 to "सूर्य $sunHouse औँ भावमा रहेकाले यो महिना उच्च अधिकारीसँगको व्यवहार र स्वास्थ्यमा विशेष संयमता अपनाउनुहोला।"
        }

        val marsEffect = if (marsHouse in listOf(3, 6, 11)) {
            "मङ्गल ग्रह $marsHouse औँ भावमा बलियो भएकाले साहस बढ्ने, शत्रु पराजित हुने र अचल सम्पत्ति बाट लाभ मिल्ने योग छ।"
        } else {
            "मङ्गल $marsHouse औँ भावमा रहेकाले आवेशमा निर्णय नलिन र सवारी चलाउँदा सतर्क रहन सुझाव दिइन्छ।"
        }

        return PersonalizedRashifalOutput(
            timeframe = RashifalType.MONTHLY,
            headlineBadge = "मासिक सूर्य गोचर: $sunHouse औँ भाव • मङ्गल: $marsHouse औँ भाव",
            overallScorePercent = sunScore,
            generalSummary = sunEffect,
            careerAndFinance = "$marsEffect शुक्र $venusHouse औँ भावमा रहेकाले आर्थिक प्रवाह सन्तुलित रहनेछ।",
            healthAndFamily = "जन्म लग्न (${natal.lagnaRashiName}) का अनुसार यो महिना पारिवारिक दायित्व र स्वास्थ्य सन्तुलनमा ध्यान दिनु लाभदायक छ।",
            dashaInfluenceNote = "हाल चलिरहेको ${natal.activeVimshottariLord} महादशा र ${natal.activeYoginiName} योगिनीले यो महिनाका प्रयासहरूलाई स्थायित्व दिनेछन्।",
            remedyAndLuckyTip = "प्रत्येक बिहान सूर्योदयको समयमा सूर्यलाई अर्घ्य दिनाले यो महिनाको सूर्य गोचर अझ फलदायी हुनेछ।"
        )
    }

    /**
     * 4. YEARLY RASHIFAL (वार्षिक राशिफल)
     */
    fun generateYearlyRashifal(
        natal: NatalChartProfile,
        yearPlanets: PlanetarySnapshot
    ): PersonalizedRashifalOutput {
        val jupHouse = houseFrom(natal.janmaRashi, yearPlanets.jupiterRashi)
        val satHouse = houseFrom(natal.janmaRashi, yearPlanets.saturnRashi)
        val rahuHouse = houseFrom(natal.janmaRashi, yearPlanets.rahuRashi)

        val jupFavorable = jupHouse in listOf(2, 5, 7, 9, 11)
        val jupText = if (jupFavorable) {
            "यो वर्ष बृहस्पति तपाईँको जन्म राशिबाट परम शुभ $jupHouse औँ भावमा भ्रमण गर्दैछ। यसले शिक्षा, सन्तान, माङ्गलिक कार्य र भाग्योदयमा ठूलो साथ दिनेछ।"
        } else {
            "यो वर्ष बृहस्पति $jupHouse औँ भावमा रहेकोले गुरुबल मध्यम छ; अध्ययन र आर्थिक लगानीमा सुझबुझका साथ अघि बढ्दा सफलता मिल्नेछ।"
        }

        val shaniStatus = when (satHouse) {
            12 -> "शनिको साढेसाती (प्रथम चरण - द्वादश शनि) चलिरहेकोले खर्च र यात्रा बढ्न सक्छ, तर धैर्यताले दीर्घकालीन जग बसाल्नेछ।"
            1 -> "शनिको साढेसाती (मध्य चरण - जन्म राशिको शनि) प्रभावमा रहेकाले परिश्रम बढी गर्नुपर्ने र स्वास्थ्यमा ध्यान दिनुपर्ने वर्ष छ।"
            2 -> "शनिको साढेसाती (अन्तिम चरण - द्वितीय शनि) रहेकाले बोली र आर्थिक लेनदेनमा संयमता अपनाउँदा वर्षको उत्तरार्द्धमा लाभ मिल्नेछ।"
            4, 8 -> "शनिको अढैया ($satHouse औँ भाव) प्रभाव रहेकाले कार्यक्षेत्रमा धैर्यता र निरन्तरता आवश्यक छ।"
            3, 6, 11 -> "शनि ग्रह $satHouse औँ अति शुभ भावमा रहेकाले यो वर्ष ठूला उपलब्धि, पदोन्नति र शत्रुमाथि विजय प्राप्त हुने योग छ।"
            else -> "शनिको गोचर $satHouse औँ भावमा सामान्य र सन्तुलित अवस्थामा रहेको छ।"
        }

        val baseScore = (if (jupFavorable) 88 else 68) + (if (satHouse in listOf(3, 6, 11)) 10 else if (satHouse in listOf(12, 1, 2, 4, 8)) -12 else 0)

        return PersonalizedRashifalOutput(
            timeframe = RashifalType.YEARLY,
            headlineBadge = "वार्षिक बृहस्पति: $jupHouse औँ भाव • शनि: $satHouse औँ भाव",
            overallScorePercent = baseScore.coerceIn(40, 98),
            generalSummary = "$jupText $shaniStatus",
            careerAndFinance = "राहु $rahuHouse औँ भावमा र जन्म लग्न ${natal.lagnaRashiName} को संयोगले यो वर्ष प्रविधि, अनुसन्धान र दीर्घकालीन योजनामा प्रगति गराउनेछ।",
            healthAndFamily = if (jupFavorable) "बृहस्पतिको शुभ दृष्टिले घरमा माङ्गलिक कार्य र पारिवारिक सुख-शान्ति कायम रहनेछ।" else "पारिवारिक स्वास्थ्य र नियमित दिनचर्यामा ध्यान दिनुहोला।",
            dashaInfluenceNote = "यो वर्ष तपाईँलाई ${natal.activeVimshottariLord} को महादशा र ${natal.activeYoginiName} योगिनीको मुख्य प्रभाव रहनेछ।",
            remedyAndLuckyTip = if (!jupFavorable || satHouse in listOf(12, 1, 2, 4, 8)) {
                "यो वर्ष बिहीबार पहेँलो वस्तुको दान र शनिबार पीपलमा जल चढाउनाले ग्रह गोचरका अवरोधहरू शान्त भई विशेष उन्नति हुनेछ।"
            } else {
                "गुरु र शनिको अनुकूलताले यो वर्ष नयाँ परियोजना र दीर्घकालीन लगानीका लागि अत्यन्त उत्तम छ।"
            }
        )
    }

    private fun findActiveDashaLord(
        startIdx: Int,
        remFraction: Double,
        yearsElapsed: Double,
        cycleTable: Array<Pair<String, Double>>,
        cycleLength: Double
    ): String {
        var remYears = yearsElapsed % cycleLength
        val firstSpan = cycleTable[startIdx].second * remFraction
        if (remYears <= firstSpan) return cycleTable[startIdx].first
        remYears -= firstSpan

        var currIdx = (startIdx + 1) % cycleTable.size
        while (true) {
            val span = cycleTable[currIdx].second
            if (remYears <= span) return cycleTable[currIdx].first
            remYears -= span
            currIdx = (currIdx + 1) % cycleTable.size
        }
    }

    private fun calculateAscendant(birth: BirthInput, siderealSunLon: Double): Double {
        val birthDecimalHours = birth.hour + (birth.minute / 60.0)
        val stdMeridian = birth.timezoneOffsetHours * 15.0
        val localTimeCorrectionHours = (birth.longitude - stdMeridian) / 15.0
        val hoursSinceSunrise = ((birthDecimalHours + localTimeCorrectionHours - 6.0) + 24.0) % 24.0
        return normalize(siderealSunLon + (hoursSinceSunrise * 15.0))
    }

    private data class Quad(val a: Int, val b: String, val c: String, val d: String)
}
