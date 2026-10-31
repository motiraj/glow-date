@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

enum class FestivalCategory {
    MAJOR_FESTIVAL,    // दशैं, तिहार, छठ, शिवरात्रि, होली, तीज, बुद्ध जयन्ती
    VRATA_FAST,        // एकादशी, प्रदोष, जनैपूर्णिमा, सत्यनारायण
    JAYANTI,           // रामनवमी, कृष्ण जन्माष्टमी, सरस्वती पूजा, गीता जयन्ती
    REGIONAL_CULTURAL, // इन्द्रजात्रा, बिस्केट, माघी, ल्होसार, उँभौली, उधौली
    SOLAR_PARVA        // नयाँ वर्ष, माघे संक्रान्ति, साउने संक्रान्ति, राष्ट्रिय पर्व
}

data class FestivalInfo(
    val id: String,
    val name: String,
    val englishName: String,
    val category: FestivalCategory,
    val significance: String,
    val ritualsAndRules: String,
    val isMajor: Boolean = false,
    val customGreeting: String = ""
)

object ParvaFestivalData {

    /**
     * Resolves festival information for a given BS date and Tithi.
     */
    fun getFestivalForDate(
        bsYear: Int,
        bsMonth: Int,
        bsDay: Int,
        tithi: String
    ): FestivalInfo? {
        val cleanTithi = tithi.trim()
        val isShukla = cleanTithi.contains("शुक्ल")
        val isKrishna = cleanTithi.contains("कृष्ण")

        // 0. Year-Specific Exact Holiday & Festival (वि.सं. २०८३ लगायत आधिकारिक पात्रो अनुसार)
        val yearSpecificHoliday = HolidayData.getHoliday(bsYear, bsMonth, bsDay)
        if (yearSpecificHoliday != null && yearSpecificHoliday.bsYear == bsYear) {
            val isMajor = yearSpecificHoliday.titleNepali.contains("दशैं") ||
                    yearSpecificHoliday.titleNepali.contains("तिहार") ||
                    yearSpecificHoliday.titleNepali.contains("टीका") ||
                    yearSpecificHoliday.titleNepali.contains("छठ") ||
                    yearSpecificHoliday.titleNepali.contains("नयाँ वर्ष") ||
                    yearSpecificHoliday.titleNepali.contains("शिवरात्रि") ||
                    yearSpecificHoliday.titleNepali.contains("होली") ||
                    yearSpecificHoliday.titleNepali.contains("तीज") ||
                    yearSpecificHoliday.titleNepali.contains("बुद्ध जयन्ती") ||
                    yearSpecificHoliday.titleNepali.contains("जन्माष्टमी") ||
                    yearSpecificHoliday.titleNepali.contains("रक्षाबन्धन") ||
                    yearSpecificHoliday.titleNepali.contains("जनै पूर्णिमा")
            return FestivalInfo(
                id = "holiday_${bsYear}_${bsMonth}_$bsDay",
                name = yearSpecificHoliday.titleNepali,
                englishName = yearSpecificHoliday.title,
                category = if (isMajor) FestivalCategory.MAJOR_FESTIVAL else FestivalCategory.SOLAR_PARVA,
                significance = "नेपाली पात्रो अनुसार वि.सं. $bsYear को पावन अवसर: ${yearSpecificHoliday.titleNepali}।",
                ritualsAndRules = "यस दिनको विशेष परम्परा अनुसार पूजा-अर्चना, पारिवारिक भेटघाट तथा शुभकामना आदानप्रदान गर्नुहोस्।",
                isMajor = isMajor,
                customGreeting = "${yearSpecificHoliday.titleNepali}को हार्दिक शुभकामना!"
            )
        }

        // 1. Ekadashi Check (Priority 1 for Vrata)
        if (cleanTithi.contains("एकादशी")) {
            val ekadashi = EkadashiData.getEkadashiForMonthAndPaksha(bsMonth, isShukla)
            if (ekadashi != null) {
                return FestivalInfo(
                    id = "ekadashi_${bsMonth}_${if (isShukla) "shukla" else "krishna"}",
                    name = ekadashi.name,
                    englishName = ekadashi.name,
                    category = FestivalCategory.VRATA_FAST,
                    significance = ekadashi.significance,
                    ritualsAndRules = "भगवान् श्रीहरि नारायणको पूजा, तुलसी आराधना, अन्न त्याग र सात्त्विक नियम पालना गर्नुहोस्।",
                    isMajor = ekadashi.isMajor,
                    customGreeting = "${ekadashi.name} व्रतको पावन शुभकामना!"
                )
            }
        }

        // 2. Solar & Fixed-Day Parvas (सौर्य महिना तथा गते अनुसार)
        when (bsMonth) {
            1 -> when (bsDay) {
                1 -> return FestivalInfo(
                    id = "new_year",
                    name = "नयाँ वर्ष तथा मेष संक्रान्ति",
                    englishName = "Nepali New Year",
                    category = FestivalCategory.SOLAR_PARVA,
                    significance = "विक्रम संवत्को नयाँ वर्षको पहिलो दिन र सूर्य मेष राशिमा प्रवेश गर्ने दिन। नयाँ संकल्प र उमंगका साथ मनाइन्छ।",
                    ritualsAndRules = "बिहानै स्नान गरी इष्टदेवको पूजा गर्ने, मीठो परिकार खाने र मान्यजनबाट आशीर्वाद लिने।",
                    isMajor = true,
                    customGreeting = "नयाँ वर्षको हार्दिक मङ्गलमय शुभकामना!"
                )
                2 -> return FestivalInfo(
                    id = "judsheetal",
                    name = "जुडशीतल तथा सिन्दूर जात्रा",
                    englishName = "Jud Sheetal",
                    category = FestivalCategory.REGIONAL_CULTURAL,
                    significance = "मिथिला क्षेत्रमा नयाँ वर्षको शीतलता र आशिष बाँड्ने पर्व तथा भक्तपुर थिमीमा सिन्दूर जात्रा।",
                    ritualsAndRules = "ज्येष्ठ सदस्यबाट शिरमा जल थापेर आशीर्वाद लिने र रूखबिरुवामा पानी हाल्ने।",
                    isMajor = false
                )
            }
            3 -> when (bsDay) {
                15 -> return FestivalInfo(
                    id = "asadh_15",
                    name = "असार १५ (राष्ट्रिय धान दिवस तथा दही-चिउरा खाने दिन)",
                    englishName = "National Paddy Day (Asadh 15)",
                    category = FestivalCategory.SOLAR_PARVA,
                    significance = "नेपालको कृषि संस्कृति झल्काउने रोपाईं उत्सव र दही-चिउरा खाएर ऊर्जा प्राप्त गर्ने दिन।",
                    ritualsAndRules = "खेतमा धान रोप्ने, असारे गीत गाउने, हिलो छ्यापाछ्याप गर्ने र सपरिवार दही-चिउरा खाने।",
                    isMajor = true,
                    customGreeting = "राष्ट्रिय धान दिवस तथा असार १५ को शुभकामना!"
                )
            }
            4 -> when (bsDay) {
                1 -> return FestivalInfo(
                    id = "saune_sankranti",
                    name = "साउने संक्रान्ति (कर्कट संक्रान्ति)",
                    englishName = "Saune Sankranti",
                    category = FestivalCategory.SOLAR_PARVA,
                    significance = "सूर्य दक्षिणायन हुने दिन। खेतबारीमा रोपाईं सकिएपछि विश्राम लिने र लुतो फालेर रोगव्याधि पन्छाउने दिन।",
                    ritualsAndRules = "कण्डारक पूजा गरी साँझ लुतो फाल्ने, मीठा परिकार खाने र हरियो चुरा-मेहन्दी लगाउने।",
                    isMajor = true,
                    customGreeting = "साउने संक्रान्तिको हार्दिक शुभकामना!"
                )
                15 -> return FestivalInfo(
                    id = "shrawan_15",
                    name = "साउन १५ (खीर खाने दिन)",
                    englishName = "Shrawan 15 (Kheer Khane Din)",
                    category = FestivalCategory.SOLAR_PARVA,
                    significance = "किसानहरूले खेतको काम सकेर शरीरलाई पुष्ट र निरोगी राख्न मीठो खीर बनाएर खाने परम्परा।",
                    ritualsAndRules = "भगवान् शिव र कुलदेवतालाई खीर अर्पण गरी प्रसादस्वरूप सपरिवार खीर खाने।",
                    isMajor = false,
                    customGreeting = "साउन १५ (खीर खाने दिन) को हार्दिक शुभकामना!"
                )
            }
            6 -> when (bsDay) {
                1 -> return FestivalInfo(
                    id = "vishwakarma_puja",
                    name = "विश्वकर्मा पूजा (कन्या संक्रान्ति)",
                    englishName = "Vishwakarma Puja",
                    category = FestivalCategory.SOLAR_PARVA,
                    significance = "वास्तुशिल्प र यन्त्र-औजारका अधिपति भगवान विश्वकर्माको पूजा आराधना गर्ने दिन।",
                    ritualsAndRules = "कलकारखाना, सवारी साधन, यन्त्र र औजारहरूको सरसफाइ गरी विशेष पूजा गर्ने।",
                    isMajor = true,
                    customGreeting = "विश्वकर्मा पूजाको हार्दिक शुभकामना!"
                )
                3 -> return FestivalInfo(
                    id = "constitution_day",
                    name = "संविधान दिवस",
                    englishName = "Constitution Day",
                    category = FestivalCategory.SOLAR_PARVA,
                    significance = "नेपालको नयाँ संविधान जारी भएको ऐतिहासिक राष्ट्रिय दिवस।",
                    ritualsAndRules = "दीपावली गर्ने, राष्ट्रिय एकता र समृद्धिको कामना गर्ने।",
                    isMajor = false
                )
            }
            9 -> when (bsDay) {
                15 -> return FestivalInfo(
                    id = "tamu_lhosar",
                    name = "तमु ल्होसार तथा पुस १५ (लट्टे खाने दिन)",
                    englishName = "Tamu Lhosar & Poush 15",
                    category = FestivalCategory.REGIONAL_CULTURAL,
                    significance = "गुरुङ समुदायको नयाँ वर्ष र पुस १५ मा लट्टे र खीर खाएर जाडो भगाउने नेपाली सांस्कृतिक पर्व।",
                    ritualsAndRules = "पारम्परिक भेषभूषामा सजिएर गुम्बा जाने, लामा गुरुबाट आशीर्वाद लिने र अनदीको लट्टे-सेलरोटी खाने।",
                    isMajor = true,
                    customGreeting = "तमु ल्होसार तथा पुस १५ को हार्दिक मङ्गलमय शुभकामना!"
                )
            }
            10 -> when (bsDay) {
                1 -> return FestivalInfo(
                    id = "maghe_sankranti",
                    name = "माघे संक्रान्ति (मकर संक्रान्ति / माघी)",
                    englishName = "Maghe Sankranti",
                    category = FestivalCategory.SOLAR_PARVA,
                    significance = "सूर्य धनुबाट मकर राशिमा प्रवेश गरी उत्तरायण प्रारम्भ हुने दिन। थारु समुदायको माघी नयाँ वर्ष।",
                    ritualsAndRules = "पवित्र नदी-तीर्थमा मकर स्नान, घिउ, चाकु, तिलको लड्डु, तरुल, खिचडी दान तथा सेवन गर्ने।",
                    isMajor = true,
                    customGreeting = "माघे संक्रान्ति तथा माघी पर्वको शुभकामना!"
                )
            }
        }

        // 3. Lunar Tithi-Based Great Festivals (बडादशैं, तिहार, छठ, शिवरात्रि, तीज आदि)
        when (bsMonth) {
            // वैशाख (Baisakh)
            1 -> {
                if (isKrishna && cleanTithi.contains("औंसी")) {
                    return FestivalInfo(
                        id = "matatirtha_aunsi",
                        name = "मातातीर्थ औंसी (आमाको मुख हेर्ने दिन)",
                        englishName = "Mother's Day (Matatirtha Aunsi)",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "आमाप्रति असीम सम्मान, कृतज्ञता र भक्ति प्रकट गर्ने पवित्र दिन।",
                        ritualsAndRules = "आमालाई मीठो भोजन, फलफूल, वस्त्र उपहार दिई आशीर्वाद लिने। मातातीर्थ कुण्डमा श्राद्ध गर्ने।",
                        isMajor = true,
                        customGreeting = "मातातीर्थ औंसी (आमाको मुख हेर्ने दिन) को शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("तृतीया")) {
                    return FestivalInfo(
                        id = "akshaya_tritiya",
                        name = "अक्षय तृतीया",
                        englishName = "Akshaya Tritiya",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "यस दिन गरेको दान, होम र शुभ कार्यको कहिल्यै क्षय नहुने महापवित्र दिन। त्रेतायुगको प्रारम्भ भएको दिन।",
                        ritualsAndRules = "जौको सातु र चिसो सर्वत दान गर्ने, सेवन गर्ने र जलपूर्ण घडा दान गर्ने।",
                        isMajor = true,
                        customGreeting = "अक्षय तृतीयाको पावन शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("नवमी")) {
                    return FestivalInfo(
                        id = "sita_jayanti",
                        name = "जानकी जयन्ती (सीता नवमी)",
                        englishName = "Sita Jayanti",
                        category = FestivalCategory.JAYANTI,
                        significance = "मिथिलाकी राजकुमारी तथा माता जानकी सीताको प्राकट्य दिवस।",
                        ritualsAndRules = "माता सीता र भगवान् श्रीरामको पूजा, जनकपुरधाममा विशेष उत्सव र रामायण पाठ।",
                        isMajor = true,
                        customGreeting = "सीता नवमी तथा जानकी जयन्तीको शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("पूर्णिमा")) {
                    return FestivalInfo(
                        id = "buddha_jayanti",
                        name = "बुद्ध जयन्ती तथा उँभौली पर्व",
                        englishName = "Buddha Jayanti & Ubhauli",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "भगवान् गौतम बुद्धको जन्म, ज्ञान प्राप्ति र महापरिनिर्वाणको त्रिसंयोग दिवस तथा किरात समुदायको उँभौली पूजा।",
                        ritualsAndRules = "अहिंसा, करुणा, ध्यान र बुद्धका उपदेशहरूको स्मरण, दीप प्रज्वलन र साकेला नाच।",
                        isMajor = true,
                        customGreeting = "बुद्ध जयन्ती तथा उँभौलीको हार्दिक शुभकामना!"
                    )
                }
            }

            // जेठ (Jestha)
            2 -> {
                if (isShukla && cleanTithi.contains("दशमी")) {
                    return FestivalInfo(
                        id = "ganga_dussehra",
                        name = "गंगा दशहरा",
                        englishName = "Ganga Dussehra",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "भागीरथको तपस्याबाट स्वर्गबाट धर्तीमा पावन भागीरथी गंगाको अवतरण भएको दिन।",
                        ritualsAndRules = "पवित्र नदीमा स्नान, १० प्रकारका पुष्प-फल अर्पण र १० प्रकारका पाप निवारणको प्रार्थना।",
                        isMajor = false,
                        customGreeting = "गंगा दशहराको पावन शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("पूर्णिमा")) {
                    return FestivalInfo(
                        id = "sithi_nakha",
                        name = "वट सावित्री व्रत तथा सिथि नखः",
                        englishName = "Vat Savitri & Sithi Nakha",
                        category = FestivalCategory.REGIONAL_CULTURAL,
                        significance = "सती सावित्रीले यमराजबाट पतिको प्राण फिर्ता ल्याएको सम्झनामा वट सावित्री व्रत र इनार-कुवा सफा गर्ने सिथि नखः।",
                        ritualsAndRules = "बरको पूजा, अखण्ड सौभाग्यको कामना तथा परम्परागत मुहान, इनार र पोखरीको सरसफाइ।",
                        isMajor = true,
                        customGreeting = "वट सावित्री तथा सिथि नखःको शुभकामना!"
                    )
                }
            }

            // आषाढ (Asadh)
            3 -> {
                if (isShukla && cleanTithi.contains("पूर्णिमा")) {
                    return FestivalInfo(
                        id = "guru_purnima",
                        name = "गुरु पूर्णिमा तथा व्यास जयन्ती",
                        englishName = "Guru Purnima",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "अज्ञानको अन्धकार हटाएर ज्ञानको दिव्य प्रकाश दिने गुरुहरूप्रति श्रद्धा अर्पण गर्ने दिन।",
                        ritualsAndRules = "आफ्ना गुरु, शिक्षक र माता-पितालाई नमन गरी वस्त्र, उपहार र कृतज्ञता अर्पण गर्ने।",
                        isMajor = true,
                        customGreeting = "गुरु पूर्णिमाको हार्दिक श्रद्धाञ्जली तथा शुभकामना!"
                    )
                }
            }

            // श्रावण (Shrawan)
            4 -> {
                if (isKrishna && cleanTithi.contains("चतुर्दशी")) {
                    return FestivalInfo(
                        id = "gathe_mangal",
                        name = "घण्टाकर्ण चतुर्दशी (गठेमङ्गल)",
                        englishName = "Ghantakarna / Gathe Mangal",
                        category = FestivalCategory.REGIONAL_CULTURAL,
                        significance = "उपत्यकाको प्राचीन पर्व। दुष्ट दानव घण्टाकर्णको दहन गरी रोगव्याधि र नकारात्मक ऊर्जा भगाउने दिन।",
                        ritualsAndRules = "चौबाटोमा नर्कटको घण्टाकर्ण ठड्याएर साँझ दहन गर्ने र फलामको औंठी लगाउने।",
                        isMajor = false
                    )
                }
                if (isShukla && cleanTithi.contains("पञ्चमी")) {
                    return FestivalInfo(
                        id = "nag_panchami",
                        name = "नागपञ्चमी",
                        englishName = "Nag Panchami",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "नाग देवताको पूजा आराधना गरी चट्याङ, सर्पभय र अनिष्टबाट सुरक्षा पाउने धार्मिक पर्व।",
                        ritualsAndRules = "ढोकामा गाईको गोबरले नागको चित्र टाँस्ने, गाईको दूध, दुबो र लाभा चढाउने।",
                        isMajor = true,
                        customGreeting = "नागपञ्चमीको पावन शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("पूर्णिमा")) {
                    return FestivalInfo(
                        id = "janai_purnima",
                        name = "जनै पूर्णिमा, रक्षाबन्धन तथा ऋषि तर्पणी",
                        englishName = "Janai Purnima & Rakshabandhan",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "पवित्र यज्ञोपवीत (जनै) धारण, रक्षा-सूत्र बाँध्ने र नौ प्रकारका गेडागुडी मिसाइएको क्वाँटी खाने दिन।",
                        ritualsAndRules = "सप्तऋषि तर्पण गरी नयाँ जनै फेर्ने, दिदीबहिनीबाट दाजुभाइलाई रक्षाबन्धन बाँध्ने।",
                        isMajor = true,
                        customGreeting = "जनै पूर्णिमा तथा रक्षाबन्धनको हार्दिक शुभकामना!"
                    )
                }
            }

            // भाद्र (Bhadra)
            5 -> {
                if (isKrishna && cleanTithi.contains("प्रतिपदा")) {
                    return FestivalInfo(
                        id = "gai_jatra",
                        name = "गाईजात्रा (सापारु)",
                        englishName = "Gai Jatra",
                        category = FestivalCategory.REGIONAL_CULTURAL,
                        significance = "वर्षभरि दिवङ्गत आफन्तको सद्गतिको कामना गर्दै गाई वा बालबालिकालाई गाईको रूपमा सिँगारी नगर परिक्रमा गराउने पर्व।",
                        ritualsAndRules = "हाँस्य-व्यंग्य, प्रहसन र सांस्कृतिक झाँकीहरू प्रदर्शन गर्ने।",
                        isMajor = true
                    )
                }
                if (isKrishna && cleanTithi.contains("अष्टमी")) {
                    return FestivalInfo(
                        id = "krishna_janmashtami",
                        name = "श्रीकृष्ण जन्माष्टमी",
                        englishName = "Krishna Janmashtami",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "अधर्मको नाश र धर्मको स्थापनाका लागि द्वापरयुगमा भगवान् श्रीकृष्णको धर्तीमा प्राकट्य भएको दिन।",
                        ritualsAndRules = "दिनभर उपवास, मध्यरातमा श्रीकृष्ण जन्मोत्सव, पञ्चामृत अभिषेक र गीता पाठ।",
                        isMajor = true,
                        customGreeting = "श्रीकृष्ण जन्माष्टमीको हार्दिक मङ्गलमय शुभकामना!"
                    )
                }
                if (isKrishna && cleanTithi.contains("औंसी")) {
                    return FestivalInfo(
                        id = "kushe_aunsi",
                        name = "कुशे औंसी (बाबुको मुख हेर्ने दिन)",
                        englishName = "Father's Day (Kushe Aunsi)",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "पिताप्रति आदर, कृतज्ञता प्रकट गर्ने दिन तथा वर्षभरिका धार्मिक कार्यका लागि पवित्र कुश घरमा भित्र्याउने दिन।",
                        ritualsAndRules = "पितालाई मीठो भोजन, फलफूल र वस्त्र अर्पण गरी गोडा ढोग्ने, गोकर्णेश्वरमा श्राद्ध गर्ने।",
                        isMajor = true,
                        customGreeting = "कुशे औंसी (बाबुको मुख हेर्ने दिन) को शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("तृतीया")) {
                    return FestivalInfo(
                        id = "haritalika_teej",
                        name = "हरितालिका तीज व्रत",
                        englishName = "Haritalika Teej",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "माता पार्वतीले भगवान् शिवजीलाई पतिको रूपमा पाउन गरेको कठोर तपस्याको स्मरण। सौभाग्य र पारिवारिक सुखको महान् पर्व।",
                        ritualsAndRules = "अघिल्लो साँझ दर खाने, तीजको दिन निराहार व्रत बस्ने, शिव-पार्वती पूजा र हरियो/रातो पहिरनमा भजन-नाचगान।",
                        isMajor = true,
                        customGreeting = "हरितालिका तीजको हार्दिक मङ्गलमय शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("चतुर्थी")) {
                    return FestivalInfo(
                        id = "ganesh_chaturthi",
                        name = "गणेश चतुर्थी तथा चौठचन्द्र (चौरचन)",
                        englishName = "Ganesh Chaturthi & Chourchan",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "विघ्नहर्ता भगवान् गणेशको जन्मोत्सव तथा मिथिलामा रोहिणी/चन्द्रमाको पूजा गर्ने पर्व।",
                        ritualsAndRules = "गणेशजीलाई मोदक र दुबो अर्पण गर्ने, साँझ चन्द्रमालाई फलफूल-दहीको अर्घ्य दिने।",
                        isMajor = true,
                        customGreeting = "गणेश चतुर्थीको हार्दिक मङ्गलमय शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("पञ्चमी")) {
                    return FestivalInfo(
                        id = "rishi_panchami",
                        name = "ऋषि पञ्चमी",
                        englishName = "Rishi Panchami",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "सप्तऋषिहरूको विधिपूर्वक पूजा आराधना गरी अन्जानमा भएका पाप र दोषहरूबाट मुक्ति पाउने दिन।",
                        ritualsAndRules = "३६५ दतिवनले दाँत माझ्ने, पवित्र जलमा अपामार्ग स्नान र कश्यपादि सप्तऋषि पूजा।",
                        isMajor = true,
                        customGreeting = "ऋषि पञ्चमीको पावन शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("चतुर्दशी")) {
                    return FestivalInfo(
                        id = "indra_jatra",
                        name = "इन्द्रजात्रा तथा अनन्त चतुर्दशी",
                        englishName = "Indra Jatra & Ananta Chaturdashi",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "वर्षा र सहकालका देवता इन्द्रको पूजा, जीवित देवी श्री कुमारीको रथयात्रा तथा अनन्त भगवानको पवित्र सूत्र धारण।",
                        ritualsAndRules = "वसन्तपुरमा यःसिं (लिङ्गो) ठड्याउने, लाखे नाच, श्वेत भैरवको प्रसाद र अनन्त डोरो बाँध्ने।",
                        isMajor = true,
                        customGreeting = "इन्द्रजात्रा तथा अनन्त चतुर्दशीको शुभकामना!"
                    )
                }
            }

            // आश्विन (Ashwin - Dashain Season)
            6 -> {
                if (isShukla && cleanTithi.contains("प्रतिपदा")) {
                    return FestivalInfo(
                        id = "ghatasthapana",
                        name = "घटस्थापना (बडादशैं प्रारम्भ)",
                        englishName = "Ghatasthapana (Dashain Start)",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "नवरात्रिको पहिलो दिन। घरघरमा विधिपूर्वक घट स्थापना गरी जमरा राख्ने र शैलपुत्री भगवतीको पूजा गर्ने दिन।",
                        ritualsAndRules = "जौ र मकैको जमरा राख्ने, दुर्गा सप्तशती (चण्डी) पाठ प्रारम्भ गर्ने।",
                        isMajor = true,
                        customGreeting = "बडादशैं घटस्थापनाको मङ्गलमय शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("सप्तमी")) {
                    return FestivalInfo(
                        id = "phoolpati",
                        name = "फूलपाती (नवपत्रिका प्रवेश)",
                        englishName = "Phoolpati",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "दशैंको सातौं दिन। नौ प्रकारका वनस्पति (नवपत्रिका) लाई पूजा कोठामा भित्र्याएर हर्षोल्लास मनाउने दिन।",
                        ritualsAndRules = "कालरात्रि भगवतीको पूजा र दशैंको औपचारिक रौनक सुरु।",
                        isMajor = true,
                        customGreeting = "फूलपातीको शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("अष्टमी")) {
                    return FestivalInfo(
                        id = "maha_ashtami",
                        name = "महाअष्टमी तथा कालरात्रि पूजा",
                        englishName = "Maha Ashtami",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "आसुरी शक्तिमाथि दैवी शक्तिको विजयको प्रतीक। माता महागौरी र कालरात्रिको उग्र एवं कल्याणकारी रूपको पूजा।",
                        ritualsAndRules = "शक्ति पीठहरूमा पूजा, कलश पूजा, हातहतियार र औजारहरूको विशेष पूजा।",
                        isMajor = true,
                        customGreeting = "महाअष्टमीको हार्दिक शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("नवमी")) {
                    return FestivalInfo(
                        id = "maha_navami",
                        name = "महानवमी (सिद्धिदात्री पूजा)",
                        englishName = "Maha Navami",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "नवदुर्गाको अन्तिम रूप सिद्धिदात्रीको पूजा, विश्वकर्मा तथा सवारीसाधनको पूजा।",
                        ritualsAndRules = "कोत पूजा, गाडी र मेसिनरी औजारहरूको पूजा, कन्या पूजा।",
                        isMajor = true,
                        customGreeting = "महानवमीको मङ्गलमय शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("दशमी")) {
                    return FestivalInfo(
                        id = "vijaya_dashami",
                        name = "विजयादशमी (दशैंको मुख्य टीका)",
                        englishName = "Vijaya Dashami",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "असत्यमाथि सत्य र महिषासुरमाथि भगवती दुर्गाको विजयको महान् उत्सव। नेपालीहरूको सबैभन्दा ठूलो राष्ट्रिय पर्व।",
                        ritualsAndRules = "शुभ साइतमा देवी विसर्जन, मान्यजनबाट रातो अक्षता-टीका र पहेँलो जमरा लगाई दीर्घायुको आशीर्वाद लिने।",
                        isMajor = true,
                        customGreeting = "बडादशैं विजयादशमीको हार्दिक मङ्गलमय शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("पूर्णिमा")) {
                    return FestivalInfo(
                        id = "kojagrat_purnima",
                        name = "कोजाग्रत पूर्णिमा (दशैं समापन)",
                        englishName = "Kojagrat Purnima",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "'को जागेको छ?' भनी महालक्ष्मी राति पृथ्वीमा विचरण गर्ने र जाग्राम बस्नेलाई धनधान्य दिने विश्वास। दशैंको समापन।",
                        ritualsAndRules = "रातभर जाग्राम बस्ने, महालक्ष्मी र इन्द्रदेवको पूजा, पासा खेल्ने।",
                        isMajor = true,
                        customGreeting = "कोजाग्रत पूर्णिमाको शुभकामना!"
                    )
                }
            }

            // कार्तिक (Kartik - Tihar Season)
            7 -> {
                if (isKrishna && cleanTithi.contains("त्रयोदशी")) {
                    return FestivalInfo(
                        id = "kag_tihar",
                        name = "काग तिहार तथा धनतेरस",
                        englishName = "Kag Tihar & Dhanteras",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "यमपञ्चक प्रारम्भ। यमदूत कागलाई पूजा गर्ने तथा आयुर्वेदका जनक धन्वन्तरी जयन्ती र धनतेरस खरिदको दिन।",
                        ritualsAndRules = "कागलाई पहिलो गाँस खान दिने, धनतेरसमा सुन, चाँदी वा नयाँ धातुका भाँडा किन्ने।",
                        isMajor = true,
                        customGreeting = "यमपञ्चक काग तिहार तथा धनतेरसको शुभकामना!"
                    )
                }
                if (isKrishna && cleanTithi.contains("चतुर्दशी")) {
                    return FestivalInfo(
                        id = "kukur_tihar",
                        name = "कुकुर तिहार तथा नरक चतुर्दशी",
                        englishName = "Kukur Tihar & Narak Chaturdashi",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "इमान्दार र स्वामीभक्त कुकुरको पूजा तथा यमदीपदान गरी नरक भयबाट मुक्ति पाउने दिन।",
                        ritualsAndRules = "कुकुरलाई माला र टीका लगाई मीठो खानेकुरा दिने, साँझ चारमुखे बत्ती बाल्ने।",
                        isMajor = true,
                        customGreeting = "कुकुर तिहारको शुभकामना!"
                    )
                }
                if (isKrishna && cleanTithi.contains("औंसी")) {
                    return FestivalInfo(
                        id = "laxmi_puja",
                        name = "लक्ष्मीपूजा तथा दीपावली (सुखरात्रि)",
                        englishName = "Laxmi Puja & Deepawali",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "धन, ऐश्वर्य र समृद्धिकी देवी महालक्ष्मीको भव्य पूजा। घरघरमा दीपमाला बालेर अन्धकार हटाउने पर्व।",
                        ritualsAndRules = "घर सफा गरी रातो माटो/रंगोली बनाउने, दीप प्रज्वलन, धन-लक्ष्मी पूजा र भैलो खेल्ने।",
                        isMajor = true,
                        customGreeting = "दीपावली तथा लक्ष्मीपूजाको हार्दिक मङ्गलमय शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("प्रतिपदा")) {
                    return FestivalInfo(
                        id = "govardhan_puja",
                        name = "गोवर्द्धन पूजा, गाई/गोरु पूजा तथा म्ह पूजा (नेपाल संवत् न्हूदँ)",
                        englishName = "Govardhan Puja & Mha Puja",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "प्रकृति र गाई-गोरुको पूजा, नेवार समुदायमा आत्मपूजा (म्ह पूजा) र नेपाल संवत् नयाँ वर्ष प्रारम्भ।",
                        ritualsAndRules = "गाई-गोरुलाई माला र पिठो खुवाउने, गोवर्द्धन पर्वत बनाएर पूजा गर्ने, देउसी खेल्ने।",
                        isMajor = true,
                        customGreeting = "गोवर्द्धन पूजा तथा नेपाल संवत् न्हूदँको शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("द्वितीया")) {
                    return FestivalInfo(
                        id = "bhai_tika",
                        name = "भाइटीका (यमद्वितीया)",
                        englishName = "Bhai Tika",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "दिदीबहिनी र दाजुभाइको प्रगाढ प्रेम र आत्मीयताको महान् पर्व। यमराजले बहिनी यमुनाको हातबाट टीका लगाएको दिन।",
                        ritualsAndRules = "ओखर फोरेर यमराजको मार्ग छेक्ने, सप्तरङ्गी टीका, मखमली-सयपत्री माला र भाइ-मसाला अर्पण।",
                        isMajor = true,
                        customGreeting = "भाइटीकाको हार्दिक मङ्गलमय शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("षष्ठी")) {
                    return FestivalInfo(
                        id = "chhath_parva",
                        name = "छठ पर्व (सूर्य षष्ठी व्रत)",
                        englishName = "Chhath Parva",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "प्रत्यक्ष देवता सूर्य र षष्ठी देवीको निष्ठापूर्वक आराधना। आरोग्यता, सन्तान सुख र समृद्धिको महापर्व।",
                        ritualsAndRules = "जलाशयमा उभिई अस्ताउँदो र उदाउँदो सूर्यलाई ठेकुवा, भुसुवा र फलफूलको अर्घ्य दिने।",
                        isMajor = true,
                        customGreeting = "छठ महापर्वको हार्दिक शुभकामना!"
                    )
                }
            }

            // मङ्सिर (Mangsir)
            8 -> {
                if (isShukla && cleanTithi.contains("पञ्चमी")) {
                    return FestivalInfo(
                        id = "vivah_panchami",
                        name = "विवाहपञ्चमी",
                        englishName = "Vivaha Panchami",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "त्रेतायुगमा मिथिला नरेश जनककी पुत्री सीता र अयोध्याका राजकुमार श्रीरामको दिव्य विवाह भएको दिन।",
                        ritualsAndRules = "जनकपुरधाममा भव्य वैवाहिक उत्सव, राम-जानकी मन्दिरमा विशेष पूजा र डोला भ्रमण।",
                        isMajor = true,
                        customGreeting = "विवाहपञ्चमीको पावन शुभकामना!"
                    )
                }
                if (isKrishna && cleanTithi.contains("चतुर्दशी")) {
                    return FestivalInfo(
                        id = "bala_chaturdashi",
                        name = "बाला चतुर्दशी (शतबीज छर्ने दिन)",
                        englishName = "Bala Chaturdashi",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "दिवङ्गत पितृहरूको मोक्ष र सद्गतिको कामना गर्दै शतबीज (सातथरी अन्न) छर्ने दिन।",
                        ritualsAndRules = "रातभर दीप प्रज्वलन गरी जाग्राम बस्ने, बिहानै मृगस्थली/श्लेष्मान्तक वनमा शतबीज छर्ने।",
                        isMajor = true
                    )
                }
                if (isShukla && cleanTithi.contains("पूर्णिमा")) {
                    return FestivalInfo(
                        id = "dhanya_purnima",
                        name = "धान्य पूर्णिमा, योमरी पुन्ही तथा उधौली पर्व",
                        englishName = "Dhanya Purnima, Yomari Punhi & Udhauli",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "नयाँ धान भित्र्याएको खुसीयाली, नेवार समुदायमा नयाँ चामलको योमरी खाने पर्व तथा किरात समुदायको उधौली साकेला।",
                        ritualsAndRules = "अन्नपूर्णेश्वरी भगवतीको पूजा, योमरी बनाएर खाने र बाँड्ने, साकेला सिली नाच्ने।",
                        isMajor = true,
                        customGreeting = "धान्य पूर्णिमा, योमरी पुन्ही तथा उधौलीको शुभकामना!"
                    )
                }
            }

            // माघ (Magh)
            10 -> {
                if (isKrishna && cleanTithi.contains("औंसी")) {
                    return FestivalInfo(
                        id = "mauni_aunsi",
                        name = "मौनी औंसी (माघी औंसी)",
                        englishName = "Mauni Amavasya",
                        category = FestivalCategory.VRATA_FAST,
                        significance = "मौन रहेर आत्मचिन्तन र साधना गर्ने तथा पवित्र त्रिवेणी संगममा स्नान गर्दा महापुण्य मिल्ने विश्वास।",
                        ritualsAndRules = "मौन व्रत, तीर्थ स्नान, तिल र कम्बल दान तथा पितृतर्पण।",
                        isMajor = false,
                        customGreeting = "मौनी औंसीको शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("पञ्चमी")) {
                    return FestivalInfo(
                        id = "vasant_panchami",
                        name = "वसन्त पञ्चमी (सरस्वती पूजा / श्रीपञ्चमी)",
                        englishName = "Vasant Panchami & Saraswati Puja",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "विद्या, बुद्धि, कला र संगीतकी देवी माँ सरस्वतीको उत्पत्ति दिवस तथा वसन्त ऋतुको आगमन।",
                        ritualsAndRules = "किताब, कलम र वाद्ययन्त्रको पूजा, बालबालिकालाई अक्षरारम्भ गराउने, पहेँलो वस्त्र धारण।",
                        isMajor = true,
                        customGreeting = "सरस्वती पूजा तथा वसन्त पञ्चमीको शुभकामना!"
                    )
                }
            }

            // फागुन (Falgun)
            11 -> {
                if (isKrishna && cleanTithi.contains("चतुर्दशी")) {
                    return FestivalInfo(
                        id = "maha_shivaratri",
                        name = "महाशिवरात्रि",
                        englishName = "Maha Shivaratri",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "देवाधिदेव महादेव शिवको प्राकट्य रात्रि। आध्यात्मिक ऊर्जा, ध्यान, संयम र मोक्षको महान् पर्व।",
                        ritualsAndRules = "चार प्रहर पूजा, पशुपतिनाथ आदि शिवालयमा जल र बेलपत्र अर्पण, उपवास, धुनी जगाउने र ॐ नमः शिवाय जप।",
                        isMajor = true,
                        customGreeting = "महाशिवरात्रिको हार्दिक मङ्गलमय शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("पूर्णिमा")) {
                    return FestivalInfo(
                        id = "fagu_purnima",
                        name = "फागु पूर्णिमा (होली पर्व)",
                        englishName = "Holi / Fagu Purnima",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "रङ्गहरूको महान् उत्सव। प्रह्लादको भक्ति र होलिका दहनको सम्झनामा असत्यमाथि सत्यको विजयोत्सव।",
                        ritualsAndRules = "एक-आपसमा अबिर र रङ्ग दलेर भ्रातृत्व बाँड्ने, मीठा परिकार खाने।",
                        isMajor = true,
                        customGreeting = "रङ्गहरूको पर्व फागु पूर्णिमा (होली) को शुभकामना!"
                    )
                }
            }

            // चैत (Chaitra)
            12 -> {
                if (isKrishna && cleanTithi.contains("औंसी")) {
                    return FestivalInfo(
                        id = "ghode_jatra",
                        name = "घोडेजात्रा (पाहाँ चह्रे)",
                        englishName = "Ghode Jatra",
                        category = FestivalCategory.REGIONAL_CULTURAL,
                        significance = "टुँडिखेलमा गुरुमापा राक्षसको भय भगाउन घोडा दौडाउने ऐतिहासिक जात्रा र नेवार समुदायको कुलपूजा।",
                        ritualsAndRules = "घोडा दौड प्रतियोगिता, देवीदेवताको खटयात्रा र पाहुनालाई भोज।",
                        isMajor = false
                    )
                }
                if (isShukla && cleanTithi.contains("अष्टमी")) {
                    return FestivalInfo(
                        id = "chaite_dashain",
                        name = "चैते दशैं",
                        englishName = "Chaite Dashain",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "वासन्ती नवरात्रिको मुख्य दिन। शक्तिस्वरूपा देवी भगवतीको पूजा र बली अर्पण गर्ने दिन।",
                        ritualsAndRules = "दुर्गा मन्दिरहरूमा पूजा, कलश पूजा र पारिवारिक भेटघाट।",
                        isMajor = true,
                        customGreeting = "चैते दशैंको शुभकामना!"
                    )
                }
                if (isShukla && cleanTithi.contains("नवमी")) {
                    return FestivalInfo(
                        id = "ram_navami",
                        name = "रामनवमी",
                        englishName = "Ram Navami",
                        category = FestivalCategory.MAJOR_FESTIVAL,
                        significance = "मर्यादा पुरुषोत्तम भगवान् श्रीरामको धर्तीमा जन्मोत्सव। धर्म र सत्यको मार्ग प्रशस्त गर्ने पर्व।",
                        ritualsAndRules = "रामोत्सव, उपवास, रामायण पाठ, श्रीराम मन्दिरहरूमा भजन-कीर्तन।",
                        isMajor = true,
                        customGreeting = "रामनवमीको हार्दिक मङ्गलमय शुभकामना!"
                    )
                }
            }
        }

        // 4. Monthly Recurring Sacred Vratas (प्रदोष व्रत तथा पूर्णिमा व्रत)
        if (cleanTithi.contains("त्रयोदशी")) {
            val pakshaName = if (isShukla) "शुक्ल" else "कृष्ण"
            return FestivalInfo(
                id = "pradosh_vrat_${bsMonth}_${if (isShukla) "shukla" else "krishna"}",
                name = "$pakshaName पक्ष प्रदोष व्रत",
                englishName = "Pradosh Vrat",
                category = FestivalCategory.VRATA_FAST,
                significance = "प्रदोष काल (साँझ) मा भगवान् शिव र माता पार्वतीको विशेष पूजाबाट सम्पूर्ण दोष तथा कष्ट नाश हुने विश्वास।",
                ritualsAndRules = "साँझको समयमा शिवलिंगमा अभिषेक, बेलपत्र अर्पण, शिव पञ्चाक्षर मन्त्र जप।",
                isMajor = false,
                customGreeting = "प्रदोष व्रतको पावन शुभकामना!"
            )
        }

        if (cleanTithi.contains("पूर्णिमा")) {
            return FestivalInfo(
                id = "purnima_vrat_$bsMonth",
                name = "पूर्णिमा व्रत (सत्यनारायण पूजा)",
                englishName = "Purnima Vrat",
                category = FestivalCategory.VRATA_FAST,
                significance = "चन्द्रमा पूर्ण भएको पवित्र दिन। भगवान् सत्यनारायणको पूजा र व्रतबाट पारिवारिक सुख-शान्ति मिल्ने मान्यता।",
                ritualsAndRules = "सत्यनारायण कथा श्रवण, पञ्चामृत तथा पंजीरी प्रसाद वितरण, साँझ चन्द्रमालाई अर्घ्य।",
                isMajor = false,
                customGreeting = "पूर्णिमा व्रतको शुभकामना!"
            )
        }

        // 5. Fallback Check with HolidayData (जस्तै 2083 विशेष साइत वा पर्व)
        val holiday = HolidayData.getHoliday(bsYear, bsMonth, bsDay)
        if (holiday != null) {
            return FestivalInfo(
                id = "holiday_${bsMonth}_$bsDay",
                name = holiday.titleNepali,
                englishName = holiday.title,
                category = FestivalCategory.SOLAR_PARVA,
                significance = "नेपाली पात्रो अनुसार आजको विशेष अवसर: ${holiday.titleNepali}।",
                ritualsAndRules = "यस दिनको विशेष परम्परा अनुसार पूजा, उत्सव वा शुभकामना आदानप्रदान गर्नुहोस्।",
                isMajor = false,
                customGreeting = "${holiday.titleNepali}को शुभकामना!"
            )
        }

        return null
    }
}
