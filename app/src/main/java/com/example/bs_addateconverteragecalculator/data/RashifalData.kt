@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

data class ZodiacSign(
    val id: String,
    val nameNepali: String,
    val nameEnglish: String,
    val symbol: String,
    val dates: String,
    val lord: String,
    val element: String,
    val traits: String,
    val traitsEnglish: String,
    val descriptionNepali: String,
    val descriptionEnglish: String
) {
    fun getName(lang: String = "ne"): String = if (lang == "en") nameEnglish else nameNepali
    fun getTraits(lang: String = "ne"): String = if (lang == "en") traitsEnglish else traits
    fun getDescription(lang: String = "ne"): String = if (lang == "en") descriptionEnglish else descriptionNepali
}

data class RashifalPrediction(
    val daily: String,
    val weekly: String,
    val monthly: String,
    val yearly: String,
    val luckyColor: String,
    val luckyNumber: String,
    val gemstone: String,
    val direction: String,
    val profession: String,
    val luckyDays: String,
    val luckyMonths: String,
    val luckyAges: String
)

object RashifalData {
    val signs = listOf(
        ZodiacSign(
            id = "aries",
            nameNepali = "मेष",
            nameEnglish = "Aries",
            symbol = "♈",
            dates = "चु, चे, चो, ला, लि, लु, ले, लो, अ",
            lord = "मङ्गल (Mars)",
            element = "अग्नि (Fire)",
            traits = "अत्यधिक ऊर्जावान्, निडर, साहसी र नेतृत्वदायी",
            traitsEnglish = "Bold, energetic, pioneering, and impulsive",
            descriptionNepali = "यो राशि चक्रको पहिलो राशि हो। यस राशि भएका व्यक्तिहरू अत्यधिक ऊर्जावान्, निडर, साहसी र नेतृत्व गर्न रुचाउने हुन्छन्। उनीहरूमा निर्णय छिटो लिने क्षमता हुन्छ तर धैर्यताको कहिलेकाहीं कमी हुन सक्छ।",
            descriptionEnglish = "Aries natives are bold, energetic, pioneering, and impulsive. Their energy is dynamic, naturally stepping into leadership roles with courage and vitality."
        ),
        ZodiacSign(
            id = "taurus",
            nameNepali = "वृष",
            nameEnglish = "Taurus",
            symbol = "♉",
            dates = "इ, उ, ए, ओ, बा, बि, बु, बे, बो",
            lord = "शुक्र (Venus)",
            element = "पृथ्वी (Earth)",
            traits = "स्थिरता, सहनशीलता, दृढ संकल्प र मेहनती",
            traitsEnglish = "Stable, patient, practical, and stubborn",
            descriptionNepali = "स्थिरता, सहनशीलता र दृढ संकल्प यस राशिका मुख्य गुण हुन्। उनीहरू कला, संगीत, सौन्दर्य र भौतिक सुख-सुविधाप्रति बढी आकर्षित हुन्छन्। आफ्नो कुरामा अडिग रहने र मेहनती स्वभावका हुन्छन्।",
            descriptionEnglish = "Taurus natives are stable, patient, and practical, with a strong focus on material security, asset management, and sensory comfort."
        ),
        ZodiacSign(
            id = "gemini",
            nameNepali = "मिथुन",
            nameEnglish = "Gemini",
            symbol = "♊",
            dates = "का, कि, कु, घ, ङ, छ, के, को, ह",
            lord = "बुध (Mercury)",
            element = "वायु (Air)",
            traits = "बहुमुखी प्रतिभा, चञ्चल र उत्कृष्ट सञ्चार कला",
            traitsEnglish = "Curious, communicative, witty, and adaptable",
            descriptionNepali = "दुई व्यक्तिको प्रतीक भएको यो राशि भएकाहरू बहुमुखी प्रतिभाका, छिटो कुरा बुझ्ने, चञ्चल र उत्कृष्ट सञ्चार कला भएका हुन्छन्। उनीहरूलाई नयाँ कुरा सिक्न र मानिसहरूसँग घुलमिल हुन धेरै मन पर्छ।",
            descriptionEnglish = "Gemini natives are curious, communicative, witty, and adaptable, excelling in social and intellectual networking with great versatility."
        ),
        ZodiacSign(
            id = "cancer",
            nameNepali = "कर्कट",
            nameEnglish = "Cancer",
            symbol = "♋",
            dates = "हि, हु, हे, हो, डा, डि, डु, डे, डो",
            lord = "चन्द्रमा (Moon)",
            element = "जल (Water)",
            traits = "भावनात्मक संवेदनशीलता, परिवारप्रेमी र परोपकारी",
            traitsEnglish = "Emotional, nurturing, intuitive, and protective",
            descriptionNepali = "यो राशि भावनात्मक संवेदनशीलता, परिवारप्रतिको गहिरो प्रेम र परोपकारी भावनाका लागि चिनिन्छ। उनीहरूको स्मरण शक्ति तीव्र हुन्छ र अरूको भावनालाई छिटो महसुस गर्न सक्छन्।",
            descriptionEnglish = "Cancer natives are emotional, nurturing, intuitive, and protective, deeply attached to family and emotional security."
        ),
        ZodiacSign(
            id = "leo",
            nameNepali = "सिंह",
            nameEnglish = "Leo",
            symbol = "♌",
            dates = "मा, मि, मु, मे, मो, टा, टि, टु, टे",
            lord = "सूर्य (Sun)",
            element = "अग्नि (Fire)",
            traits = "तेज, आत्मविश्वास, उदारता र प्रभावकारी नेतृत्व",
            traitsEnglish = "Confident, ambitious, generous, and charismatic",
            descriptionNepali = "राजाको जस्तो स्वभाव भएको यो राशि तेज, आत्मविश्वास, उदारता र प्रभावकारी नेतृत्वका लागि प्रसिद्ध छ। उनीहरू जीवनलाई ठूलो र खुला रूपमा जिउन रुचाउँछन् र प्रशंसा पाउन मन पराउँछन्।",
            descriptionEnglish = "Leo natives are confident, ambitious, generous, and charismatic, possessing natural leadership skills and creative brilliance."
        ),
        ZodiacSign(
            id = "virgo",
            nameNepali = "कन्या",
            nameEnglish = "Virgo",
            symbol = "♍",
            dates = "टो, पा, पि, पु, ष, ण, ठ, पे, पो",
            lord = "बुध (Mercury)",
            element = "पृथ्वी (Earth)",
            traits = "विश्लेषणात्मक क्षमता, पूर्णतावादी र व्यावहारिक",
            traitsEnglish = "Analytical, meticulous, hardworking, and organized",
            descriptionNepali = "विश्लेषणात्मक क्षमता, पूर्णतावादी (Perfectionist) स्वभाव र सफा-सुग्घर रहने बानी यस राशिका मानिसहरूमा हुन्छ। उनीहरू कुनै पनि काम गर्दा त्यसको सूक्ष्म अध्ययन गर्छन् र व्यावहारिक हुन्छन्।",
            descriptionEnglish = "Virgo natives are analytical, meticulous, hardworking, and organized, excelling in administration, research, and process optimization."
        ),
        ZodiacSign(
            id = "libra",
            nameNepali = "तुला",
            nameEnglish = "Libra",
            symbol = "♎",
            dates = "रा, रि, रु, रे, रो, ता, ति, तु, ते",
            lord = "शुक्र (Venus)",
            element = "वायु (Air)",
            traits = "न्यायप्रेमी, सन्तुलनकारी र सौन्दर्यका पारखी",
            traitsEnglish = "Diplomatic, fair-minded, charming, and social",
            descriptionNepali = "न्यायप्रेमी, सन्तुलनकारी र सौन्दर्यका पारखी हुन्छन्। उनीहरू जीवनमा शान्ति, सद्भाव र निष्पक्षता कायम राख्न सधैँ प्रयत्नशील रहन्छन्। उनीहरूलाई एक्लै बस्नभन्दा सहकार्य गर्न मन पर्छ।",
            descriptionEnglish = "Libra natives are diplomatic, fair-minded, charming, and social, prioritizing balance, partnership, and aesthetic harmony."
        ),
        ZodiacSign(
            id = "scorpio",
            nameNepali = "वृश्चिक",
            nameEnglish = "Scorpio",
            symbol = "♏",
            dates = "तो, ना, नि, नु, ने, नो, या, यि, यु",
            lord = "मङ्गल (Mars)",
            element = "जल (Water)",
            traits = "रहस्यमय स्वभाव, दृढ इच्छाशक्ति र तीक्ष्ण बुद्धि",
            traitsEnglish = "Intense, passionate, private, and transformative",
            descriptionNepali = "गहिरो रहस्यमय स्वभाव, दृढ इच्छाशक्ति र तीक्ष्ण बुद्धिका धनी हुन्छन्। उनीहरूले जुन काममा हात हाल्छन्, त्यसलाई अन्त्यसम्म पुर्‍याएरै छाड्छन्। उनीहरूको भावनात्मक गहिराइ धेरै हुन्छ।",
            descriptionEnglish = "Scorpio natives are intense, passionate, private, and transformative, excelling in research, psychology, and deep strategic focus."
        ),
        ZodiacSign(
            id = "sagittarius",
            nameNepali = "धनु",
            nameEnglish = "Sagittarius",
            symbol = "♐",
            dates = "ये, यो, भा, भि, भू, ध, फा, ढ, भे",
            lord = "बृहस्पति (Jupiter)",
            element = "अग्नि (Fire)",
            traits = "आशावादी, स्वतन्त्रताप्रेमी र दार्शनिक",
            traitsEnglish = "Optimistic, philosophical, adventurous, and blunt",
            descriptionNepali = "आशावादी, स्वतन्त्रताप्रेमी, दार्शनिक र घुम्न रुचाउने स्वभावका हुन्छन्। उनीहरू सत्य बोल्न र जीवनका उच्च ज्ञान वा अध्यात्मतर्फ खोज गर्न बढी रुचि राख्छन्।",
            descriptionEnglish = "Sagittarius natives are optimistic, philosophical, adventurous, and broad-minded, thriving in higher education, travel, and publishing."
        ),
        ZodiacSign(
            id = "capricorn",
            nameNepali = "मकर",
            nameEnglish = "Capricorn",
            symbol = "♑",
            dates = "भो, जा, जि, जु, जे, जो, ख, खि, खु, खे, खो, गा, गि",
            lord = "शनि (Saturn)",
            element = "पृथ्वी (Earth)",
            traits = "परिश्रमी, अनुशासित, व्यावहारिक र लक्ष्यप्रति समर्पित",
            traitsEnglish = "Disciplined, ambitious, practical, and resilient",
            descriptionNepali = "परिश्रमी, अनुशासित, व्यावहारिक र लक्ष्यप्रति समर्पित हुन्छन्। उनीहरू छोटो बाटो भन्दा पनि निरन्तरको मिहिनेतबाट दीर्घकालीन सफलता हासिल गर्नमा विश्वास राख्छन्।",
            descriptionEnglish = "Capricorn natives are disciplined, ambitious, practical, and resilient, excelling in administration, governance, and long-term planning."
        ),
        ZodiacSign(
            id = "aquarius",
            nameNepali = "कुम्भ",
            nameEnglish = "Aquarius",
            symbol = "♒",
            dates = "गु, गे, गो, सा, सि, सु, से, सो, द",
            lord = "शनि (Saturn)",
            element = "वायु (Air)",
            traits = "क्रान्तिकारी सोच, परोपकारी, दूरदर्शी र स्वाभिमानी",
            traitsEnglish = "Independent, innovative, humanitarian, and original",
            descriptionNepali = "क्रान्तिकारी सोच, परोपकारी, दूरदर्शी र स्वाभिमानी हुन्छन्। उनीहरू नयाँ प्रविधि, समाज सुधार र मानिसहरूको हितमा काम गर्न बढी प्रेरित हुन्छन्।",
            descriptionEnglish = "Aquarius natives are independent, innovative, humanitarian, and original, driving technological and social reform."
        ),
        ZodiacSign(
            id = "pisces",
            nameNepali = "मीन",
            nameEnglish = "Pisces",
            symbol = "♆",
            dates = "दी, दू, थ, झ, ञ, दे, दो, चा, चि",
            lord = "बृहस्पति (Jupiter)",
            element = "जल (Water)",
            traits = "कल्पनाशील, कोमल हृदय भएका र आध्यात्मिक",
            traitsEnglish = "Compassionate, artistic, spiritual, and empathetic",
            descriptionNepali = "कल्पनाशील, कोमल हृदय भएका, आध्यात्मिक र निकै दयालु हुन्छन्। उनीहरू आफ्नो कल्पनाको संसारमा रमाउन रुचाउँछन् र कलात्मक क्षेत्रमा विशेष ख्याति कमाउँछन्।",
            descriptionEnglish = "Pisces natives are compassionate, imaginative, spiritual, and artistic."
        )
    )

    fun getPrediction(signId: String, lang: String = "ne"): RashifalPrediction {
        if (lang == "en") {
            return when (signId) {
                "aries" -> RashifalPrediction(
                    daily = "Today will be profitable. You will achieve success in business and get new opportunities. Health will remain excellent.",
                    weekly = "Your influence at work will increase this week. Favorable time for starting new ventures.",
                    monthly = "Financial gain at the beginning of the month. Happiness in the family. Colleagues will support your career.",
                    yearly = "Aries natives will feel great courage and energy this year. Your hard work will take your business to new heights with profit in real estate.",
                    luckyColor = "Red, Pink",
                    luckyNumber = "1, 8",
                    gemstone = "Red Coral",
                    direction = "East",
                    profession = "Military, Police, Medical, Engineering, Administration",
                    luckyDays = "Tuesday, Sunday",
                    luckyMonths = "Kartik, Falgun",
                    luckyAges = "16, 22, 28, 34"
                )
                "taurus" -> RashifalPrediction(
                    daily = "Exercise caution in financial matters today. Unnecessary expenses may rise. Enjoy family time.",
                    weekly = "Excellent week from a financial standpoint. Family bonds will deepen.",
                    monthly = "Despite minor fluctuations in business, ultimate success is assured. Take care of your diet.",
                    yearly = "Taurus natives will enjoy financial prosperity and material comfort this year. Great recognition in art, music, and banking.",
                    luckyColor = "White, Silver",
                    luckyNumber = "2, 7",
                    gemstone = "Diamond",
                    direction = "South",
                    profession = "Art, Music, Fashion, Banking, Agriculture, Business",
                    luckyDays = "Friday, Wednesday",
                    luckyMonths = "Margsir, Chaitra",
                    luckyAges = "25, 33, 41, 49"
                )
                "gemini" -> RashifalPrediction(
                    daily = "Courage and self-confidence will increase. Pending tasks will be completed with the help of friends.",
                    weekly = "Success in intellectual endeavors. Travel opportunities will open up.",
                    monthly = "Great time for students. Opportunities to earn fame in intellectual fields.",
                    yearly = "Gemini natives will achieve landmark success in intellectual development, higher education, and communication. Foreign travel prospects will blossom.",
                    luckyColor = "Green",
                    luckyNumber = "3, 6",
                    gemstone = "Emerald",
                    direction = "West",
                    profession = "Teaching, Journalism, Writing, Law, Stock Market",
                    luckyDays = "Wednesday, Friday",
                    luckyMonths = "Ashwin, Magh",
                    luckyAges = "22, 32, 42, 52"
                )
                "cancer" -> RashifalPrediction(
                    daily = "Despite domestic busy schedule, your heart will remain cheerful. Interest in art and music will grow.",
                    weekly = "Mental peace will be attained. Domestic comforts will increase.",
                    monthly = "Concern regarding mother's health may arise. New business strategies will take shape.",
                    yearly = "A year of mental peace, family prosperity, and acquiring new property for Cancer natives. Social prestige will flourish.",
                    luckyColor = "White, Cream",
                    luckyNumber = "4, 9",
                    gemstone = "Pearl",
                    direction = "North",
                    profession = "Navy, Dairy, Hotel, Social Work, Nursing",
                    luckyDays = "Monday, Thursday",
                    luckyMonths = "Shrawan, Mangsir",
                    luckyAges = "24, 29, 38, 47"
                )
                "leo" -> RashifalPrediction(
                    daily = "Prestige and reputation will rise. Leadership capabilities will shine.",
                    weekly = "Opportunities to lead at work will arise. Respect will grow.",
                    monthly = "Success in political and social spheres. Income sources will expand.",
                    yearly = "Leo natives will assume commanding leadership roles in society and career this year. Major elevation in status and responsibilities.",
                    luckyColor = "Gold, Yellow",
                    luckyNumber = "1, 5",
                    gemstone = "Ruby",
                    direction = "East",
                    profession = "Government Service, Politics, Management, Leadership",
                    luckyDays = "Sunday, Tuesday",
                    luckyMonths = "Bhadra, Poush",
                    luckyAges = "22, 28, 37, 46"
                )
                "virgo" -> RashifalPrediction(
                    daily = "Focus on expense control today. Avoid long-distance travel.",
                    weekly = "New business contracts will be signed. Health will improve.",
                    monthly = "Success in legal affairs. Stay guarded against covert rivals.",
                    yearly = "A year of freedom from debt, victory in legal matters and competitive exams for Virgo natives. Unexpected gains from long travels.",
                    luckyColor = "Green, Dark Green",
                    luckyNumber = "3, 8",
                    gemstone = "Emerald",
                    direction = "South",
                    profession = "Accounting, Software, Diplomacy, Trade, Research",
                    luckyDays = "Wednesday, Friday",
                    luckyMonths = "Ashwin, Magh",
                    luckyAges = "25, 32, 40, 48"
                )
                "libra" -> RashifalPrediction(
                    daily = "New agreements in business. Marital life will be blissful.",
                    weekly = "Gains in partnership work. Romantic relationships will deepen.",
                    monthly = "Love relationships will flourish. Interest in art and music will rise.",
                    yearly = "An exceptionally favorable year for partnerships, marriage, and romance for Libra natives. Great fame in creative fields.",
                    luckyColor = "White, Light Blue",
                    luckyNumber = "2, 7",
                    gemstone = "Diamond",
                    direction = "West",
                    profession = "Judiciary, Art, Fashion, Partnership Trade, Entertainment",
                    luckyDays = "Friday, Saturday",
                    luckyMonths = "Kartik, Falgun",
                    luckyAges = "21, 25, 36, 45"
                )
                "scorpio" -> RashifalPrediction(
                    daily = "Sudden financial gains possible. Pay special attention to diet and health.",
                    weekly = "Interest in spirituality and research will grow. Old problems will resolve.",
                    monthly = "Inclination towards spirituality. Pending tasks will resume.",
                    yearly = "A transformative year of research, hidden wealth gains, and spiritual progress for Scorpio natives, bringing long-term rewards.",
                    luckyColor = "Red, Maroon",
                    luckyNumber = "9, 8",
                    gemstone = "Red Coral",
                    direction = "North",
                    profession = "Research, Medicine, Intelligence, Insurance, Chemistry",
                    luckyDays = "Tuesday, Sunday",
                    luckyMonths = "Margsir, Chaitra",
                    luckyAges = "24, 30, 42, 50"
                )
                "sagittarius" -> RashifalPrediction(
                    daily = "Fortune will favor you. Mind will incline towards righteous deeds.",
                    weekly = "Success in religious journeys and higher education.",
                    monthly = "Prospects of higher studies and overseas travel.",
                    yearly = "A successful year for higher education, long journeys, and spiritual ventures for Sagittarius natives. Intellectual triumphs ahead.",
                    luckyColor = "Yellow, Orange",
                    luckyNumber = "3, 9",
                    gemstone = "Yellow Sapphire",
                    direction = "East",
                    profession = "Teaching, Higher Education, Religious Leadership, Publishing, Tourism",
                    luckyDays = "Thursday, Sunday",
                    luckyMonths = "Baishakh, Shrawan",
                    luckyAges = "18, 27, 35, 44"
                )
                "capricorn" -> RashifalPrediction(
                    daily = "Fruits of hard work will be realized. New career opportunities will arise.",
                    weekly = "Progress in professional sphere. Financial standing will strengthen.",
                    monthly = "Favorable prospects for acquiring land or property.",
                    yearly = "Hard work will yield outstanding returns for Capricorn natives. A stellar year for building permanent assets and major projects.",
                    luckyColor = "Blue, Grey",
                    luckyNumber = "4, 8",
                    gemstone = "Blue Sapphire",
                    direction = "South",
                    profession = "Engineering, Contracting, Administration, Mining, Construction",
                    luckyDays = "Saturday, Tuesday",
                    luckyMonths = "Jestha, Ashwin",
                    luckyAges = "19, 25, 37, 46"
                )
                "aquarius" -> RashifalPrediction(
                    daily = "Enthusiasm will rise with friends' support. Opportunities to learn new tech.",
                    weekly = "Gains from modern technology and social initiatives.",
                    monthly = "Active involvement in social work. New income channels will open.",
                    yearly = "A groundbreaking year in technology, innovation, and social reform for Aquarius natives. Team endeavors yield immense rewards.",
                    luckyColor = "Bright Blue, Violet",
                    luckyNumber = "4, 7",
                    gemstone = "Hessonite",
                    direction = "West",
                    profession = "Technology, IT, Science, Social Service, Aviation",
                    luckyDays = "Saturday, Wednesday",
                    luckyMonths = "Ashadh, Kartik",
                    luckyAges = "21, 31, 40, 50"
                )
                "pisces" -> RashifalPrediction(
                    daily = "Mind will be joyful. Success in art and literature.",
                    weekly = "Success in spiritual and artistic pursuits.",
                    monthly = "Inclination towards charity and spiritual deeds. Business will flourish.",
                    yearly = "A year of spiritual elevation, artistic creation, and philanthropic triumph for Pisces natives, blessed with peace and good fortune.",
                    luckyColor = "Yellow, Sea Green",
                    luckyNumber = "3, 7",
                    gemstone = "Yellow Sapphire",
                    direction = "North",
                    profession = "Art, Music, Spirituality, Medicine, Philanthropy",
                    luckyDays = "Thursday, Monday",
                    luckyMonths = "Baishakh, Magh",
                    luckyAges = "16, 25, 34, 48"
                )
                else -> RashifalPrediction(
                    daily = "Day will yield moderate results. Take care of health.",
                    weekly = "Week will be steady.",
                    monthly = "Steady progress in business.",
                    yearly = "This year will be progressive for you.",
                    luckyColor = "Red",
                    luckyNumber = "1",
                    gemstone = "Red Coral",
                    direction = "East",
                    profession = "Business",
                    luckyDays = "Sunday",
                    luckyMonths = "Baishakh",
                    luckyAges = "25"
                )
            }
        }

        return when (signId) {
            "aries" -> RashifalPrediction(
                daily = "आज दिन लाभदायक रहनेछ। व्यापारमा सफलता मिल्नेछ र नयाँ अवसर प्राप्त हुनेछन्। स्वास्थ्य उत्तम रहनेछ।",
                weekly = "यस साता कार्यक्षेत्रमा तपाईंको प्रभाव बढ्नेछ। नयाँ कार्यको थालनीका लागि समय अनुकूल छ।",
                monthly = "महिनाको सुरुवातमा आर्थिक लाभ हुनेछ। परिवारमा खुसीयाली छाउनेछ। कार्यक्षेत्रमा सहकर्मीको सहयोग मिल्नेछ।",
                yearly = "यस वर्ष मेष राशिका जातकहरूमा अदम्य साहस र उर्जाको संचार हुनेछ। तपाईंको पौरख र मेहनतले नयाँ व्यावसायिक उचाइ लिनेछ। रोकिएका प्रशासनिक कामहरू सुल्झिनुका साथै जग्गा-जमिन खरिद-बिक्रीबाट मनग्य नाफा हुने देखिन्छ।",
                luckyColor = "रातो, गुलाबी",
                luckyNumber = "१, ८",
                gemstone = "मुगा",
                direction = "पूर्व",
                profession = "सेना, प्रहरी, मेडिकल, इन्जिनियरिङ, प्रशासन",
                luckyDays = "मंगलबार, आइतबार",
                luckyMonths = "कार्तिक, फागुन",
                luckyAges = "१६, २२, २८, ३४"
            )
            "taurus" -> RashifalPrediction(
                daily = "आर्थिक मामिलामा सावधानी अपनाउनुहोला। अनावश्यक खर्च बढ्न सक्छ। परिवारसँग समय बिताउने अवसर मिल्नेछ।",
                weekly = "आर्थिक दृष्टिले यो साता उत्तम रहनेछ। पारिवारिक सम्बन्धमा प्रगाढता आउनेछ।",
                monthly = "व्यापारमा सामान्य उतारचढाव आएपनि अन्ततः सफलता मिल्नेछ। खानपानमा ध्यान दिनुहोला।",
                yearly = "वृष राशि भएका जातकका लागि आर्थिक समृद्धि र भौतिक सुख-सुविधाले भरिपूर्ण रहनेछ। कला, संगीत तथा बैंकिङ क्षेत्रमा विशेष ख्याति मिल्नेछ। परिवारमा मांगलिक कार्य सम्पन्न हुने तथा दाम्पत्य जीवन अत्यन्त सुखमय बन्नेछ।",
                luckyColor = "सेतो, चम्किलो",
                luckyNumber = "२, ७",
                gemstone = "हीरा",
                direction = "दक्षिण",
                profession = "कला, संगीत, फेसन, बैंकिङ, कृषि, व्यापार",
                luckyDays = "शुक्रबार, बुधबार",
                luckyMonths = "मार्गशीर्ष, चैत",
                luckyAges = "२५, ३३, ४१, ४९"
            )
            "gemini" -> RashifalPrediction(
                daily = "पराक्रम र आत्मविश्वास बढ्नेछ। मित्रहरूको सहयोगले अधुरा कामहरू सम्पन्न हुनेछन्।",
                weekly = "बौद्धिक क्षेत्रमा सफलता मिल्नेछ। यात्राका योगहरू खुल्नेछन्।",
                monthly = "विद्यार्थीहरूका लागि समय उत्तम छ। बौद्धिक क्षेत्रमा ख्याति कमाउने अवसर मिल्नेछ।",
                yearly = "मिथुन राशि हुनेहरूका लागि बौद्धिक विकास, उच्च शिक्षा र सञ्चार क्षेत्रमा ऐतिहासिक सफलता प्राप्त हुनेछ। विदेश यात्राका योगहरू खुल्नेछन् र रोकिएका पुराना योजनाहरूले गति लिनेछन्। शेयर बजार तथा व्यापारबाट विशेष धनलाभ हुनेछ।",
                luckyColor = "हरियो",
                luckyNumber = "३, ६",
                gemstone = "पन्ना",
                direction = "पश्चिम",
                profession = "शिक्षण, पत्रकारिता, लेखन, वकालत, सेयर बजार",
                luckyDays = "बुधबार, शुक्रबार",
                luckyMonths = "असोज, माघ",
                luckyAges = "२२, ३२, ४२, ५२"
            )
            "cancer" -> RashifalPrediction(
                daily = "घरायसी काममा व्यस्तता बढेपनि मन प्रसन्न रहनेछ। कला तथा संगीतमा रुचि बढ्नेछ।",
                weekly = "मानसिक शान्ति मिल्नेछ। घरायसी सुख-सुविधामा वृद्धि हुनेछ।",
                monthly = "आमाको स्वास्थ्यलाई लिएर चिन्ता रहन सक्छ। व्यापारमा नयाँ योजना बन्नेछन्।",
                yearly = "कर्कट राशि भएका व्यक्तिहरूका लागि मानसिक शान्ति, घरपरिवारमा सुख समृद्धि र नयाँ स्थायी सम्पत्ति आर्जन गर्ने वर्ष रहनेछ। समाजमा मान-सम्मान बढ्नेछ भने साझेदारी व्यवसायबाट ठूलो मुनाफा हात पार्न सकिनेछ।",
                luckyColor = "सेतो, क्रिम",
                luckyNumber = "४, ९",
                gemstone = "मोती",
                direction = "उत्तर",
                profession = "जलसेना, दुग्ध व्यवसाय, होटल, समाजसेवा, नर्सिङ",
                luckyDays = "सोमबार, बिहीबार",
                luckyMonths = "साउन, मंसिर",
                luckyAges = "२४, २९, ३८, ४७"
            )
            "leo" -> RashifalPrediction(
                daily = "मानसम्मान र प्रतिष्ठामा वृद्धि हुनेछ। नेतृत्वदायी क्षमता देखिनेछ।",
                weekly = "कार्यक्षेत्रमा नेतृत्व गर्ने अवसर मिल्नेछ। मान-सम्मान बढ्नेछ।",
                monthly = "राजनीतिक तथा सामाजिक क्षेत्रमा सफलता मिल्नेछ। आयस्रोत बढ्नेछ।",
                yearly = "सिंह राशिका व्यक्तिहरूले यस वर्ष समाज र कार्यक्षेत्रमा नेतृत्वदायी भूमिका निर्वाह गर्नेछन्। तपाईंको मान-सम्मान, पद र प्रतिष्ठामा उच्च वृद्धि हुनेछ। सरकारी निकाय वा ठूला संघ-संस्थाबाट महत्वपूर्ण जिम्मेवारी हात लाग्नेछ।",
                luckyColor = "सुनौलो, पहेंलो",
                luckyNumber = "१, ५",
                gemstone = "माणिक्य",
                direction = "पूर्व",
                profession = "सरकारी जागिर, राजनीति, व्यवस्थापन, नेतृत्वदायी भूमिका",
                luckyDays = "आइतबार, मंगलबार",
                luckyMonths = "भदौ, पुस",
                luckyAges = "२२, २८, ३७, ४६"
            )
            "virgo" -> RashifalPrediction(
                daily = "खर्च नियन्त्रणमा ध्यान दिनुहोला। टाढाको यात्रा नगरेको बेस।",
                weekly = "व्यापारमा नयाँ सम्झौता हुनेछ। स्वास्थ्यमा सुधार आउनेछ।",
                monthly = "कानूनी वा अदालती मामलामा सफलता मिल्नेछ। गुप्त शत्रुबाट बच्नुहोला।",
                yearly = "कन्या राशि भएका जातकका लागि ऋणमुक्ति, अदालती तथा प्रतियोगी परीक्षामा विजय प्राप्त गर्ने वर्ष हो। व्यापार व्यवसायमा विश्वासीलो वातावरण बन्नेछ र विदेश वा टाढाको यात्राबाट अप्रत्याशित सफलता हात लाग्नेछ।",
                luckyColor = "हरियो, गाढा हरियो",
                luckyNumber = "३, ८",
                gemstone = "पन्ना",
                direction = "दक्षिण",
                profession = "एकाउन्ट, सफ्टवेयर, कूटनीति, व्यापार, अनुसन्धान",
                luckyDays = "बुधबार, शुक्रबार",
                luckyMonths = "असोज, माघ",
                luckyAges = "२५, ३२, ४०, ४८"
            )
            "libra" -> RashifalPrediction(
                daily = "व्यापारमा नयाँ सम्झौता हुनेछ। दाम्पत्य जीवन सुखमय रहनेछ।",
                weekly = "साझेदारी काममा लाभ मिल्नेछ। प्रेम सम्बन्ध प्रगाढ बन्नेछ।",
                monthly = "प्रेम सम्बन्ध प्रगाढ बन्नेछ। कला तथा संगीतमा रुचि बढ्नेछ।",
                yearly = "तुला राशि भएका जातकका लागि यो वर्ष साझेदारी व्यवसाय, विवाह तथा प्रेम सम्बन्धका लागि अत्यन्तै शुभ रहनेछ। कला, फेसन र मिडिया क्षेत्रमा ठूलो ख्याति कमाउने अवसर प्राप्त हुनेछ।",
                luckyColor = "सेतो, हल्का नीलो",
                luckyNumber = "२, ७",
                gemstone = "हीरा",
                direction = "पश्चिम",
                profession = "न्याय, कला, फेसन, साझेदारी व्यापार, मनोरञ्जन",
                luckyDays = "शुक्रबार, शनिबार",
                luckyMonths = "कार्तिक, फागुन",
                luckyAges = "२१, २५, ३६, ४५"
            )
            "scorpio" -> RashifalPrediction(
                daily = "अचानक धन लाभ हुन सक्छ। स्वास्थ्यमा खानपानको विशेष ध्यान दिनुहोला।",
                weekly = "अध्यात्म तथा अनुसन्धानमा रुचि बढ्नेछ। पुराना समस्या हल हुनेछन्।",
                monthly = "अध्यात्मतर्फ रुचि बढ्नेछ। रोकिएका कामहरू बन्नेछन्।",
                yearly = "वृश्चिक राशि भएका जातकहरूका लागि अनुसन्धान, गुप्त धन प्राप्ति र आध्यात्मिक उन्नति हुने वर्ष हो। जीवनमा ठूला रूपान्तरणहरू आउनेछन् जसले दीर्घकालीन फाइदा पुर्‍याउनेछ।",
                luckyColor = "रातो, मरुमहा",
                luckyNumber = "९, ८",
                gemstone = "मुगा",
                direction = "उत्तर",
                profession = "अनुसन्धान, चिकित्सा, गुप्तचर विभाग, बीमा, रसायन",
                luckyDays = "मंगलबार, आइतबार",
                luckyMonths = "मार्गशीर्ष, चैत",
                luckyAges = "२४, ३०, ४२, ५०"
            )
            "sagittarius" -> RashifalPrediction(
                daily = "भाग्यले साथ दिनेछ। धार्मिक कार्यमा मन जानेछ।",
                weekly = "धार्मिक यात्रा तथा उच्च शिक्षामा सफलता मिल्नेछ।",
                monthly = "उच्च शिक्षा तथा वैदेशिक यात्राको सम्भावना छ।",
                yearly = "धनु राशिका जातकहरूका लागि उच्च शिक्षा, लामो यात्रा र धार्मिक कार्यमा सफलता मिल्ने वर्ष रहनेछ। बौद्धिक तथा प्राज्ञिक क्षेत्रमा ठूलो सफलता हात लाग्नेछ।",
                luckyColor = "पहेलो, सुन्तला",
                luckyNumber = "३, ९",
                gemstone = "पुष्पराज",
                direction = "पूर्व",
                profession = "शिक्षण, उच्च शिक्षा, धर्मगुरु, प्रकाशन, पर्यटन",
                luckyDays = "बिहीबार, आइतबार",
                luckyMonths = "वैशाख, साउन",
                luckyAges = "१८, २७, ३५, ४४"
            )
            "capricorn" -> RashifalPrediction(
                daily = "मेहनतको फल राम्रोसँग प्राप्त हुनेछ। करियरमा नयाँ अवसर आउनेछन्।",
                weekly = "व्यावसायिक क्षेत्रमा प्रगति हुनेछ। आर्थिक स्थिति मजबूत बन्नेछ।",
                monthly = "जग्गा जमीन वा सम्पत्ति जोडिने योग छ।",
                yearly = "मकर राशि भएका जातकका लागि यस वर्ष कठिन परिश्रमको उच्च प्रतिफल मिल्नेछ। स्थायी सम्पत्ति जोड्ने र ठूला व्यावसायिक परियोजनाहरूमा सफलता हात पार्ने वर्ष हुनेछ।",
                luckyColor = "निलो, खैरो",
                luckyNumber = "४, ८",
                gemstone = "नीलम",
                direction = "दक्षिण",
                profession = "इन्जिनियरिङ, ठेकेदार, प्रशासन, खानी, निर्माण",
                luckyDays = "शनिबार, मंगलबार",
                luckyMonths = "जेठ, असोज",
                luckyAges = "१९, २५, ३७, ४६"
            )
            "aquarius" -> RashifalPrediction(
                daily = "मित्रहरूको सहयोगले उत्साह बढ्नेछ। नयाँ प्रविधि सिक्ने अवसर मिल्नेछ।",
                weekly = "नयाँ प्रविधि तथा सामाजिक कार्यबाट लाभ मिल्नेछ।",
                monthly = "सामाजिक कार्यमा सक्रियता बढ्नेछ। आयका नयाँ स्रोत खुल्नेछन्।",
                yearly = "कुम्भ राशि भएका जातकहरूका लागि विज्ञान, प्रविधि, सामाजिक कार्य र आविष्कारका क्षेत्रमा विशेष सफलता मिल्ने वर्ष रहनेछ। सामूहिक कार्यबाट ठूलो लाभ हुनेछ।",
                luckyColor = "उज्यालो नीलो, बैजनी",
                luckyNumber = "४, ७",
                gemstone = "गोमेद",
                direction = "पश्चिम",
                profession = "प्रविधि, आईटी, विज्ञान, समाजसेवा, उड्डयन",
                luckyDays = "शनिबार, बुधबार",
                luckyMonths = "असार, कार्तिक",
                luckyAges = "२१, ३१, ४०, ५०"
            )
            "pisces" -> RashifalPrediction(
                daily = "मन प्रसन्न रहनेछ। कला तथा साहित्यमा सफलता मिल्नेछ।",
                weekly = "आध्यात्मिक तथा कलात्मक कार्यमा सफलता मिल्नेछ।",
                monthly = "दानपुण्य तथा धार्मिक कार्यमा मन जानेछ। व्यापारमा फाइदा हुनेछ।",
                yearly = "मीन राशिका व्यक्तिहरूका लागि यो वर्ष आध्यात्मिक उन्नति, कलात्मक सिर्जना र परोपकारी कार्यमा विशेष सफलता मिल्ने वर्ष रहनेछ। मानसिक शान्ति र भाग्यको पूर्ण साथ रहनेछ।",
                luckyColor = "पहेलो, समुद्री हरियो",
                luckyNumber = "३, ७",
                gemstone = "पुष्पराज",
                direction = "उत्तर",
                profession = "कला, संगीत, अध्यात्म, चिकित्सा, परोपकार",
                luckyDays = "बिहीबार, सोमबार",
                luckyMonths = "वैशाख, माघ",
                luckyAges = "१६, २५, ३४, ४८"
            )
            else -> RashifalPrediction(
                daily = "दिन सामान्य रहनेछ। स्वास्थ्यमा ध्यान दिनुहोला।",
                weekly = "साता सामान्य रहनेछ।",
                monthly = "व्यापारमा सामान्य प्रगति हुनेछ।",
                yearly = "यो वर्ष तपाईंको लागि प्रगतिशील रहनेछ।",
                luckyColor = "रातो",
                luckyNumber = "१",
                gemstone = "मुगा",
                direction = "पूर्व",
                profession = "व्यापार",
                luckyDays = "आइतबार",
                luckyMonths = "वैशाख",
                luckyAges = "२५"
            )
        }
    }
}
