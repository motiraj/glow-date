@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

data class VedhaResult(
    val planetName: String,
    val houseFromMoon: Int,
    val isAuspiciousHouse: Boolean,
    val isObstructedByVedha: Boolean,
    val vedhaObstructingPlanet: String? = null,
    val effectiveScoreContribution: Int // -10 to +10
)

data class SpecialTransitConditions(
    val isSadeSati: Boolean,
    val sadeSatiPhase: String, // "1st Phase (आद्य)", "2nd Phase (मध्य)", "3rd Phase (अन्त्य)", or "None"
    val isDhaiya: Boolean, // 4th or 8th house Saturn
    val isAshtamaChandra: Boolean, // 8th house Moon
    val hasGuruBala: Boolean // Jupiter in 2, 5, 7, 9, 11
)

data class LifeAreaCategoryScore(
    val categoryName: String, // Career, Finance, Health, Relationships, Education
    val scoreStars: Int,      // 1 to 5 stars
    val description: String
)

data class ComprehensiveVedicPrediction(
    val overallScorePercent: Int,
    val headlineBadge: String,
    val specialConditions: SpecialTransitConditions,
    val vedhaResults: List<VedhaResult>,
    val generalSummary: String,
    val categories: List<LifeAreaCategoryScore>,
    val luckyColor: String,
    val luckyNumber: String,
    val bestDirection: String,
    val auspiciousTimeWindow: String,
    val vedicRemedy: String
)

object GocharaVedhaRuleEngine {

    // Benefic Houses & Vedha Points (Phaladeepika & BPHS)
    private val SUN_BENEFIC = intArrayOf(3, 6, 10, 11)
    private val SUN_VEDHA = intArrayOf(9, 12, 4, 5)

    private val MOON_BENEFIC = intArrayOf(1, 3, 6, 7, 10, 11)
    private val MOON_VEDHA = intArrayOf(5, 9, 12, 2, 4, 8)

    private val MARS_BENEFIC = intArrayOf(3, 6, 11)
    private val MARS_VEDHA = intArrayOf(12, 9, 5)

    private val MERCURY_BENEFIC = intArrayOf(2, 4, 6, 8, 10, 11)
    private val MERCURY_VEDHA = intArrayOf(5, 3, 9, 1, 8, 12)

    private val JUPITER_BENEFIC = intArrayOf(2, 5, 7, 9, 11)
    private val JUPITER_VEDHA = intArrayOf(12, 4, 3, 10, 8)

    private val VENUS_BENEFIC = intArrayOf(1, 2, 3, 4, 5, 8, 9, 11, 12)
    private val VENUS_VEDHA = intArrayOf(8, 7, 1, 10, 9, 5, 11, 6, 3)

    private val SATURN_BENEFIC = intArrayOf(3, 6, 11)
    private val SATURN_VEDHA = intArrayOf(12, 9, 5)

    private val RAHU_KETU_BENEFIC = intArrayOf(3, 6, 11)
    private val RAHU_KETU_VEDHA = intArrayOf(12, 9, 5)

    private fun houseFrom(baseRashi: Int, targetRashi: Int): Int {
        return ((targetRashi - baseRashi + 12) % 12) + 1
    }

    /**
     * Checks Vedha (Obstruction) for a planet in a transit house from Natal Moon.
     */
    fun checkVedha(
        planetId: String,
        janmaRashi: Int,
        transitRashiByPlanet: Map<String, Int>
    ): VedhaResult {
        val planetTransitRashi = transitRashiByPlanet[planetId] ?: janmaRashi
        val house = houseFrom(janmaRashi, planetTransitRashi)

        val (beneficHouses, vedhaHouses) = when (planetId) {
            "sun" -> SUN_BENEFIC to SUN_VEDHA
            "moon" -> MOON_BENEFIC to MOON_VEDHA
            "mars" -> MARS_BENEFIC to MARS_VEDHA
            "mercury" -> MERCURY_BENEFIC to MERCURY_VEDHA
            "jupiter" -> JUPITER_BENEFIC to JUPITER_VEDHA
            "venus" -> VENUS_BENEFIC to VENUS_VEDHA
            "saturn" -> SATURN_BENEFIC to SATURN_VEDHA
            else -> RAHU_KETU_BENEFIC to RAHU_KETU_VEDHA
        }

        val idx = beneficHouses.indexOf(house)
        val isAuspiciousHouse = idx != -1

        if (!isAuspiciousHouse) {
            return VedhaResult(
                planetName = planetId,
                houseFromMoon = house,
                isAuspiciousHouse = false,
                isObstructedByVedha = false,
                effectiveScoreContribution = if (house == 8) -10 else -3
            )
        }

        val expectedVedhaHouse = vedhaHouses[idx]
        var obstructingPlanet: String? = null

        // Check if any other planet is sitting in the expected Vedha House from Natal Moon
        transitRashiByPlanet.forEach { (otherPlanetId, otherRashi) ->
            if (otherPlanetId != planetId) {
                // Special exceptions: Sun & Saturn cause no Vedha to each other; Moon causes no Vedha to Mercury
                val isSunSaturnPair = (planetId == "sun" && otherPlanetId == "saturn") || (planetId == "saturn" && otherPlanetId == "sun")
                val isMoonMercuryNoVedha = (planetId == "mercury" && otherPlanetId == "moon")

                if (!isSunSaturnPair && !isMoonMercuryNoVedha) {
                    val otherHouse = houseFrom(janmaRashi, otherRashi)
                    if (otherHouse == expectedVedhaHouse) {
                        obstructingPlanet = otherPlanetId
                    }
                }
            }
        }

        val isObstructed = obstructingPlanet != null
        val score = if (isObstructed) 2 else 8

        return VedhaResult(
            planetName = planetId,
            houseFromMoon = house,
            isAuspiciousHouse = true,
            isObstructedByVedha = isObstructed,
            vedhaObstructingPlanet = obstructingPlanet,
            effectiveScoreContribution = score
        )
    }

    /**
     * Detect Special Transit Conditions (Sade Sati, Dhaiya, Ashtama Chandra, Guru Bala)
     */
    fun evaluateSpecialConditions(
        janmaRashi: Int,
        transitRashiByPlanet: Map<String, Int>,
        lang: String = "ne"
    ): SpecialTransitConditions {
        val saturnRashi = transitRashiByPlanet["saturn"] ?: janmaRashi
        val saturnHouse = houseFrom(janmaRashi, saturnRashi)

        val isSadeSati = saturnHouse in listOf(12, 1, 2)
        val sadeSatiPhase = when (saturnHouse) {
            12 -> if (lang == "en") "1st Phase (Rising)" else if (lang == "hi") "प्रथम चरण (आद्य)" else "पहिलो चरण (आद्य)"
            1 -> if (lang == "en") "2nd Phase (Peak)" else if (lang == "hi") "द्वितीय चरण (मध्य)" else "दोस्रो चरण (मध्य)"
            2 -> if (lang == "en") "3rd Phase (Setting)" else if (lang == "hi") "तृतीय चरण (अन्त्य)" else "तेस्रो चरण (अन्त्य)"
            else -> if (lang == "en") "None" else "छैन"
        }

        val isDhaiya = saturnHouse in listOf(4, 8)

        val moonRashi = transitRashiByPlanet["moon"] ?: janmaRashi
        val moonHouse = houseFrom(janmaRashi, moonRashi)
        val isAshtamaChandra = moonHouse == 8

        val jupiterRashi = transitRashiByPlanet["jupiter"] ?: janmaRashi
        val jupiterHouse = houseFrom(janmaRashi, jupiterRashi)
        val hasGuruBala = jupiterHouse in listOf(2, 5, 7, 9, 11)

        return SpecialTransitConditions(
            isSadeSati = isSadeSati,
            sadeSatiPhase = sadeSatiPhase,
            isDhaiya = isDhaiya,
            isAshtamaChandra = isAshtamaChandra,
            hasGuruBala = hasGuruBala
        )
    }

    /**
     * Compute Weighted Score:
     * Prediction Score = (0.40 * Dasha Strength) + (0.35 * Gochara Score) + (0.25 * Tara & Chandra Bala)
     */
    fun calculateWeightedScore(
        dashaScore: Double,      // 0..100
        gocharaScore: Double,    // 0..100
        balaScore: Double        // 0..100
    ): Int {
        val total = (0.40 * dashaScore) + (0.35 * gocharaScore) + (0.25 * balaScore)
        return total.toInt().coerceIn(10, 99)
    }
}
