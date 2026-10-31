@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

object ShraddhaCalculator {
    fun getSorhaShraddhaInfo(month: Int, tithi: String): String? {
        // Ashwin Krishna Paksha (Sorha Shraddha / Pitru Paksha)
        if (month == 6 && tithi.contains("कृष्ण")) {
            return when {
                tithi.contains("प्रतिपदा") -> "प्रतिपदा श्राद्ध"
                tithi.contains("द्वितीया") -> "द्वितीया श्राद्ध"
                tithi.contains("तृतीया") -> "तृतीया श्राद्ध"
                tithi.contains("चतुर्थी") -> "चतुर्थी श्राद्ध"
                tithi.contains("पञ्चमी") -> "पञ्चमी श्राद्ध"
                tithi.contains("षष्ठी") -> "षष्ठी श्राद्ध"
                tithi.contains("सप्तमी") -> "सप्तमी श्राद्ध"
                tithi.contains("अष्टमी") -> "महालक्ष्मी व्रत समाप्त, अष्टमी श्राद्ध"
                tithi.contains("नवमी") -> "मातृ नवमी, नवमी श्राद्ध"
                tithi.contains("दशमी") -> "दशमी श्राद्ध"
                tithi.contains("एकादशी") -> "इन्दिरा एकादशी व्रत, एकादशी श्राद्ध"
                tithi.contains("द्वादशी") -> "द्वादशी श्राद्ध (सन्यासी श्राद्ध)"
                tithi.contains("त्रयोदशी") -> "त्रयोदशी श्राद्ध"
                tithi.contains("चतुर्दशी") -> "चतुर्दशी श्राद्ध (अकाल मृत्यु श्राद्ध)"
                tithi.contains("औंसी") -> "सोह्र श्राद्ध समाप्त, औंसी श्राद्ध (पितृ विसर्जन)"
                else -> "सोह्र श्राद्ध"
            }
        }

        // Special Vratas and Tithi Parvas
        return when {
            month == 1 && tithi == "शुक्ल तृतीया" -> "अक्षय तृतीया व्रत"
            month == 1 && tithi == "पूर्णिमा" -> "बुद्ध जयन्ती / उभौली पर्व"
            month == 3 && tithi == "शुक्ल एकादशी" -> "हरिशयनी एकादशी व्रत (तुलसी रोपण)"
            month == 5 && tithi == "कृष्ण अष्टमी" -> "श्री कृष्ण जन्माष्टमी व्रत"
            month == 5 && tithi == "शुक्ल तृतीया" -> "हरितालिका तीज व्रत"
            month == 5 && tithi == "शुक्ल चतुर्थी" -> "गणेश चतुर्थी व्रत"
            month == 5 && tithi == "शुक्ल पञ्चमी" -> "ऋषि पञ्चमी व्रत"
            month == 7 && tithi == "शुक्ल एकादशी" -> "हरिबोधिनी एकादशी व्रत (तुलसी विवाह)"
            month == 7 && tithi == "शुक्ल षष्ठी" -> "छठ पर्व व्रत"
            month == 11 && tithi == "कृष्ण चतुर्दशी" -> "महाशिवरात्रि व्रत"
            month == 11 && tithi == "पूर्णिमा" -> "फागु पूर्णिमा (होली)"
            tithi.contains("एकादशी") -> "एकादशी व्रत"
            tithi.contains("त्रयोदशी") -> "प्रदोष व्रत"
            tithi.contains("पूर्णिमा") -> "पूर्णिमा व्रत"
            tithi.contains("औंसी") -> "दर्शा औंसी"
            else -> null
        }
    }
}
