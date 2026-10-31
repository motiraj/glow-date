@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import kotlin.math.abs

enum class Graha(
    val nepaliName: String,
    val exaltedRashi: Int,
    val exaltedDeg: Double,
    val debilitatedRashi: Int,
    val ownRashis: List<Int>,
    val combustionLimitDeg: Double,
    val vedicMantra: String,
    val donationItem: String,
    val gemstone: String
) {
    SUN("सूर्य", 1, 10.0, 7, listOf(5), 0.0, "ॐ घृणि सूर्याय नमः", "गहुँ, सक्खर र तामा", "माणिक्य (Ruby)"),
    MOON("चन्द्र", 2, 3.0, 8, listOf(4), 12.0, "ॐ सों सोमाय नमः", "चामल, दूध र चाँदी", "मोती (Pearl)"),
    MARS("मङ्गल", 10, 28.0, 4, listOf(1, 8), 17.0, "ॐ अं अङ्गारकाय नमः", "रातो मुसुरो दाल र सखर", "मुगा (Red Coral)"),
    MERCURY("बुध", 6, 15.0, 12, listOf(3, 6), 14.0, "ॐ बुं बुधाय नमः", "हरियो मुँग र शैक्षिक सामग्री", "पन्ना (Emerald)"),
    JUPITER("बृहस्पति", 4, 5.0, 10, listOf(9, 12), 11.0, "ॐ बृं बृहस्पतये नमः", "चनाको दाल, बेसार र पहेँलो वस्त्र", "पुष्पराज (Yellow Sapphire)"),
    VENUS("शुक्र", 12, 27.0, 6, listOf(2, 7), 10.0, "ॐ शुं शुक्राय नमः", "दही, घिउ र सेतो वस्त्र", "हीरा वा ओपल (Opal)"),
    SATURN("शनि", 7, 20.0, 1, listOf(10, 11), 15.0, "ॐ शं शनैश्चराय नमः", "कालो तिल, तोरीको तेल र फलाम", "नीलम (Blue Sapphire)"),
    RAHU("राहु", 2, 20.0, 8, emptyList(), 0.0, "ॐ रां राहवे नमः", "कालो कम्बल, मूला र सप्तधान्य", "गोमेद (Hessonite)"),
    KETU("केतु", 8, 20.0, 2, emptyList(), 0.0, "ॐ कें केतवे नमः", "दुई रङ्गी कम्बल र तिल", "लहसुनिया (Cat's Eye)")
}

data class GrahaAnalysisResult(
    val graha: Graha,
    val houseNumber: Int,
    val dignityNepali: String,      // उच्च, स्वगृही, नीच, सामान्य
    val avasthaNepali: String,      // बाल, कुमार, युवा, वृद्ध, मृत
    val isCombust: Boolean,         // अस्त अवस्था
    val isFunctionalBenefic: Boolean,// योगकारक/शुभ भावेश
    val strengthPercent: Int,       // 0 - 100%
    val impactSummaryNepali: String,
    val remedyRecommendationNepali: String
)

data class KundaliDoshaResult(
    val isMangalik: Boolean,
    val isGrahanYoga: Boolean,
    val isGuruChandal: Boolean,
    val isVishYoga: Boolean,
    val isKemadruma: Boolean,
    val activeDoshasSummary: List<String>
)

object GrahaImpactAndRemedyEngine {

    fun analyzePlanet(
        graha: Graha,
        planetLongitude: Double,
        sunLongitude: Double,
        lagnaRashi: Int
    ): GrahaAnalysisResult {
        val rashi = (planetLongitude / 30.0).toInt() + 1
        val degInRashi = planetLongitude % 30.0
        val house = ((rashi - lagnaRashi + 12) % 12) + 1

        // १. राशिगत अवस्था (Dignity)
        val dignity = when {
            rashi == graha.exaltedRashi -> "उच्च (Exalted)"
            rashi == graha.debilitatedRashi -> "नीच (Debilitated)"
            graha.ownRashis.contains(rashi) -> "स्वगृही (Own Sign)"
            else -> "सामान्य (Neutral/Friend)"
        }

        // २. बाल्यादि अंश अवस्था (Degree Avastha)
        val isOddRashi = rashi % 2 != 0
        val (avastha, degWeight) = getBalyadiAvastha(degInRashi, isOddRashi)

        // ३. अस्त परीक्षण (Combustion Check)
        val angularDiff = abs(planetLongitude - sunLongitude).let { if (it > 180) 360 - it else it }
        val isCombust = graha != Graha.SUN && graha != Graha.RAHU && graha != Graha.KETU &&
                angularDiff <= graha.combustionLimitDeg

        // ४. योगकारक / शुभ भावेश परीक्षण (1, 5, 9 र केन्द्र स्वामित्व)
        val ownedHouses = graha.ownRashis.map { ((it - lagnaRashi + 12) % 12) + 1 }
        val isTrikonaLord = ownedHouses.any { it in listOf(1, 5, 9) }
        val isDusthanaLord = ownedHouses.any { it in listOf(6, 8, 12) }
        val isFunctionalBenefic = isTrikonaLord && !isDusthanaLord

        // ५. कुल बल प्रतिशत गणना
        var score = degWeight
        if (rashi == graha.exaltedRashi) score = (score + 40).coerceAtMost(100)
        if (graha.ownRashis.contains(rashi)) score = (score + 30).coerceAtMost(100)
        if (rashi == graha.debilitatedRashi) score = (score - 40).coerceAtLeast(10)
        if (isCombust) score = (score - 35).coerceAtLeast(5)
        if (house in listOf(6, 8, 12)) score = (score - 20).coerceAtLeast(10)

        // ६. प्रभाव र निराकरण निर्णय
        val impact = buildImpactText(graha, house, dignity, avastha, isCombust)
        val remedy = buildRemedyText(graha, house, rashi == graha.debilitatedRashi, isCombust, isFunctionalBenefic, score)

        return GrahaAnalysisResult(
            graha = graha,
            houseNumber = house,
            dignityNepali = dignity,
            avasthaNepali = avastha,
            isCombust = isCombust,
            isFunctionalBenefic = isFunctionalBenefic,
            strengthPercent = score,
            impactSummaryNepali = impact,
            remedyRecommendationNepali = remedy
        )
    }

    private fun getBalyadiAvastha(deg: Double, isOdd: Boolean): Pair<String, Int> {
        val slot = (deg / 6.0).toInt().coerceIn(0, 4)
        val effectiveSlot = if (isOdd) slot else (4 - slot)
        return when (effectiveSlot) {
            0 -> "बाल अवस्था" to 40
            1 -> "कुमार अवस्था" to 70
            2 -> "युवा अवस्था (पूर्ण बली)" to 100
            3 -> "वृद्ध अवस्था" to 25
            else -> "मृत अवस्था (अति कमजोर)" to 10
        }
    }

    private fun buildImpactText(
        graha: Graha, house: Int, dignity: String, avastha: String, isCombust: Boolean
    ): String {
        val status = if (isCombust) "सूर्यबाट अस्त (तेजहीन)" else dignity
        return "${graha.nepaliName} कुण्डलीको $house औं भावमा $status र $avastha मा रहेको छ। " +
                when (house) {
                    1, 4, 5, 7, 9, 10, 11 -> "यो शुभ स्थानमा भए तापनि अवस्था र बल अनुसार फल प्राप्त हुनेछ।"
                    6, 8, 12 -> "यो त्रिक (दुःस्थान) भावमा रहेकाले स्वास्थ्य, खर्च वा कार्यमा केही संघर्ष र ढिलाइ गराउन सक्छ।"
                    else -> "यसले धन, वाणी र पराक्रमको क्षेत्रमा मध्यम प्रभाव पार्दछ।"
                }
    }

    private fun buildRemedyText(
        graha: Graha, house: Int, isDebilitated: Boolean, isCombust: Boolean, isBenefic: Boolean, score: Int
    ): String {
        return when {
            // नियम १: योगकारक ग्रह कमजोर वा अस्त भए रत्न + मन्त्र
            isBenefic && (score < 60 || isCombust) && !isDebilitated ->
                "योगकारक ${graha.nepaliName} को बल बढाउन '${graha.gemstone}' धारण गर्नु र नित्य '${graha.vedicMantra}' जाप गर्नु उत्तम हुन्छ।"
            // नियम २: नीच, ६/८/१२ भाव वा अशुभ ग्रह भए दान र मन्त्र मात्र (रत्न निषेध)
            isDebilitated || house in listOf(6, 8, 12) || !isBenefic ->
                "अशुभ प्रभाव र दोष शान्तिका लागि '${graha.vedicMantra}' जाप गर्नुहोस् र '${graha.donationItem}' दान गर्नुहोस्। (रत्न धारण नगर्नुहोला)।"
            else ->
                "${graha.nepaliName} सबल अवस्थामा छ। शुभत्व कायम राख्न इष्टदेवको स्मरण र '${graha.vedicMantra}' पाठ गर्न सकिन्छ।"
        }
    }

    /**
     * Detects Major Kundali Doshas (Mangalik, Grahan, Guru Chandal, Vish Yoga, Kemadruma)
     */
    fun detectKundaliDoshas(
        lagnaRashi: Int,
        moonRashi: Int,
        marsRashi: Int,
        sunRashi: Int,
        jupiterRashi: Int,
        saturnRashi: Int,
        rahuRashi: Int,
        ketuRashi: Int
    ): KundaliDoshaResult {
        val marsHouseFromLagna = ((marsRashi - lagnaRashi + 12) % 12) + 1
        val marsHouseFromMoon = ((marsRashi - moonRashi + 12) % 12) + 1

        val isMangalik = (marsHouseFromLagna in listOf(1, 4, 7, 8, 12)) || (marsHouseFromMoon in listOf(1, 4, 7, 8, 12))

        val isGrahanYoga = (sunRashi == rahuRashi || sunRashi == ketuRashi || moonRashi == rahuRashi || moonRashi == ketuRashi)
        val isGuruChandal = (jupiterRashi == rahuRashi || jupiterRashi == ketuRashi)
        val isVishYoga = (saturnRashi == moonRashi || ((saturnRashi - moonRashi + 12) % 12) + 1 == 7)

        val nextHouseRashi = ((moonRashi % 12) + 1)
        val prevHouseRashi = if (moonRashi == 1) 12 else moonRashi - 1

        val otherPlanets = listOf(marsRashi, sunRashi, jupiterRashi, saturnRashi)
        val hasPlanetIn2ndOr12th = otherPlanets.any { it == nextHouseRashi || it == prevHouseRashi }
        val isKemadruma = !hasPlanetIn2ndOr12th

        val activeDoshas = mutableListOf<String>()
        if (isMangalik) activeDoshas.add("माङ्गलिक दोष (Mangalik Dosha)")
        if (isGrahanYoga) activeDoshas.add("ग्रहण योग (Grahan Yoga)")
        if (isGuruChandal) activeDoshas.add("गुरु-चाण्डाल योग (Guru Chandal Yoga)")
        if (isVishYoga) activeDoshas.add("विष योग (Vish Yoga)")
        if (isKemadruma) activeDoshas.add("केमद्रुम दोष (Kemadruma Dosha)")

        return KundaliDoshaResult(
            isMangalik = isMangalik,
            isGrahanYoga = isGrahanYoga,
            isGuruChandal = isGuruChandal,
            isVishYoga = isVishYoga,
            isKemadruma = isKemadruma,
            activeDoshasSummary = activeDoshas
        )
    }
}
