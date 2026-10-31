package com.example.bs_addateconverteragecalculator.data

object NepaliNumerals {
    private val englishToNepali = mapOf(
        '0' to '०', '1' to '१', '2' to '२',
        '3' to '३', '4' to '४', '5' to '५',
        '6' to '६', '7' to '७', '8' to '८', '9' to '९'
    )

    private val nepaliToEnglish = englishToNepali.entries.associate { (k, v) -> v to k }

    fun toNepaliDigits(number: Int): String {
        return number.toString().map { englishToNepali[it] ?: it }.joinToString("")
    }

    fun toNepaliDigits(text: String): String {
        return text.map { englishToNepali[it] ?: it }.joinToString("")
    }

    fun toEnglishDigits(text: String): String {
        return text.map { nepaliToEnglish[it] ?: it }.joinToString("")
    }

    fun formatDigits(number: Int, isNepali: Boolean): String {
        return if (isNepali) toNepaliDigits(number) else number.toString()
    }

    fun formatDigits(text: String, isNepali: Boolean): String {
        return if (isNepali) toNepaliDigits(text) else text
    }
}

object NepaliMonths {
    val names = listOf(
        "Baishakh", "Jestha", "Ashadh", "Shrawan",
        "Bhadra", "Ashwin", "Kartik", "Mangsir",
        "Poush", "Magh", "Falgun", "Chaitra"
    )

    val namesNepali = listOf(
        "बैशाख", "जेठ", "असार", "श्रावण",
        "भदौ", "असोज", "कार्तिक", "मंसिर",
        "पुष", "माघ", "फागुन", "चैत"
    )

    fun getMonthName(month: Int, isNepali: Boolean): String {
        if (month !in 1..12) return ""
        return if (isNepali) namesNepali[month - 1] else names[month - 1]
    }
}

object NepaliDays {
    val names = listOf(
        "Sunday", "Monday", "Tuesday", "Wednesday",
        "Thursday", "Friday", "Saturday"
    )

    val namesNepali = listOf(
        "आइतबार", "सोमबार", "मंगलबार", "बुधबार",
        "बिहिबार", "शुक्रबार", "शनिबार"
    )

    fun getDayName(dayOfWeekIndex: Int, isNepali: Boolean): String {
        val index = (dayOfWeekIndex % 7 + 7) % 7
        return if (isNepali) namesNepali[index] else names[index]
    }
}
