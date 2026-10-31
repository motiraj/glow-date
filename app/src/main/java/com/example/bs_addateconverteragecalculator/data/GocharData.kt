package com.example.bs_addateconverteragecalculator.data

data class GrahaGocharInfo(
    val name: String,
    val karaka: String,
    val auspiciousHouses: String,
    val inauspiciousHouses: String
)

object GocharData {
    val grahaDetails = listOf(
        GrahaGocharInfo(
            name = "सूर्य",
            karaka = "सरकारी जागिर, उच्च पद, मान-सम्मान, आत्मा, पूर्वज, नेत्र, पिता, राजनीति, चिकित्सा विज्ञान र आत्मविश्वास।",
            auspiciousHouses = "३, ६, १०, ११",
            inauspiciousHouses = "१, २, ४, ५, ७, ८, ९, १२"
        ),
        GrahaGocharInfo(
            name = "चन्द्र",
            karaka = "मन, आमा, यात्रा, जल, मनोविकार र धन।",
            auspiciousHouses = "१, ३, ६, ७, १०, ११",
            inauspiciousHouses = "४, ८, १२ (विशेष), तथा २, ५, ९"
        ),
        GrahaGocharInfo(
            name = "मंगल",
            karaka = "साहस, पराक्रम, ऊर्जा, क्रोध, शक्ति, विवाद, युद्ध, शत्रु र भूमि।",
            auspiciousHouses = "३, ६, १०, ११",
            inauspiciousHouses = "४, ८, १२ (विशेष), तथा १, २, ५, ७, ९"
        ),
        GrahaGocharInfo(
            name = "बुध",
            karaka = "बुद्धिमत्ता, तर्कशक्ति, गणित, छाला, चेतना, व्यापार, सांख्यिकी, मामा र मित्र।",
            auspiciousHouses = "२, ४, ६, ८, १०, ११",
            inauspiciousHouses = "१, ३, ५, ७, ९, १२"
        ),
        GrahaGocharInfo(
            name = "बृहस्पति",
            karaka = "ज्ञान, दर्शन, विवेक, धर्म, गुरु, आध्यात्मिकता, परोपकारी कार्य र सन्तान।",
            auspiciousHouses = "२, ५, ७, ९, ११",
            inauspiciousHouses = "१, ३, ४, ६, ८, १०, १२"
        ),
        GrahaGocharInfo(
            name = "शुक्र",
            karaka = "जीवनसाथी, कला, प्रेम, सौन्दर्य, भौतिक सुख, वाहन, सङ्गीत र शयन सुख।",
            auspiciousHouses = "१, २, ३, ४, ५, ८, ९, ११, १२",
            inauspiciousHouses = "६, ७, १०"
        ),
        GrahaGocharInfo(
            name = "शनि",
            karaka = "आयु, जागिर, कर्म, न्याय, दुःख, गरिबी, आकस्मिक संकट, विदेशी भाषा, फलाम र तेल।",
            auspiciousHouses = "३, ६, ११",
            inauspiciousHouses = "१, २, ४, ५, ७, ८, ९, १०, १२ (१२, १, २ साढेसाती)"
        ),
        GrahaGocharInfo(
            name = "राहु",
            karaka = "कठोर बोली, जुवा, भ्रमित बुद्धि, विष, चोरी, दुष्टता, छालाका रोग र धार्मिक यात्रा।",
            auspiciousHouses = "३, ६, ११",
            inauspiciousHouses = "१, २, ४, ५, ७, ८, ९, १०, १२"
        ),
        GrahaGocharInfo(
            name = "केतु",
            karaka = "तन्त्र-मन्त्र, जादु-टुना, दुखाइ, ज्वरो, घाउ, शत्रुलाई हानि र मोक्ष।",
            auspiciousHouses = "१, २, ३, ४, ५, ७, ९, ११",
            inauspiciousHouses = "६, ८, १०, १२"
        )
    )

    fun getDetailForGraha(grahaName: String): GrahaGocharInfo? {
        return grahaDetails.find { it.name == grahaName }
    }
}
