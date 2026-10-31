@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

data class BalaResult(
    val taraNumber: Int,          // 1 to 9
    val taraName: String,         // जन्म तारा, सम्पत् तारा, आदि
    val isTaraShubha: Boolean,     // True if 2, 4, 6, 8, 9
    val chandraHouse: Int,        // 1 to 12
    val chandraStatus: String,    // अति शुभ (लाभदायक), चन्द्राष्टम, आदि
    val isChandraShubha: Boolean   // True if 3, 6, 7, 10, 11
)

object BalaCalculator {

    private val TARA_NAMES = arrayOf(
        "जन्म तारा", "सम्पत् तारा", "विपत् तारा", "क्षेम तारा",
        "प्रत्यरि तारा", "साधक तारा", "वध तारा", "मित्र तारा", "अतिमित्र तारा"
    )

    fun calculateTaraAndChandraBala(
        natalNakshatra: Int, // 1 to 27
        todayNakshatra: Int, // 1 to 27
        natalRashi: Int,     // 1 to 12
        todayRashi: Int      // 1 to 12
    ): BalaResult {
        // 1. Tara Bala Calculation (1 to 9)
        val taraDiff = (todayNakshatra - natalNakshatra + 27) % 9
        val taraIdx = if (taraDiff == 0) 8 else taraDiff - 1
        val taraName = TARA_NAMES[taraIdx]

        // Favorable Taras: सम्पत्(2), क्षेम(4), साधक(6), मित्र(8), अतिमित्र(9) -> Index 1, 3, 5, 7, 8
        val isTaraShubha = taraIdx in listOf(1, 3, 5, 7, 8)

        // 2. Chandra Bala Calculation (1 to 12)
        val chandraHouse = ((todayRashi - natalRashi + 12) % 12) + 1

        // Favorable Chandra Houses: 3, 6, 7, 10, 11
        val isChandraShubha = chandraHouse in listOf(3, 6, 7, 10, 11)
        val chandraStatus = when (chandraHouse) {
            3, 6, 7, 10, 11 -> "अति शुभ (लाभदायक)"
            8 -> "चन्द्राष्टम (अति अशुभ - कार्य वर्जित)"
            4, 12 -> "प्रतिकूल (खर्च र कष्ट)"
            else -> "मध्यम (सजग रहनुहोला)"
        }

        return BalaResult(
            taraNumber = taraIdx + 1,
            taraName = taraName,
            isTaraShubha = isTaraShubha,
            chandraHouse = chandraHouse,
            chandraStatus = chandraStatus,
            isChandraShubha = isChandraShubha
        )
    }
}
