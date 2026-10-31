@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

object GenderAwarePredictionHelper {

    // 1. Spouse Significator (पति/पत्नी कारक ग्रह)
    fun getSpouseKarakas(gender: UserGender): List<String> {
        return when (gender) {
            UserGender.MALE -> listOf("शुक्र")
            UserGender.FEMALE -> listOf("बृहस्पति", "मङ्गल")
            UserGender.UNSPECIFIED -> listOf("शुक्र", "बृहस्पति")
        }
    }

    // 2. Phrasing by Gender in Nepali and English
    fun getSpouseTermNepali(gender: UserGender): String {
        return when (gender) {
            UserGender.MALE -> "धर्मपत्नी / जीवनसाथी"
            UserGender.FEMALE -> "पति / जीवनसाथी"
            UserGender.UNSPECIFIED -> "जीवनसाथी"
        }
    }

    fun getSpouseTermEnglish(gender: UserGender): String {
        return when (gender) {
            UserGender.MALE -> "Wife / Life Partner"
            UserGender.FEMALE -> "Husband / Life Partner"
            UserGender.UNSPECIFIED -> "Spouse / Life Partner"
        }
    }

    // 3. Marriage & Relationship Promise Score
    fun calculateMarriagePromiseScore(
        gender: UserGender,
        seventhHouseScore: Int,
        eighthHouseScore: Int,
        venusStrength: Int,
        jupiterStrength: Int
    ): Int {
        return when (gender) {
            UserGender.MALE -> (seventhHouseScore * 0.55 + venusStrength * 0.35 + eighthHouseScore * 0.10).toInt()
            UserGender.FEMALE -> (seventhHouseScore * 0.45 + jupiterStrength * 0.35 + eighthHouseScore * 0.20).toInt()
            UserGender.UNSPECIFIED -> {
                val combinedKaraka = (venusStrength + jupiterStrength) / 2
                (seventhHouseScore * 0.50 + combinedKaraka * 0.35 + eighthHouseScore * 0.15).toInt()
            }
        }
    }
}
