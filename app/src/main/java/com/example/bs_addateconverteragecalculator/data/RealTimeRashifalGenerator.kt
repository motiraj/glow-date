@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import java.time.LocalDate
import java.time.LocalTime

object RealTimeRashifalGenerator {

    private fun getSignIndex(signId: String): Int {
        return when (signId) {
            "aries" -> 1
            "taurus" -> 2
            "gemini" -> 3
            "cancer" -> 4
            "leo" -> 5
            "virgo" -> 6
            "libra" -> 7
            "scorpio" -> 8
            "sagittarius" -> 9
            "capricorn" -> 10
            "aquarius" -> 11
            "pisces" -> 12
            else -> 1
        }
    }

    private fun getRashiIndex(rashiName: String): Int {
        val rashis = listOf("मेष", "वृष", "मिथुन", "कर्कट", "सिंह", "कन्या", "तुला", "वृश्चिक", "धनु", "मकर", "कुम्भ", "मीन")
        val index = rashis.indexOfFirst { rashiName.trim().startsWith(it) }
        return if (index != -1) index + 1 else 1
    }

    private fun calculateHouse(transitRashiName: String, natalSignNum: Int): Int {
        val transitSignNum = getRashiIndex(transitRashiName)
        var house = transitSignNum - natalSignNum + 1
        if (house <= 0) house += 12
        return house
    }

    fun generateDailyPrediction(signId: String, date: LocalDate, lang: String = "ne"): String {
        val natalSignNum = getSignIndex(signId)
        
        val gochar = AstroCalculator.getGochar(date, LocalTime.of(6, 0))
        
        val sunRashi = gochar.find { it.first == "सूर्य" }?.second ?: "मेष"
        val moonRashi = gochar.find { it.first == "चन्द्र" }?.second ?: "मेष"
        val marsRashi = gochar.find { it.first == "मंगल" }?.second ?: "मेष"
        val mercuryRashi = gochar.find { it.first == "बुध" }?.second ?: "मेष"
        val venusRashi = gochar.find { it.first == "शुक्र" }?.second ?: "मेष"

        val sunHouse = calculateHouse(sunRashi, natalSignNum)
        val moonHouse = calculateHouse(moonRashi, natalSignNum)
        val marsHouse = calculateHouse(marsRashi, natalSignNum)
        val mercuryHouse = calculateHouse(mercuryRashi, natalSignNum)
        val venusHouse = calculateHouse(venusRashi, natalSignNum)

        val moonText = if (lang == "en") {
            when (moonHouse) {
                1 -> "Moon in 1st house brings mental cheerfulness and high self-confidence today."
                2 -> "Moon in 2nd house advises extra caution in financial dealings."
                3 -> "Moon in 3rd house brings courage and support from friends."
                4 -> "Moon in 4th house advises focusing on domestic tasks and vehicle comfort."
                5 -> "Moon in 5th house brings intellectual growth, study success, and happiness from children."
                6 -> "Moon in 6th house overcomes enemies and improves health."
                7 -> "Moon in 7th house brings business profit and marital bliss."
                8 -> "Moon in 8th house suggests caution during unexpected travel and diet."
                9 -> "Moon in 9th house brings luck and interest in righteous deeds."
                10 -> "Moon in 10th house brings career success and high honor."
                11 -> "Moon in 11th house increases income from multiple sources."
                12 -> "Moon in 12th house may increase unnecessary expenses and travel."
                else -> "Today will yield moderate results."
            }
        } else {
            when (moonHouse) {
                1 -> "चन्द्रमा १ औं भावमा रहेकाले आज मन प्रसन्न रहनेछ र आत्मविश्वास वृद्धि हुनेछ।"
                2 -> "चन्द्रमा २ औं भावमा रहेकाले आर्थिक कारोबारमा विशेष सावधानी अपनाउनुहोला।"
                3 -> "चन्द्रमा ३ औं भावमा रहेकाले पराक्रम र मित्रहरूको सहयोग मिल्नेछ।"
                4 -> "चन्द्रमा ४ औं भावमा रहेकाले घरायसी काम र वाहन सुखमा ध्यान दिनुहोला।"
                5 -> "चन्द्रमा ५ औं भावमा रहेकाले बौद्धिक कार्य, अध्ययन तथा संतान सुख प्राप्त हुनेछ।"
                6 -> "चन्द्रमा ६ औं भावमा रहेकाले शत्रु परास्त हुनेछन् र स्वास्थ्यमा सुधार आउनेछ।"
                7 -> "चन्द्रमा ७ औं भावमा रहेकाले व्यापारमा लाभ र दाम्पत्य जीवन सुखमय रहनेछ।"
                8 -> "चन्द्रमा ८ औं भावमा रहेकाले आकस्मिक यात्रा र खानपानमा सतर्क रहनुहोला।"
                9 -> "चन्द्रमा ९ औं भावमा रहेकाले भाग्यले साथ दिनेछ र धार्मिक कार्यमा रुचि बढ्नेछ।"
                10 -> "चन्द्रमा १० औं भावमा रहेकाले कर्मक्षेत्रमा सफलता र उच्च सम्मान मिल्नेछ।"
                11 -> "चन्द्रमा ११ औं भावमा रहेकाले विभिन्न स्रोतबाट आयआर्जन बढ्नेछ।"
                12 -> "चन्द्रमा १२ औं भावमा रहेकाले अनावश्यक खर्च र भागदौड बढ्न सक्छ।"
                else -> "आजको दिन सामान्य फलदायी रहनेछ।"
            }
        }

        val sunText = if (sunHouse in listOf(3, 6, 10, 11)) {
            if (lang == "en") " Favorable Sun transit grants administrative success." else " सूर्यको शुभ गोचरले प्रशासनिक तथा सरकारी काममा सफलता दिलाउनेछ।"
        } else ""

        val marsText = if (marsHouse in listOf(3, 6, 10, 11)) {
            if (lang == "en") " Mars alignment boosts energy and courage." else " मंगलको अनुकूलताले ऊर्जा र पराक्रममा वृद्धि गर्नेछ।"
        } else ""

        val mercuryText = if (mercuryHouse in listOf(2, 4, 6, 8, 10, 11)) {
            if (lang == "en") " Mercury transit benefits communication and business decisions." else " बुधको गोचरले सञ्चार, व्यापार र बौद्धिक निर्णयमा लाभ गराउनेछ।"
        } else ""

        val venusText = if (venusHouse in listOf(1, 2, 3, 4, 5, 8, 9, 11, 12)) {
            if (lang == "en") " Venus influence enhances family joy and comfort." else " शुक्रको प्रभावले पारिवारिक सुख र आनन्दको वातावरण बन्नेछ।"
        } else ""

        return "$moonText$sunText$marsText$mercuryText$venusText"
    }

    fun generateWeeklyPrediction(signId: String, date: LocalDate, lang: String = "ne"): String {
        val natalSignNum = getSignIndex(signId)
        val gochar = AstroCalculator.getGochar(date, LocalTime.of(6, 0))
        val moonRashi = gochar.find { it.first == "चन्द्र" }?.second ?: "मेष"
        val mercuryRashi = gochar.find { it.first == "बुध" }?.second ?: "मेष"
        val venusRashi = gochar.find { it.first == "शुक्र" }?.second ?: "मेष"

        val moonHouse = calculateHouse(moonRashi, natalSignNum)
        val mercuryHouse = calculateHouse(mercuryRashi, natalSignNum)
        val venusHouse = calculateHouse(venusRashi, natalSignNum)

        val moonText = if (lang == "en") {
            "With Moon transiting house $moonHouse this week, maintain emotional balance in key decisions."
        } else {
            "यस साता चन्द्रमाको गोचर भाव $moonHouse मा रहेकाले मानसिक निर्णयहरूमा सन्तुलन राख्नुहोला।"
        }

        val mercuryText = if (mercuryHouse in listOf(2, 4, 6, 8, 10, 11)) {
            if (lang == "en") " Mercury boosts commerce and intellectual work." else " बुधको प्रभावले सञ्चार, व्यापार तथा बौद्धिक क्षेत्रमा लाभ मिल्नेछ।"
        } else ""

        val venusText = if (venusHouse in listOf(1, 2, 3, 4, 5, 8, 9, 11, 12)) {
            if (lang == "en") " Venus fosters love and family harmony." else " शुक्रको गोचरले प्रेम सम्बन्ध तथा पारिवारिक सौहार्दता बढाउनेछ।"
        } else ""

        return "$moonText$mercuryText$venusText"
    }

    fun generateMonthlyPrediction(signId: String, year: Int, month: Int, lang: String = "ne"): String {
        val natalSignNum = getSignIndex(signId)
        val date = LocalDate.now()
        val gochar = AstroCalculator.getGochar(date, LocalTime.of(6, 0))

        val sunRashi = gochar.find { it.first == "सूर्य" }?.second ?: "मेष"
        val jupiterRashi = gochar.find { it.first == "बृहस्पति" }?.second ?: "मेष"
        val saturnRashi = gochar.find { it.first == "शनि" }?.second ?: "मकर"

        val sunHouse = calculateHouse(sunRashi, natalSignNum)
        val jupiterHouse = calculateHouse(jupiterRashi, natalSignNum)
        val saturnHouse = calculateHouse(saturnRashi, natalSignNum)

        val sunMonthlyText = if (sunHouse in listOf(3, 6, 10, 11)) {
            if (lang == "en") "Sun in house $sunHouse this month brings career advancement, prestige, and new opportunities." else "यस महिना सूर्य $sunHouse औं भावमा रहेकाले करियरमा उन्नति, मान-प्रतिष्ठा र नयाँ अवसर प्राप्त हुनेछन्।"
        } else {
            if (lang == "en") "Sun transit requires careful execution and health focus this month." else "यस महिना सूर्यको गोचर अनुकूल नरहेकाले कार्यक्षेत्रमा सावधानी र स्वास्थ्यमा ध्यान दिनुहोला।"
        }

        val jupiterText = if (jupiterHouse in listOf(2, 5, 7, 9, 11)) {
            if (lang == "en") " Additionally, Jupiter in house $jupiterHouse brings wealth, wisdom, and family peace." else " साथै देवगुरु बृहस्पति $jupiterHouse औं भावमा शुभ रहेकाले धनलाभ, ज्ञानवृद्धि र परिवारमा सुखशान्ति छाउनेछ।"
        } else {
            if (lang == "en") " Jupiter in house $jupiterHouse advises thoughtfulness in major investments." else " बृहस्पति $jupiterHouse औं भावमा रहेकाले ठूला निर्णयहरू गर्दा विचार पुर्‍याउनुहोला।"
        }

        val saturnText = when (saturnHouse) {
            12, 1, 2 -> if (lang == "en") " Saturn's Sade Sati phase requires hard work and patience." else " शनिको साढेसाती प्रभावले गर्दा कडा परिश्रम र धैर्यता आवश्यक छ।"
            3, 6, 11 -> if (lang == "en") " Favorable Saturn transit brings lasting gains and completes pending tasks." else " शनिको शुभ गोचरले रोकिएका कामहरू बन्ने र स्थायी लाभ हुनेछ।"
            else -> if (lang == "en") " Saturn provides balanced and steady progress." else " शनिको गोचरले मध्यम फल प्रदान गर्नेछ।"
        }

        return "$sunMonthlyText $jupiterText$saturnText"
    }

    fun generateYearlyPrediction(signId: String, year: Int, lang: String = "ne"): String {
        val natalSignNum = getSignIndex(signId)
        val date = LocalDate.of(year, 1, 1)
        val gochar = AstroCalculator.getGochar(date, LocalTime.of(6, 0))

        val jupiterRashi = gochar.find { it.first == "बृहस्पति" }?.second ?: "मेष"
        val saturnRashi = gochar.find { it.first == "शनि" }?.second ?: "मकर"
        val rahuRashi = gochar.find { it.first == "राहु" }?.second ?: "मीन"

        val jupiterHouse = calculateHouse(jupiterRashi, natalSignNum)
        val saturnHouse = calculateHouse(saturnRashi, natalSignNum)
        val rahuHouse = calculateHouse(rahuRashi, natalSignNum)

        val guruText = if (jupiterHouse in listOf(2, 5, 7, 9, 11)) {
            if (lang == "en") "Jupiter's transit in house $jupiterHouse this year grants spiritual growth, financial prosperity, and business success." else "यस वर्ष देवगुरु बृहस्पतिको गोचर तपाईंको जन्म राशिबाट $jupiterHouse औं भावमा रहनेछ, जसले गर्दा धार्मिक कार्यमा रुचि, आर्थिक उन्नति, व्यापारमा ठूलो सफलता र परिवारमा मांगलिक कार्य सम्पन्न हुनेछ।"
        } else {
            if (lang == "en") "Jupiter in house $jupiterHouse advises caution in health and financial ventures." else "यस वर्ष बृहस्पतिको गोचर $jupiterHouse औं भावमा रहने हुनाले स्वास्थ्य र आर्थिक लगानीमा विशेष सावधानी अपनाउनु पर्नेछ।"
        }

        val shaniText = when (saturnHouse) {
            12, 1, 2 -> if (lang == "en") " Saturn's Sade Sati influence emphasizes perseverance in career." else " शनिको साढेसाती वा ढैयाको प्रभावले गर्दा करियर तथा व्यवसायमा केही संघर्ष र धैर्यको आवश्यकता पर्नेछ।"
            3, 6, 11 -> if (lang == "en") " Saturn's favorable transit overcomes obstacles and builds permanent assets." else " शनिको शुभ प्रभावले शत्रु परास्त हुनेछन्, शत्रु पक्षमा विजय र स्थायी सम्पत्ति जोड्ने सुअवसर प्राप्त हुनेछ।"
            else -> if (lang == "en") " Saturn's blessings reward continuous effort." else " शनिदेवको कृपाले कर्मक्षेत्रमा निरन्तर मेहनतको फल प्राप्त हुनेछ।"
        }

        val rahuText = if (rahuHouse in listOf(3, 6, 11)) {
            if (lang == "en") " Rahu's transit brings unexpected success and foreign travel." else " राहुको अनुकूल गोचरले अप्रत्याशित सफलता र विदेश यात्राका योग जुराउनेछ।"
        } else ""

        val closing = if (lang == "en") " Overall, this year will be filled with dedication and significant achievements." else " समग्रमा यो वर्ष तपाईंको लागि कडा परिश्रम र महत्वपूर्ण उपलब्धियोंले भरिपूर्ण रहनेछ।"

        return "$guruText$shaniText$rahuText$closing"
    }
}
