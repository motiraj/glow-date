@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

object NepalSambatCalculator {

    private val NS_MONTHS = arrayOf(
        "बछला",   // 1 (Baisakh - NS 7th month)
        "तछला",    // 2 (Jestha - NS 8th month)
        "दिल्ला",   // 3 (Asadh - NS 9th month)
        "गुँला",    // 4 (Shrawan - NS 10th month)
        "ञंला",     // 5 (Bhadra - NS 11th month)
        "कौला",    // 6 (Ashwin - NS 12th month)
        "कछला",   // 7 (Kartik - NS 1st month / New Year)
        "थिंला",    // 8 (Mangsir - NS 2nd month)
        "पोहेला",   // 9 (Poush - NS 3rd month)
        "सिल्ला",    // 10 (Magh - NS 4th month)
        "चिल्ला",    // 11 (Falgun - NS 5th month)
        "चौला"     // 12 (Chaitra - NS 6th month)
    )

    fun getNepalSambatDetails(bsYear: Int, bsMonth: Int, bsDay: Int, paksha: String, tithi: String): String {
        val cleanPaksha = if (paksha.contains("शुक्ल") || paksha.contains("पूर्णिमा") || tithi.contains("शुक्ल") || tithi.contains("पूर्णिमा")) "शुक्ल" else "कृष्ण"

        // In Kartik (Month 7), Mha Puja / Nhudhan happens around Kartik 23/24 (Kartik Shukla 1).
        // Before Mha Puja (Kartik 1 to ~22 or when Tithi belongs to Ashwin/Kaula cycle):
        //   nsYear = bsYear - 937, nsMonthName = "कौला"
        // Starting Mha Puja (Kartik Shukla 1 to Chaitra end):
        //   nsYear = bsYear - 936, nsMonthName = "कछला"
        val isBeforeMhaPujaInKartik = (bsMonth == 7 && (bsDay < 24 || tithi.contains("आश्विन") || tithi.contains("कौला")))

        val nsYear = if (bsMonth > 7 || (bsMonth == 7 && !isBeforeMhaPujaInKartik)) {
            bsYear - 936
        } else {
            bsYear - 937
        }

        val nsMonthName = when {
            isBeforeMhaPujaInKartik -> "कौला"
            bsMonth in 1..12 -> NS_MONTHS[bsMonth - 1]
            else -> "कछला"
        }

        // Nepal Sambat Paksha: Shukla = "थ्व", Krishna = "गाः"
        val nsPaksha = if (cleanPaksha == "शुक्ल") "थ्व" else "गाः"

        // Clean Newar Tithi Phrasing
        val cleanTithiRaw = tithi.replace("शुक्ल ", "").replace("कृष्ण ", "").replace("आश्विन ", "").replace("कार्तिक ", "").trim()
        val nsTithi = when {
            cleanTithiRaw.contains("प्रतिपदा") -> "पारु"
            cleanTithiRaw.contains("द्वितीया") -> "दुतिया"
            cleanTithiRaw.contains("तृतीया") -> "तितिया"
            cleanTithiRaw.contains("चतुर्थी") -> "चौथि"
            cleanTithiRaw.contains("पञ्चमी") -> "पन्चमि"
            cleanTithiRaw.contains("षष्ठी") -> "खस्तमि"
            cleanTithiRaw.contains("सप्तमी") -> "सप्तमि"
            cleanTithiRaw.contains("अष्टमी") -> "अस्तमि"
            cleanTithiRaw.contains("नवमी") -> "नःमि"
            cleanTithiRaw.contains("दशमी") -> "दसमि"
            cleanTithiRaw.contains("एकादशी") -> "एकादसि"
            cleanTithiRaw.contains("द्वादशी") -> "दुवादसि"
            cleanTithiRaw.contains("त्रयोदशी") -> "त्रयदसि"
            cleanTithiRaw.contains("चतुर्दशी") -> "चःह्रे"
            cleanTithiRaw.contains("पूर्णिमा") -> "पुन्हि"
            cleanTithiRaw.contains("औँसी") || cleanTithiRaw.contains("अमावास्या") -> "आमै"
            else -> cleanTithiRaw
        }

        return "नेपाल संवत् ${NepaliNumerals.toNepaliDigits(nsYear)}, $nsMonthName$nsPaksha $nsTithi"
    }

    // Overload for backward compatibility
    fun getNepalSambatDetails(bsYear: Int, bsMonth: Int, paksha: String, tithi: String): String {
        return getNepalSambatDetails(bsYear, bsMonth, 15, paksha, tithi)
    }
}
