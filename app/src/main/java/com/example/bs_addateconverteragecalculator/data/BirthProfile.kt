@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

enum class UserGender {
    MALE,
    FEMALE,
    UNSPECIFIED
}

data class BirthProfile(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val gender: UserGender = UserGender.UNSPECIFIED,
    val isBsInput: Boolean = true,
    val bsYear: Int = 2058,
    val bsMonth: Int = 1,
    val bsDay: Int = 1,
    val adBirthDate: String = "2001-04-14", // YYYY-MM-DD
    val birthHour: Int = 7,
    val birthMinute: Int = 30,
    val locationName: String = "Kathmandu, Nepal",
    val latitude: Double = 27.7172,
    val longitude: Double = 85.3240,
    val timezoneOffset: Double = 5.75,
    val isEnabled: Boolean = true
)

data class NatalProfile(
    val janmaRashiIndex: Int, // 0 = मेष ... 11 = मीन
    val janmaRashiNameNepali: String,
    val janmaRashiNameEnglish: String,
    val nakshatraIndex: Int, // 0 = अश्विनी ... 26 = रेवती
    val nakshatraNameNepali: String,
    val pada: Int, // 1 to 4
    val naamAkshar: String,
    val nadi: String, // आदि, मध्य, अन्त्य
    val gana: String, // देव, मनुष्य, राक्षस
    val lagnaIndex: Int, // 0 = मेष ... 11 = मीन
    val lagnaNameNepali: String,
    val ishtakalaText: String = "६ घडी ५ पला",
    val bhayatText: String = "२२ घडी ३० पला",
    val bhabhogText: String = "६० घडी ० पला",
    val bhuktaDashaText: String = "",
    val bhogyaDashaText: String = ""
)

data class TransitAnalysis(
    val transitMoonRashiIndex: Int,
    val transitMoonRashiName: String,
    val houseFromNatal: Int, // 1 to 12
    val isChandrashtama: Boolean, // houseFromNatal == 8
    val chandraBalaStatus: String, // "शुभ", "अशुभ", "सामान्य"
    val chandraBalaDesc: String,
    val taraBalaIndex: Int, // 1 to 9
    val taraBalaName: String,
    val taraBalaStatus: String, // "शुभ", "अशुभ", "सावधान"
    val taraBalaDesc: String,
    val activeMahadashaLord: String,
    val activeAntardashaLord: String,
    val activeYoginiDasha: String
)

data class PersonalizedPrediction(
    val health: String,
    val career: String,
    val finance: String,
    val luckyColor: String,
    val luckyNumber: String,
    val dailyAdvice: String
)
