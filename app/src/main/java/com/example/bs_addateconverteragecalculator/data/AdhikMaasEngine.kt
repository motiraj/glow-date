@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import java.time.LocalDate

data class AdhikMaasPeriodInfo(
    val bsYear: Int,
    val bsMonthName: String,
    val adStartDate: LocalDate,
    val adEndDate: LocalDate,
    val bsStartText: String,
    val bsEndText: String,
    val descriptionNepali: String,
    val descriptionEnglish: String
)

data class AdhikMaasStatusResult(
    val isAdhikMaasActive: Boolean,
    val currentOrUpcomingInfo: AdhikMaasPeriodInfo?,
    val isCurrentDateInMalmas: Boolean
)

object AdhikMaasEngine {

    // Known Calibrated Adhik Maas Table for BS 2070 to 2090
    val ADHIK_MAAS_TABLE = listOf(
        AdhikMaasPeriodInfo(
            bsYear = 2072,
            bsMonthName = "भाद्र",
            adStartDate = LocalDate.of(2015, 8, 15),
            adEndDate = LocalDate.of(2015, 9, 13),
            bsStartText = "२०७२ भाद्र १",
            bsEndText = "२०७२ भाद्र २८",
            descriptionNepali = "वि.सं. २०७२ भाद्र महिनामा शुद्ध भाद्र अधिकमास परेको थियो।",
            descriptionEnglish = "Bhadra Adhik Maas occurred in BS 2072."
        ),
        AdhikMaasPeriodInfo(
            bsYear = 2075,
            bsMonthName = "ज्येष्ठ",
            adStartDate = LocalDate.of(2018, 5, 16),
            adEndDate = LocalDate.of(2018, 6, 13),
            bsStartText = "२०७५ ज्येष्ठ २",
            bsEndText = "२०७५ ज्येष्ठ ३०",
            descriptionNepali = "वि.सं. २०७५ ज्येष्ठ महिनामा शुद्ध पुरुषोत्तम अधिकमास परेको थियो।",
            descriptionEnglish = "Jestha Adhik Maas occurred in BS 2075."
        ),
        AdhikMaasPeriodInfo(
            bsYear = 2077,
            bsMonthName = "आश्विन",
            adStartDate = LocalDate.of(2020, 9, 18),
            adEndDate = LocalDate.of(2020, 10, 16),
            bsStartText = "२०७७ आश्विन २",
            bsEndText = "२०७७ कात्तिक ३०",
            descriptionNepali = "वि.सं. २०७७ आश्विन महिनामा अधिकमास (मलमास) परेको कारण दशैँ तिहार सो वर्ष १ महिना ढिलो भएको थियो।",
            descriptionEnglish = "Ashwin Adhik Maas occurred in BS 2077, shifting Dashain by 1 month."
        ),
        AdhikMaasPeriodInfo(
            bsYear = 2080,
            bsMonthName = "श्रावण",
            adStartDate = LocalDate.of(2023, 7, 18),
            adEndDate = LocalDate.of(2023, 8, 16),
            bsStartText = "२०८० श्रावण २",
            bsEndText = "२०८० भाद्र ३",
            descriptionNepali = "वि.सं. २०८० श्रावण महिनामा शुद्ध श्रावण अधिकमास (मलमास / पुरुषोत्तम मास) परेको थियो।",
            descriptionEnglish = "Shrawan Adhik Maas (Purushottam Maas) occurred in BS 2080."
        ),
        AdhikMaasPeriodInfo(
            bsYear = 2083,
            bsMonthName = "ज्येष्ठ",
            adStartDate = LocalDate.of(2026, 5, 17),
            adEndDate = LocalDate.of(2026, 6, 15),
            bsStartText = "२०८३ ज्येष्ठ ३",
            bsEndText = "२०८३ असार १",
            descriptionNepali = "वि.सं. २०८३ ज्येष्ठ महिनामा आगामी अधिकमास (मलमास / अनला) पर्नेछ। यस अवधिमा विष्णु/पुरुषोत्तम भगवानको पूजाआजा फलदायी हुनेछ।",
            descriptionEnglish = "Upcoming Jestha Adhik Maas (Malmas / Anala) in BS 2083 (May-June 2026 AD)."
        ),
        AdhikMaasPeriodInfo(
            bsYear = 2085,
            bsMonthName = "चैत्र",
            adStartDate = LocalDate.of(2028, 4, 17),
            adEndDate = LocalDate.of(2028, 5, 15),
            bsStartText = "२०८५ चैत्र ५",
            bsEndText = "२०८६ वैशाख २",
            descriptionNepali = "वि.सं. २०८५ चैत्र/वैशाख महिनामा अधिकमास (मलमास) पर्नेछ।",
            descriptionEnglish = "Chaitra Adhik Maas in BS 2085 (April-May 2028 AD)."
        )
    )

    /**
     * Checks if a given AD Date falls inside an active Adhik Maas (Malmas) period.
     */
    fun checkAdhikMaas(date: LocalDate): AdhikMaasStatusResult {
        val currentPeriod = ADHIK_MAAS_TABLE.find {
            !date.isBefore(it.adStartDate) && !date.isAfter(it.adEndDate)
        }

        if (currentPeriod != null) {
            return AdhikMaasStatusResult(
                isAdhikMaasActive = true,
                currentOrUpcomingInfo = currentPeriod,
                isCurrentDateInMalmas = true
            )
        }

        // Find upcoming Adhik Maas
        val upcoming = ADHIK_MAAS_TABLE.find { date.isBefore(it.adStartDate) }

        return AdhikMaasStatusResult(
            isAdhikMaasActive = false,
            currentOrUpcomingInfo = upcoming,
            isCurrentDateInMalmas = false
        )
    }

    /**
     * Astronomical check: Returns true if a lunar month starting on adDate has NO Sun Sankranti.
     */
    fun isAstronomicalAdhikMaas(adDate: LocalDate, tzOffset: Double = 5.75): Boolean {
        val snap1 = OfflineVedicEphemeris.getPlanetarySnapshot(
            adDate.year, adDate.monthValue, adDate.dayOfMonth, 6, 0, 0, tzOffset
        )
        val snap2 = OfflineVedicEphemeris.getPlanetarySnapshot(
            adDate.plusDays(29).year, adDate.plusDays(29).monthValue, adDate.plusDays(29).dayOfMonth, 6, 0, 0, tzOffset
        )
        return snap1.sunRashi == snap2.sunRashi
    }
}
