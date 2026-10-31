package com.example.bs_addateconverteragecalculator.data

data class Holiday(
    val bsYear: Int? = null, // null = every year (solar/fixed), non-null = specific year (lunar/shifting)
    val bsMonth: Int,
    val bsDay: Int,
    val title: String,
    val titleNepali: String
)

object HolidayData {
    val holidays = listOf(
        // --- Year-Agnostic (Solar / Fixed Date) Holidays ---
        Holiday(null, 1, 1, "Nepali New Year", "नेपाली नयाँ वर्ष"),
        Holiday(null, 1, 11, "Loktantra Diwas", "लोकतान्त्रिक दिवस"),
        Holiday(null, 2, 15, "Republic Day", "गणतन्त्र दिवस"),
        Holiday(null, 3, 15, "National Paddy Day", "राष्ट्रिय धान दिवस / रोपाइँ जात्रा"),
        Holiday(null, 6, 3, "Constitution Day", "संविधान दिवस"),
        Holiday(null, 10, 1, "Maghe Sankranti", "माघे संक्रान्ति"),
        Holiday(null, 10, 16, "Sahid Diwas", "शहीद दिवस"),
        Holiday(null, 11, 7, "Prajatantra Diwas", "प्रजातन्त्र दिवस"),
        Holiday(null, 11, 24, "International Women's Day", "अन्तर्राष्ट्रिय महिला दिवस"),
        Holiday(null, 12, 10, "Ram Navami", "राम नवमी"),

        // =========================================================================
        // --- Year 2083 BS Official Festivals, Parvas & Holidays (नेपाली पात्रो २०८३) ---
        // =========================================================================

        // --- बैशाख २०८३ (Baisakh 2083) ---
        Holiday(2083, 1, 1, "Nepali New Year 2083 / Mesha Sankranti", "नेपाली नयाँ वर्ष २०८३ / मेष संक्रान्ति"),
        Holiday(2083, 1, 2, "Jud Sheetal & Sindoor Jatra", "जुडशीतल तथा सिन्दूर जात्रा"),
        Holiday(2083, 1, 4, "Matatirtha Aunsi (Mother's Day)", "मातातीर्थ औंसी (आमाको मुख हेर्ने दिन)"),
        Holiday(2083, 1, 7, "Akshaya Tritiya", "अक्षय तृतीया"),
        Holiday(2083, 1, 11, "Loktantra Diwas", "लोकतन्त्र दिवस"),
        Holiday(2083, 1, 12, "Sita Jayanti / Janaki Jayanti", "जानकी जयन्ती (सीता नवमी)"),
        Holiday(2083, 1, 14, "Mohini Ekadashi Vrat", "मोहिनी एकादशी व्रत"),
        Holiday(2083, 1, 15, "Bratabandha & Annaprashan Sait", "ब्रतबन्ध तथा अन्नप्रासन शुभ साइत"),
        Holiday(2083, 1, 18, "Buddha Jayanti / Ubhauli / Labour Day", "बुद्ध जयन्ती / उभौली पर्व / अन्तर्राष्ट्रिय श्रमिक दिवस"),
        Holiday(2083, 1, 22, "Vaparik Pratishthan Arambha Sait", "व्यापारिक प्रतिष्ठान प्रारम्भ साइत"),
        Holiday(2083, 1, 23, "Law Day", "राष्ट्रिय कानुन दिवस"),
        Holiday(2083, 1, 30, "Apara Ekadashi Vrat", "अपरा एकादशी व्रत"),

        // --- जेठ २०८३ (Jestha 2083) ---
        Holiday(2083, 2, 2, "Adhik Maas (Malmas) Arambha", "अधिकमास (मलमास) प्रारम्भ"),
        Holiday(2083, 2, 12, "Padmini Ekadashi / Vivah Sait", "पद्मिनी एकादशी व्रत / विवाह शुभ साइत"),
        Holiday(2083, 2, 15, "Republic Day", "गणतन्त्र दिवस"),
        Holiday(2083, 2, 17, "Adhik Maas Purnima Vrat", "अधिकमास पूर्णिमा व्रत"),
        Holiday(2083, 2, 18, "Rudri & Agni Jurne Din", "रुद्री जुर्ने / अग्नि जुर्ने दिन"),
        Holiday(2083, 2, 22, "World Environment Day", "विश्व वातावरण दिवस"),
        Holiday(2083, 2, 28, "Parama Ekadashi Vrat", "परमा एकादशी व्रत"),
        Holiday(2083, 2, 31, "Adhik Maas (Malmas) Samapti", "अधिकमास (मलमास) समाप्ति"),

        // --- असार २०८३ (Ashadh 2083) ---
        Holiday(2083, 3, 1, "Ashadh Sankranti / Mithun Sankranti", "असार संक्रान्ति (मिथुन संक्रान्ति) / दर्श औंसी"),
        Holiday(2083, 3, 11, "Nirjala Ekadashi Vrat", "निर्जला एकादशी व्रत"),
        Holiday(2083, 3, 15, "National Paddy Day / Asadh 15 / Purnima Vrat", "राष्ट्रिय धान दिवस / दही-चिउरा खाने दिन / पूर्णिमा व्रत"),
        Holiday(2083, 3, 27, "Yogini Ekadashi Vrat", "योगिनी एकादशी व्रत"),
        Holiday(2083, 3, 30, "Ashadh Aunsi", "आषाढ दर्श औंसी"),

        // --- श्रावण २०८३ (Shrawan 2083) ---
        Holiday(2083, 4, 1, "Saune Sankranti (Dakshinayan Start)", "साउने संक्रान्ति (कर्कट संक्रान्ति) / दक्षिणायन प्रारम्भ"),
        Holiday(2083, 4, 9, "Harishayani Ekadashi (Chaturmas Vrat Start)", "हरिशयनी एकादशी व्रत (तुलसी रोपण / चतुर्मास व्रत सुरु)"),
        Holiday(2083, 4, 13, "Guru Purnima / Bhanu Jayanti", "गुरु पूर्णिमा तथा व्यास जयन्ती / भानु जयन्ती"),
        Holiday(2083, 4, 15, "Shrawan 15 (Kheer Khane Din)", "साउन १५ (खीर खाने दिन)"),
        Holiday(2083, 4, 24, "Kamika Ekadashi Vrat", "कामिका एकादशी व्रत"),
        Holiday(2083, 4, 26, "Ghantakarna Chaturdashi (Gathe Mangal)", "घण्टाकर्ण चतुर्दशी (गठेमङ्गल)"),
        Holiday(2083, 4, 27, "Shrawan Aunsi", "श्रावण दर्श औंसी"),

        // --- भाद्र २०८३ (Bhadra 2083) ---
        Holiday(2083, 5, 1, "Bhadra Sankranti / Nag Panchami", "भदौ संक्रान्ति (सिंह संक्रान्ति) / नागपञ्चमी"),
        Holiday(2083, 5, 7, "Putrada Ekadashi Vrat", "पुत्रदा एकादशी व्रत"),
        Holiday(2083, 5, 11, "Janai Purnima / Rakshabandhan / Kwati Punhi", "जनै पूर्णिमा, रक्षाबन्धन तथा क्वाँटी खाने दिन"),
        Holiday(2083, 5, 12, "Gai Jatra (Saparu)", "गाईजात्रा (सापारु)"),
        Holiday(2083, 5, 19, "Shree Krishna Janmashtami", "श्रीकृष्ण जन्माष्टमी व्रत"),
        Holiday(2083, 5, 22, "Aja Ekadashi Vrat", "अजा एकादशी व्रत"),
        Holiday(2083, 5, 26, "Kushe Aunsi (Father's Day / Moti Jayanti)", "कुशे औंसी (बाबुको मुख हेर्ने दिन / मोती जयन्ती)"),
        Holiday(2083, 5, 29, "Haritalika Teej Vrat", "हरितालिका तीज व्रत"),
        Holiday(2083, 5, 30, "Ganesh Chaturthi / Chourchan", "गणेश चतुर्थी तथा चौठचन्द्र (चौरचन)"),
        Holiday(2083, 5, 31, "Rishi Panchami", "ऋषि पञ्चमी"),

        // --- आश्विन २०८३ (Ashwin 2083) ---
        Holiday(2083, 6, 1, "Vishwakarma Puja / Kanya Sankranti", "विश्वकर्मा पूजा (कन्या संक्रान्ति)"),
        Holiday(2083, 6, 3, "National Constitution Day", "राष्ट्रिय संविधान दिवस"),
        Holiday(2083, 6, 6, "Parivartini Ekadashi Vrat", "परिवर्तिनी एकादशी व्रत"),
        Holiday(2083, 6, 9, "Indra Jatra / Ananta Chaturdashi", "इन्द्रजात्रा तथा अनन्त चतुर्दशी"),
        Holiday(2083, 6, 10, "Purnima Vrat / Sorha Shraddha Start", "पूर्णिमा व्रत / सोह्र श्राद्ध (पितृ पक्ष) प्रारम्भ"),
        Holiday(2083, 6, 20, "Indira Ekadashi Vrat", "इन्दिरा एकादशी व्रत (एकादशी श्राद्ध)"),
        Holiday(2083, 6, 24, "Sarvapitri Aunsi (Sorha Shraddha End)", "सर्वपितृ औंसी (सोह्र श्राद्ध विसर्जन)"),
        Holiday(2083, 6, 25, "Ghatasthapana (Dashain Start)", "घटस्थापना (बडादशैं प्रारम्भ)"),
        Holiday(2083, 6, 31, "Pachali Bhairav Yatra", "पचली भैरव यात्रा"),

        // --- कात्तिक २०८३ (Kartik 2083) ---
        Holiday(2083, 7, 1, "Phoolpati / Maha Ashtami / Dashain Bidha", "फूलपाती / महाअष्टमी (कालरात्रि पूजा) / दशैं बिदा"),
        Holiday(2083, 7, 2, "Maha Navami / Dashain Bidha", "महानवमी (सिद्धिदात्री पूजा) / दशैं बिदा"),
        Holiday(2083, 7, 3, "Vijaya Dashami (Main Tika) / Devi Visarjan", "विजयादशमी (दशैंको मुख्य टीका / देवी विसर्जन)"),
        Holiday(2083, 7, 4, "Dashain Bidha (Ekadashi)", "दशैं बिदा"),
        Holiday(2083, 7, 5, "Papankusha Ekadashi / Dashain Bidha", "पापांकुशा एकादशी व्रत / दशैं बिदा"),
        Holiday(2083, 7, 6, "Dashain Bidha", "दशैं बिदा"),
        Holiday(2083, 7, 8, "Kojagrat Purnima (Dashain End)", "कोजाग्रत पूर्णिमा (दशैं समापन / आकाशदीप दान)"),
        Holiday(2083, 7, 21, "Kag Tihar / Dhanteras / Dhanwantari Jayanti", "काग तिहार / धनतेरस / धन्वन्तरी जयन्ती (यमपञ्चक सुरु)"),
        Holiday(2083, 7, 22, "Kukur Tihar / Laxmi Puja / Deepawali", "कुकुर तिहार / नरक चतुर्दशी / लक्ष्मीपूजा / दीपावली"),
        Holiday(2083, 7, 23, "Gai Puja / Govardhan Puja / Tihar Bidha", "गाई पूजा / गोवर्धन पूजा / बलि पूजा / तिहार बिदा"),
        Holiday(2083, 7, 24, "Mha Puja / Nepal Sambat 1147 Nhudhan", "म्ह पूजा / नेपाल संवत् ११४७ न्हूदँ प्रारम्भ (हलो पूजा)"),
        Holiday(2083, 7, 25, "Bhai Tika / Phalgunanda Jayanti", "भाइटीका (यमद्वितीया) / महागुरु फाल्गुनन्द जयन्ती"),
        Holiday(2083, 7, 26, "Tihar Bidha", "तिहार बिदा"),
        Holiday(2083, 7, 29, "Chhath Parva (Surya Shasthi)", "छठ पर्व (सूर्य षष्ठी व्रत / डाला पूजा)"),

        // --- मंसिर २०८३ (Mangsir 2083) ---
        Holiday(2083, 8, 1, "Mangsir Sankranti / Gopashtami", "मंसिर संक्रान्ति (वृश्चिक संक्रान्ति) / गोपाष्टमी"),
        Holiday(2083, 8, 5, "Haribodhini Ekadashi (Tulsi Vivah) / Vivah Sait (1)", "हरिबोधिनी एकादशी व्रत (ठूलो एकादशी / तुलसी विवाह) / विवाह शुभ साइत"),
        Holiday(2083, 8, 8, "Baikuntha Chaturdashi / Purnima Vrat", "बैकुण्ठ चतुर्दशी / पूर्णिमा व्रत"),
        Holiday(2083, 8, 9, "Bala Chaturdashi (Shatbij Chharne Din)", "बाला चतुर्दशी (शतबीज छर्ने दिन)"),
        Holiday(2083, 8, 10, "Vivah Panchami (Sita Ram Vivah)", "विवाह पञ्चमी (सीताराम विवाह महोत्सव)"),
        Holiday(2083, 8, 12, "Vivah & Bratabandha Sait", "विवाह तथा ब्रतबन्ध गर्ने शुभ साइत"),
        Holiday(2083, 8, 18, "Utpattika Ekadashi Vrat", "उत्पत्तिका एकादशी व्रत"),
        Holiday(2083, 8, 19, "Vivah Sait (2) / Rudri Jurne", "विवाह गर्ने शुभ साइत / रुद्री जुर्ने"),
        Holiday(2083, 8, 24, "Udhauli Parva / Yomari Punhi / Dhanya Purnima", "उधौली पर्व / धान्य पूर्णिमा / योमरी पुन्ही"),
        Holiday(2083, 8, 25, "Human Rights Day", "अन्तर्राष्ट्रिय मानव अधिकार दिवस"),
        Holiday(2083, 8, 26, "Vivah Sait (3)", "विवाह गर्ने शुभ साइत"),

        // --- पुस २०८३ (Poush 2083) ---
        Holiday(2083, 9, 1, "Poush Sankranti (Dhanu Sankranti)", "पुस संक्रान्ति (धनु संक्रान्ति)"),
        Holiday(2083, 9, 5, "Mokshada Ekadashi / Geeta Jayanti", "मोक्षदा एकादशी व्रत (गीता जयन्ती)"),
        Holiday(2083, 9, 9, "Poush Purnima Vrat", "पूर्णिमा व्रत"),
        Holiday(2083, 9, 10, "Christmas Day", "क्रिसमस डे"),
        Holiday(2083, 9, 15, "Tamu Lhosar / Poush 15 (Latte Khane Din)", "तमु ल्होसार (गुरुङ नयाँ वर्ष) / पुस १५ (लट्टे खाने दिन)"),
        Holiday(2083, 9, 19, "Saphala Ekadashi Vrat", "सफला एकादशी व्रत"),
        Holiday(2083, 9, 23, "Poush Aunsi", "पुस दर्श औंसी"),
        Holiday(2083, 9, 27, "National Unity Day (Prithvi Jayanti)", "राष्ट्रिय एकता दिवस (पृथ्वी जयन्ती)"),

        // --- माघ २०८३ (Magh 2083) ---
        Holiday(2083, 10, 1, "Maghe Sankranti / Maghi Parva", "माघे संक्रान्ति (मकर संक्रान्ति / माघी पर्व) / उत्तरायण प्रारम्भ"),
        Holiday(2083, 10, 5, "Putrada Ekadashi Vrat", "पुत्रदा एकादशी व्रत"),
        Holiday(2083, 10, 8, "Swasthani Vrat Arambha / Purnima Vrat", "श्री स्वस्थानी व्रत प्रारम्भ / माघ स्नान सुरु / पूर्णिमा व्रत"),
        Holiday(2083, 10, 10, "Vivah & Annaprashan Sait", "विवाह तथा अन्नप्रासन शुभ साइत"),
        Holiday(2083, 10, 16, "Sahid Diwas (Martyrs' Day)", "राष्ट्रिय शहीद दिवस"),
        Holiday(2083, 10, 19, "Shattila Ekadashi Vrat", "षट्तिला एकादशी व्रत"),
        Holiday(2083, 10, 22, "Agni Jurne / Vaparik Arambha", "व्यापारिक प्रतिष्ठान प्रारम्भ / अग्नि जुर्ने"),
        Holiday(2083, 10, 23, "Mauni Aunsi / Sonam Lhosar", "मौनी औंसी / सोनाम ल्होसार (तामाङ नयाँ वर्ष)"),
        Holiday(2083, 10, 28, "Basanta Panchami / Saraswati Puja", "वसन्त पञ्चमी / श्रीपञ्चमी (सरस्वती पूजा)"),

        // --- फागुन २०८३ (Falgun 2083) ---
        Holiday(2083, 11, 1, "Falgun Sankranti (Kumbha Sankranti)", "फागुन संक्रान्ति (कुम्भ संक्रान्ति)"),
        Holiday(2083, 11, 5, "Jaya Ekadashi Vrat", "जया एकादशी व्रत"),
        Holiday(2083, 11, 7, "Prajatantra Diwas (Democracy Day)", "राष्ट्रिय प्रजातन्त्र दिवस"),
        Holiday(2083, 11, 8, "Swasthani Vrat Samapti / Purnima Vrat", "श्री स्वस्थानी व्रत समापन / पूर्णिमा व्रत"),
        Holiday(2083, 11, 19, "Vijaya Ekadashi Vrat", "विजया एकादशी व्रत"),
        Holiday(2083, 11, 22, "Gyalpo Lhosar", "ग्याल्पो ल्होसार (शेर्पा नयाँ वर्ष)"),
        Holiday(2083, 11, 23, "Maha Shivaratri", "महाशिवरात्रि व्रत"),
        Holiday(2083, 11, 24, "International Women's Day / Aunsi", "अन्तर्राष्ट्रिय महिला दिवस / दर्श औंसी"),

        // --- चैत २०८३ (Chaitra 2083) ---
        Holiday(2083, 12, 1, "Chaitra Sankranti (Meen Sankranti)", "चैत संक्रान्ति (मीन संक्रान्ति)"),
        Holiday(2083, 12, 4, "Amalaki Ekadashi Vrat", "आमलकी एकादशी व्रत"),
        Holiday(2083, 12, 7, "Fagu Purnima (Holi - Hilly Region)", "फागु पूर्णिमा (पहाडी होली)"),
        Holiday(2083, 12, 8, "Fagu Purnima (Holi - Terai Region) / Purnima Vrat", "फागु पूर्णिमा (तराई होली) / पूर्णिमा व्रत"),
        Holiday(2083, 12, 19, "Papamochani Ekadashi Vrat", "पापमोचिनी एकादशी व्रत"),
        Holiday(2083, 12, 22, "Ghode Jatra (Pahan Chahre)", "घोडेजात्रा (पाहाँ चह्रे)"),
        Holiday(2083, 12, 23, "Chaitra Aunsi", "चैत्र दर्श औंसी"),
        Holiday(2083, 12, 24, "Basanti Navaratrarambha", "वासन्ती नवरात्रारम्भ (चैते दशैं प्रारम्भ)"),
        Holiday(2083, 12, 28, "Chaite Dashain", "चैते दशैं"),
        Holiday(2083, 12, 29, "Shree Ram Navami Vrat", "श्रीराम नवमी व्रत")
    )

    fun getHoliday(bsYear: Int, bsMonth: Int, bsDay: Int): Holiday? {
        val yearSpecific = holidays.find { it.bsYear == bsYear && it.bsMonth == bsMonth && it.bsDay == bsDay }
        if (yearSpecific != null) return yearSpecific
        return holidays.find { it.bsYear == null && it.bsMonth == bsMonth && it.bsDay == bsDay }
    }

    fun getHolidaysForMonth(bsYear: Int, bsMonth: Int): List<Holiday> {
        val yearHolidays = holidays.filter { it.bsYear == bsYear && it.bsMonth == bsMonth }
        val generalHolidays = holidays.filter { it.bsYear == null && it.bsMonth == bsMonth }
        val coveredDays = yearHolidays.map { it.bsDay }.toSet()
        return (yearHolidays + generalHolidays.filter { it.bsDay !in coveredDays }).sortedBy { it.bsDay }
    }
}
