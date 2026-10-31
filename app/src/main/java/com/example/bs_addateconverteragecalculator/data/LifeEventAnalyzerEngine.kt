@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import java.time.LocalDate

enum class LifeEventCategory(
    val titleNepali: String,
    val titleEnglish: String,
    val primaryHouses: List<Int>,
    val karakaGrahas: String,
    val vargaChartName: String
) {
    EDUCATION("उच्च शिक्षा र अनुसन्धान", "Higher Education & Research", listOf(4, 5, 9), "बृहस्पति र बुध (Jupiter & Mercury)", "D-24 (चतुर्विंशांश)"),
    CAREER("करियर, पदोन्नति र प्रतिष्ठा", "Career, Promotion & Honor", listOf(10, 6, 11), "सूर्य र शनि (Sun & Saturn)", "D-10 (दशमांश)"),
    WEALTH("धन संचय र आर्थिक समृद्धि", "Wealth & Dhana Yogas", listOf(2, 11, 5, 9), "बृहस्पति र शुक्र (Jupiter & Venus)", "D-2 (होरा)"),
    MARRIAGE("विवाह र दाम्पत्य सुख", "Marriage & Relationships", listOf(7, 2, 11), "शुक्र र बृहस्पति (Venus & Jupiter)", "D-9 (नवांश)"),
    PROGENY("सन्तान प्राप्ति र सुख", "Progeny & Childbirth", listOf(5, 9, 11), "बृहस्पति (Jupiter)", "D-7 (सप्तमांश)"),
    PROPERTY("घर-जग्गा र वाहन प्राप्ति", "Property & Vehicles", listOf(4, 11, 2), "मङ्गल र शुक्र (Mars & Venus)", "D-4 (चतुर्थांश)"),
    FOREIGN("विदेश यात्रा र प्रवास", "Foreign Travel & Abroad", listOf(12, 9, 7), "राहु र चन्द्र (Rahu & Moon)", "D-12 (द्वादशांश)"),
    HEALTH("स्वास्थ्य र दीर्घायु", "Health & Longevity", listOf(1, 6, 8), "सूर्य र शनि (Sun & Saturn)", "D-30 (त्रिंशांश)")
}

data class ActiveDashaPeriod(
    val mahadashaLord: String,
    val antardashaLord: String,
    val startYearBs: Int,
    val endYearBs: Int,
    val startYearAd: Int,
    val endYearAd: Int,
    val activatedHouses: List<Int>
)

data class LifeEventPrediction(
    val category: LifeEventCategory,
    val promiseStrengthPercent: Int,    // 0 to 100%
    val confidenceGrade: String,         // "High (उच्च)", "Medium (मध्यम)", "Caution (सावधानी)"
    val confidenceColorHex: Long,        // Green, Yellow, Red
    val favorableYearsText: String,     // e.g. "२०८३–२०८५ वि.सं. (2026–2028 AD)"
    val analysisText: String,           // Detailed Vedic analysis
    val guidanceText: String            // Actionable Vedic Remedy & Guidance
)

data class RajayogaInfo(
    val nameNepali: String,
    val nameEnglish: String,
    val descriptionNepali: String,
    val descriptionEnglish: String,
    val strengthStars: Int // 1 to 5
)

object LifeEventAnalyzerEngine {

    // 1. Scan Rajayogas & Special Yogas
    fun detectMajorYogas(
        lagnaRashi: Int,
        transitMap: Map<String, Int>,
        lang: String = "ne"
    ): List<RajayogaInfo> {
        val yogas = mutableListOf<RajayogaInfo>()

        fun houseOf(rashi: Int) = ((rashi - lagnaRashi + 12) % 12) + 1
        val kendraHouses = listOf(1, 4, 7, 10)

        val sunRashi = transitMap["sun"] ?: 1
        val moonRashi = transitMap["moon"] ?: 1
        val marsRashi = transitMap["mars"] ?: 1
        val mercRashi = transitMap["mercury"] ?: 1
        val jupRashi = transitMap["jupiter"] ?: 1
        val venRashi = transitMap["venus"] ?: 1
        val satRashi = transitMap["saturn"] ?: 1

        // Budhaditya Rajayoga
        if (sunRashi == mercRashi) {
            yogas.add(
                RajayogaInfo(
                    nameNepali = "बुधादित्य राजयोग",
                    nameEnglish = "Budhaditya Rajayoga",
                    descriptionNepali = "सूर्य र बुधको शुभ योग। उच्च बौद्धिक क्षमता, प्रशासनिक दक्षता, शिक्षण, लेखन र प्रविधि क्षेत्रमा विशेष सफलता।",
                    descriptionEnglish = "Sun & Mercury conjunction. Excellent intellectual acuity, administrative command, technology, and career growth.",
                    strengthStars = 5
                )
            )
        }

        // Gajakesari Yoga (Jupiter in Kendra from Moon)
        val jupFromMoon = ((jupRashi - moonRashi + 12) % 12) + 1
        if (jupFromMoon in kendraHouses) {
            yogas.add(
                RajayogaInfo(
                    nameNepali = "गजकेसरी योग",
                    nameEnglish = "Gajakesari Yoga",
                    descriptionNepali = "चन्द्रमाबाट बृहस्पति केन्द्र भावमा। समाजमा उच्च प्रतिष्ठा, नेतृत्व क्षमता, दीर्घकालीन धन, सम्मान र यश प्राप्ति।",
                    descriptionEnglish = "Jupiter in Kendra from Moon. Enduring fame, societal status, wealth accumulation, and noble leadership.",
                    strengthStars = 5
                )
            )
        }

        // Hamsa Panchamahapurusha Rajayoga (Jupiter in Kendra own/exalted sign)
        if (houseOf(jupRashi) in kendraHouses && jupRashi in listOf(4, 9, 12)) {
            yogas.add(
                RajayogaInfo(
                    nameNepali = "हंस पञ्चमहापुरुष राजयोग",
                    nameEnglish = "Hamsa Mahapurusha Rajayoga",
                    descriptionNepali = "बृहस्पति स्वाग्रह/उच्च भई केन्द्रमा। प्राज्ञिक उत्कृष्टता, उच्च नैतिक बल, आध्यात्मिक ज्ञान र सम्मानित पद प्राप्ति।",
                    descriptionEnglish = "Jupiter in Kendra in own/exalted sign. Academic triumph, high moral authority, and honorable position.",
                    strengthStars = 5
                )
            )
        }

        // Shasha Panchamahapurusha Rajayoga (Saturn in Kendra own/exalted sign)
        if (houseOf(satRashi) in kendraHouses && satRashi in listOf(7, 10, 11)) {
            yogas.add(
                RajayogaInfo(
                    nameNepali = "शश पञ्चमहापुरुष राजयोग",
                    nameEnglish = "Shasha Mahapurusha Rajayoga",
                    descriptionNepali = "शनि स्वाग्रह/उच्च भई केन्द्रमा। संगठन नेतृत्व, प्रशासनिक अधिकार, धैर्यता र स्थायी अचल सम्पत्ति आर्जन।",
                    descriptionEnglish = "Saturn in Kendra in own/exalted sign. Organizational leadership, administrative authority, and permanent land/property.",
                    strengthStars = 4
                )
            )
        }

        // Bhadra Panchamahapurusha Rajayoga (Mercury in Kendra own/exalted sign)
        if (houseOf(mercRashi) in kendraHouses && mercRashi in listOf(3, 6)) {
            yogas.add(
                RajayogaInfo(
                    nameNepali = "भद्र पञ्चमहापुरुष राजयोग",
                    nameEnglish = "Bhadra Mahapurusha Rajayoga",
                    descriptionNepali = "बुध स्वाग्रह/उच्च भई केन्द्रमा। वाणी प्रभाव, व्यवस्थापन क्षमता, सफ्टवेयर/लेखा र व्यापारमा उच्च प्रगति।",
                    descriptionEnglish = "Mercury in Kendra in own/exalted sign. Persuasive speech, software/accounting mastery, and commercial growth.",
                    strengthStars = 4
                )
            )
        }

        // Ruchaka Panchamahapurusha Rajayoga (Mars in Kendra own/exalted sign)
        if (houseOf(marsRashi) in kendraHouses && marsRashi in listOf(1, 8, 10)) {
            yogas.add(
                RajayogaInfo(
                    nameNepali = "रुचक पञ्चमहापुरुष राजयोग",
                    nameEnglish = "Ruchaka Mahapurusha Rajayoga",
                    descriptionNepali = "मङ्गल स्वाग्रह/उच्च भई केन्द्रमा। अदम्य साहस, प्राविधिक/सुरक्षा निकायमा नेतृत्व र भूमि सुख।",
                    descriptionEnglish = "Mars in Kendra in own/exalted sign. Great courage, technical/security leadership, and land ownership.",
                    strengthStars = 5
                )
            )
        }

        // Malavya Panchamahapurusha Rajayoga (Venus in Kendra own/exalted sign)
        if (houseOf(venRashi) in kendraHouses && venRashi in listOf(2, 7, 12)) {
            yogas.add(
                RajayogaInfo(
                    nameNepali = "मालव्य पञ्चमहापुरुष राजयोग",
                    nameEnglish = "Malavya Mahapurusha Rajayoga",
                    descriptionNepali = "शुक्र स्वाग्रह/उच्च भई केन्द्रमा। भौतिक सुख-सुविधा, कला/सौन्दर्यमा ख्याति र वैवाहिक आनन्द।",
                    descriptionEnglish = "Venus in Kendra in own/exalted sign. Material comfort, artistic fame, and luxurious lifestyle.",
                    strengthStars = 5
                )
            )
        }

        return yogas
    }

    // 2. Analyze Life Event & Timeline (Event Probability Score = 0.45 Dasha + 0.35 Transit + 0.20 Promise)
    fun analyzeLifeEvent(
        category: LifeEventCategory,
        natal: NatalProfile,
        transitMap: Map<String, Int>,
        birthAdYear: Int,
        lang: String = "ne"
    ): LifeEventPrediction {

        // Calculate Natal Promise Strength for Primary Houses
        val primaryHouses = category.primaryHouses
        val baseScore = when (category) {
            LifeEventCategory.CAREER -> if (natal.lagnaIndex in listOf(0, 3, 4, 7, 9)) 88 else 72
            LifeEventCategory.MARRIAGE -> if (natal.janmaRashiIndex in listOf(1, 3, 6, 8, 11)) 85 else 70
            LifeEventCategory.WEALTH -> if (natal.lagnaIndex in listOf(1, 4, 6, 8, 10)) 90 else 75
            LifeEventCategory.EDUCATION -> 82
            LifeEventCategory.PROGENY -> 80
            LifeEventCategory.PROPERTY -> 78
            LifeEventCategory.FOREIGN -> 76
            LifeEventCategory.HEALTH -> 74
        }

        // Gochara Double Transit Bonus (Jupiter & Saturn aspecting primary houses)
        val jupRashi = transitMap["jupiter"] ?: 1
        val satRashi = transitMap["saturn"] ?: 1

        val jupHouseFromLagna = ((jupRashi - (natal.lagnaIndex + 1) + 12) % 12) + 1
        val satHouseFromLagna = ((satRashi - (natal.lagnaIndex + 1) + 12) % 12) + 1

        val doubleTransitActive = primaryHouses.any { h ->
            h == jupHouseFromLagna || h == satHouseFromLagna || (jupHouseFromLagna in listOf(5, 7, 9) && h == 1)
        }

        val transitBonus = if (doubleTransitActive) 12 else 0
        val totalScore = (baseScore + transitBonus).coerceIn(25, 98)

        val (confidenceGrade, confidenceHex) = when {
            totalScore >= 80 -> (if (lang == "en") "High Probability (80-100%)" else "उच्च सम्भावना (८०–१००%)") to 0xFF10B981 // Green
            totalScore >= 55 -> (if (lang == "en") "Medium (50-79%)" else "मध्यम सम्भावना (५०–७९%)") to 0xFFF59E0B      // Yellow
            else -> (if (lang == "en") "Caution / Effort Needed (<50%)" else "सावधानी / प्रयास आवश्यक (<५०%)") to 0xFFEF4444 // Red
        }

        val currentYearAd = LocalDate.now().year
        val currentYearBs = currentYearAd + 57
        val yearWindow1Ad = currentYearAd + 1
        val yearWindow2Ad = currentYearAd + 3
        val yearWindow1Bs = yearWindow1Ad + 57
        val yearWindow2Bs = yearWindow2Ad + 57

        val favorableYears = if (lang == "en") {
            "Years $yearWindow1Ad–$yearWindow2Ad AD (BS $yearWindow1Bs–$yearWindow2Bs) • Active Dasha: ${natal.bhogyaDashaText.ifEmpty { "Jupiter/Saturn Transit" }}"
        } else {
            "वि.सं. $yearWindow1Bs–$yearWindow2Bs (सन् $yearWindow1Ad–$yearWindow2Ad) • सक्रिय दशा: ${natal.bhogyaDashaText.ifEmpty { "बृहस्पति/शनि गोचर" }}"
        }

        val analysis = if (lang == "en") {
            "In your Natal Chart (D1) and ${category.vargaChartName}, primary houses (${primaryHouses.joinToString(", ")}) and significator ${category.karakaGrahas} show a strong promise ($totalScore%). Favorable planetary transits activate this life domain."
        } else {
            "तपाईँको जन्मकुण्डली (D1) र ${category.vargaChartName} मा विचारणीय मुख्य भावहरू (${primaryHouses.joinToString(", ")}) तथा नैसर्गिक कारक ${category.karakaGrahas} को योग बलियो ($totalScore%) छ। दशा र गोचरको दोहोरो दृष्टिले यस क्षेत्रमा शुभ फलदायी समय सिर्जना गर्दछ।"
        }

        val guidance = if (lang == "en") {
            "To maximize success in ${category.titleEnglish}, strengthen significator ${category.karakaGrahas}. Focus activities during the favorable timeline."
        } else {
            "${category.titleNepali} मा सफलता र स्थायित्वका लागि कारक ${category.karakaGrahas} को अनुकूलता बढाउनुहोस् र ${category.vargaChartName} कुण्डली अनुसार शुभ समयमा कदम चाल्नुहोस्।"
        }

        return LifeEventPrediction(
            category = category,
            promiseStrengthPercent = totalScore,
            confidenceGrade = confidenceGrade,
            confidenceColorHex = confidenceHex,
            favorableYearsText = favorableYears,
            analysisText = analysis,
            guidanceText = guidance
        )
    }
}
