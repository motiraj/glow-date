@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

object PersonalizedRashifalGenerator {

    private val LUCKY_COLORS = arrayOf(
        "रातो र गुलाबी", // मेष
        "सेतो र चम्किलो", // वृष
        "हरियो र हल्का पहेँलो", // मिथुन
        "सेतो र क्रिम", // कर्कट
        "सुनौलो र गाढा पहेँलो", // सिंह
        "हरियो र गाढा हरियो", // कन्या
        "सेतो र हल्का नीलो", // तुला
        "रातो र मरुमहा", // वृश्चिक
        "पहेँलो र सुन्तला", // धनु
        "गाढा नीलो र खैरो", // मकर
        "उज्यालो नीलो र बैजनी", // कुम्भ
        "पहेँलो र समुद्री हरियो" // मीन
    )

    private val LUCKY_NUMBERS = arrayOf(
        "१, ९", // मेष
        "२, ६", // वृष
        "३, ५", // मिथुन
        "४, ९", // कर्कट
        "१, ५", // सिंह
        "३, ८", // कन्या
        "२, ७", // तुला
        "९, ८", // वृश्चिक
        "३, ९", // धनु
        "४, ८", // मकर
        "४, ७", // कुम्भ
        "३, ७"  // मीन
    )

    fun generatePersonalizedPrediction(
        natalProfile: NatalProfile,
        transitAnalysis: TransitAnalysis,
        lang: String = "ne"
    ): PersonalizedPrediction {
        val rashiIdx = natalProfile.janmaRashiIndex.coerceIn(0, 11)
        val rashiName = if (lang == "en") natalProfile.janmaRashiNameEnglish else natalProfile.janmaRashiNameNepali
        val nakshatraName = natalProfile.nakshatraNameNepali
        val house = transitAnalysis.houseFromNatal

        val gocharChandraDetail = if (lang == "en") {
            when (house) {
                1 -> "Today Moon transits your natal rashi ($rashiName). This enhances self-confidence, mental energy, and personal aura. Excellent time for new plans and recognition."
                2 -> "Moon transits 2nd house (wealth). Pleasant speech brings success in financial affairs. Avoid unnecessary expenditure and maintain dietary balance."
                3 -> "Moon transits 3rd house (courage). Auspicious transit boosting energy and teamwork. Short trips and sibling support yield progress."
                4 -> "Moon transits 4th house (comfort). Focus on domestic harmony and mother's health. Maintain patience in property or vehicle matters."
                5 -> "Moon transits 5th house (intellect). Creative thinking, studies, and joy from children shine. Exercise caution in speculative investments."
                6 -> "Moon transits 6th house (enemies/health). Highly favorable transit overcoming rivals, debt, and health concerns."
                7 -> "Moon transits 7th house (partnership). Fosters marital bliss, commercial partnerships, and social networking."
                8 -> "Moon in 8th house (Chandrashtama). Highly sensitive transit. Avoid major investments, new ventures, or hasty driving."
                9 -> "Moon transits 9th house (luck). Fortune favors righteous deeds, higher learning, and spiritual journeys."
                10 -> "Moon transits 10th house (career). Excellent transit bringing professional honors, promotions, and leadership growth."
                11 -> "Moon transits 11th house (gains). Highly auspicious for income growth, wish fulfillment, and support from friends."
                else -> "Moon transits 12th house (expenditure). Manage unexpected expenses and ensure proper rest."
            }
        } else {
            when (house) {
                1 -> "आज चन्द्रमा तपाईँको आफ्नै जन्म राशि ($rashiName) मा भ्रमण गर्दैछ। यसले आत्मबल, मानसिक ऊर्जा र व्यक्तित्व प्रभावमा वृद्धि ल्याउनेछ। नयाँ कार्ययोजना बनाउन र मान-सम्मान प्राप्त गर्न दिन उत्तम छ। तर चन्द्रमाको चञ्चल स्वभावका कारण हतारमा निर्णय नलिनुहोला। मिष्ठान्न भोजन र पारिवारिक रमाइलोको योग छ।"
                2 -> "चन्द्रमा द्वितीय (धन तथा कुटुम्ब) भावमा गोचर गर्दैछ। बोलीको प्रभावले रोकिएका काम बन्नेछन् र पारिवारिक व्यवसाय वा सञ्चित धनबाट लाभ मिल्नेछ। तर अनावश्यक खानपान र भ्रामक आर्थिक प्रलोभनमा पर्नबाट जोगिनुहोला। आँखा वा घाँटीको स्वास्थ्यमा सामान्य ध्यान दिनु उपयुक्त हुनेछ।"
                3 -> "चन्द्रमा तृतीय (पराक्रम) भावमा अति शुभ गोचर गर्दैछ। साहस, उर्जा र कार्यकुशलतामा ठूलो वृद्धि हुनेछ। छोटो दूरीका यात्रा फलदायी हुनेछन्। भाइ-बहिनी र मित्रजनको पूर्ण सहयोगले पुराना अड्किएका कामहरू द्रुत गतिमा अघि बढ्नेछन्।"
                4 -> "चन्द्रमा चतुर्थ (सुख तथा मातृ) भावमा गोचर गर्दैछ। घरायसी विषय, आमाको स्वास्थ्य वा सवारी साधन सम्बन्धी कुरामा मन चिन्तित हुन सक्छ। मानसिक अशान्ति वा छातीमा बेचैनी महसुस हुन सक्ने भएकाले पर्याप्त विश्राम गर्नुहोला। आर्थिक लेनदेनमा धैर्यता राख्नु बेस हुनेछ।"
                5 -> "चन्द्रमा पञ्चम (बौद्धिक तथा सन्तान) भावमा गोचर गर्दैछ। सिर्जनात्मक सोच, अध्ययन-अध्यापन र योजना निर्माणमा प्रखरता आउनेछ। सन्तान पक्षबाट खुसीको खबर प्राप्त हुनेछ। तर सेयर बजार वा जोखिमपूर्ण लगानी गर्दा सतर्कता अपनाउनुहोला।"
                6 -> "चन्द्रमा षष्ठ (रिपु तथा रोग) भावमा अति शुभ र शक्तिशाली गोचर गर्दैछ। गुप्त शत्रु र प्रतिस्पर्धीहरू परास्त हुनेछन्। पुराना रोग र स्वास्थ्य समस्याहरूबाट मुक्ति मिल्नेछ। अदालती वा कानूनी मामलामा विजय प्राप्त हुनेछ भने ऋण चुक्ता गर्ने प्रयास सफल हुनेछन्।"
                7 -> "चन्द्रमा सप्तम (दाम्पत्य तथा साझेदारी) भावमा अति शुभ गोचर गर्दैछ। जीवनसाथीसँगको सम्बन्ध प्रगाढ बन्नेछ र पारिवारिक सुख मिल्नेछ। व्यावसायिक साझेदारी र दैनिक व्यापारमा राम्रो मुनाफा हात लाग्नेछ। सामाजिक प्रतिष्ठा र नयाँ सम्पर्कहरू विस्तार हुनेछन्।"
                8 -> "आज चन्द्रमा अष्टम भावमा (चन्द्राष्टम) अति प्रतिकूल गोचर गर्दैछ। आजको दिन नयाँ काम थालनी नगर्नुहोला र ठूला आर्थिक लगानीका निर्णयहरू स्थगित गर्नुहोला। यात्रा गर्दा वा सवारी चलाउँदा विशेष सतर्कता अपनाउनुहोला। मानसिक तनाव र आकस्मिक अवरोधबाट बच्न शिवजीको आराधना वा सेतो वस्तु दान गर्नुहोला।"
                9 -> "चन्द्रमा नवम (भाग्य) भावमा शुभ गोचर गर्दैछ। रोकिराखेका कामहरूमा भाग्यको पूर्ण साथ मिल्नेछ। धार्मिक तथा सांस्कृतिक कार्यमा मन जानेछ। गुरुजन वा उच्च अधिकारीको मार्गदर्शनले नयाँ अवसरहरू सिर्जना हुनेछन्। वैदेशिक काममा लागेकाहरूका लागि फलदायी समय छ।"
                10 -> "चन्द्रमा दशम (कर्म) भावमा अति शुभ गोचर गर्दैछ। तपाईँको कर्मक्षेत्रमा उच्च सफलता, पदोन्नति वा नयाँ जिम्मेवारी मिल्ने सम्भावना छ। सरकारी वा प्रशासनिक कामहरू सहजै बन्नेछन्। तपाईँको निर्णय क्षमता र मेहनतको उच्च कदर हुनेछ।"
                11 -> "चन्द्रमा एकादश (आय तथा लाभ) भावमा अति शुभ र फलदायी गोचर गर्दैछ। विभिन्न स्रोतहरूबाट आकस्मिक धन लाभ हुनेछ। इच्छा र मनोकांक्षाहरू पूरा हुनेछन्। मित्रजन र दाजुभाइबाट ठूलो सहयोग र खुसीको समाचार मिल्नेछ।"
                else -> "चन्द्रमा द्वादश (व्यय) भावमा गोचर गर्दैछ। अनावश्यक दौडधुप र आकस्मिक खर्च बढ्न सक्छ। लेनदेन र वैदेशिक यात्रा बाहेक स्थानीय काममा सजग रहनुहोला। शारीरिक थकान र निद्राको कमी हुन सक्ने भएकाले समयमै विश्राम गर्नुहोला।"
            }
        }

        // 1. Detailed Health Description
        val healthText = if (lang == "en") {
            if (transitAnalysis.isChandrashtama) {
                "Chandrashtama effect advises extra care for physical fatigue, blood pressure, or stress. Drive carefully and maintain a balanced diet."
            } else if (transitAnalysis.taraBalaIndex == 3 || transitAnalysis.taraBalaIndex == 7) {
                "Influence of ${transitAnalysis.taraBalaName} indicates mild tiredness. Yoga, hydration, and adequate rest are recommended."
            } else {
                "With birth nakshatra ($nakshatraName) and ${transitAnalysis.taraBalaName} aligned favorably, your energy and health remain vibrant."
            }
        } else {
            if (transitAnalysis.isChandrashtama) {
                "अष्टम चन्द्रमा (चन्द्राष्टम) को प्रभावले गर्दा शारीरिक थकान, रक्तचाप वा मानसिक तनाव हुन सक्छ। हतारमा सवारी नचलाउनुहोस् र समयमै पौष्टिक आहार तथा जलपानमा ध्यान दिनुहोस्।"
            } else if (transitAnalysis.taraBalaIndex == 3 || transitAnalysis.taraBalaIndex == 7) {
                "आज ${transitAnalysis.taraBalaName} को प्रभाव रहेकाले स्वास्थ्यमा सामान्य सुस्ती वा मौसमी प्रतिकूलता देखिन सक्छ। योग, ध्यान र पर्याप्त विश्राम लिनु उपयुक्त हुनेछ।"
            } else {
                "तपाईंको जन्म नक्षत्र ($nakshatraName) र आजको ${transitAnalysis.taraBalaName} अनुकूल रहेकाले शारीरिक फुर्ती, मानसिक प्रसन्नता र उच्च उर्जा कायम रहनेछ।"
            }
        }

        // 2. Detailed Career Description
        val careerText = if (lang == "en") {
            when (transitAnalysis.taraBalaIndex) {
                2, 6, 8, 9 -> "With favorable ${transitAnalysis.taraBalaName} and house $house Moon transit, professional accomplishments and project launches gain strong momentum."
                4 -> "Stability in career. Excellent coordination with peers and superiors ensures smooth execution of responsibilities."
                3, 5, 7 -> "Some professional hurdles or competition may arise. Avoid debates and keep strategy confidential."
                else -> "Routine tasks progress steadily. Seek expert counsel for new initiatives."
            }
        } else {
            when (transitAnalysis.taraBalaIndex) {
                2, 6, 8, 9 -> "आज ${transitAnalysis.taraBalaName} र $house औँ भावको गोचर चन्द्रमा अनुकूल भएकाले कार्यक्षेत्रमा ठूलो सफलता, नयाँ परियोजनाको सुरुवात, पदोन्नति र अड्किएका कामहरू द्रुत गतिमा अघि बढ्नेछन्।"
                4 -> "कार्यक्षेत्रमा स्थायित्व र सुरक्षा मिल्नेछ। सहकर्मीहरू र उच्च अधिकारीहरूसँगको उत्कृष्ट समन्वयले जिम्मेवारीहरू सहजै सम्पन्न हुनेछन्।"
                3, 5, 7 -> "कार्यक्षेत्रमा केही चुनौती वा प्रतिस्पर्धीहरूको दबाब महसुस हुन सक्छ। बहस र बादविवादबाट जोगिनुहोला र आफ्ना योजनाहरू गोप्य राख्नुहोला।"
                else -> "दैनिक कामहरू नियमित रूपमा अघि बढ्नेछन्। नयाँ कार्य योजनामा अनुभवी व्यक्तिहरूको सल्लाह लिएर पाइला चाल्नुहोला।"
            }
        }

        // 3. Detailed Finance Description
        val financeText = if (lang == "en") {
            if (transitAnalysis.taraBalaIndex in listOf(2, 6, 8, 9) || house in listOf(2, 6, 11)) {
                "Highly favorable financial period. Promising growth in business contracts, investments, and revenue streams."
            } else if (transitAnalysis.isChandrashtama || house == 12) {
                "Avoid speculative investments, unnecessary purchases, or acting as guarantor today."
            } else {
                "Financial flow remains balanced and steady. Income matches budget estimates."
            }
        } else {
            if (transitAnalysis.isChandrashtama || house == 12) {
                "आकस्मिक खर्च बढाउने योग रहेकाले आज अनावश्यक खरिदबिक्री वा जमानत बस्ने कामबाट जोगिनुहोला। ठूला लगानीका निर्णयहरू स्थगित गर्नु बुद्धिमानी हुनेछ।"
            } else if (transitAnalysis.taraBalaIndex in listOf(2, 6, 8, 9) || house in listOf(2, 6, 11)) {
                "आर्थिक दृष्टिले आज अति उत्तम दिन छ। व्यापारमा नयाँ सम्झौता, सेयर बजार वा पुरानो लगानीबाट मनग्य धनलाभ हुने योग छ।"
            } else {
                "आर्थिक प्रवाह सामान्य र सन्तुलित रहनेछ। नियमित आम्दानी यथावत रहनेछ भने बजेट नियन्त्रणमा रहनेछ।"
            }
        }

        // 4. Lucky Color & Number
        val luckyColor = LUCKY_COLORS[rashiIdx]
        val luckyNumber = LUCKY_NUMBERS[rashiIdx]

        // 5. Deep Daily Special Advice
        val adviceText = if (lang == "en") {
            buildString {
                append("• Moon Transit: $gocharChandraDetail\n\n")
                append("• Natal Profile: Rashi: $rashiName | Nakshatra: $nakshatraName (Pada ${natalProfile.pada}, Akshar '${natalProfile.naamAkshar}')\n")
                append("• Transits: Tara Bala: ${transitAnalysis.taraBalaName} | Chandra Bala: ${transitAnalysis.chandraBalaStatus}\n")
                append("• Active Dashas: Mahadasha: ${transitAnalysis.activeMahadashaLord} | Yogini: ${transitAnalysis.activeYoginiDasha}\n")
                if (transitAnalysis.isChandrashtama) {
                    append("• Chandrashtama Remedy: Offer white flowers or milk to Lord Shiva and chant 'Om Namah Shivaya'.")
                } else {
                    append("• Daily Tip: Pray to your deity and offer water to the Sun before starting major tasks.")
                }
            }
        } else {
            buildString {
                append("• गोचर चन्द्र फल: $gocharChandraDetail\n\n")
                append("• जन्म विवरण: राशि: $rashiName | नक्षत्र: $nakshatraName (पाउ ${natalProfile.pada}, नामाक्षर '${natalProfile.naamAkshar}')\n")
                append("• गोचर स्थिति: ताराबल: ${transitAnalysis.taraBalaName} (${transitAnalysis.taraBalaStatus}) | चन्द्रबल: ${transitAnalysis.chandraBalaStatus}\n")
                append("• सक्रिय दशा: विंशोत्तरी महादशा: ${transitAnalysis.activeMahadashaLord} | योगिनी: ${transitAnalysis.activeYoginiDasha}\n")
                if (transitAnalysis.isChandrashtama) {
                    append("• चन्द्राष्टम निवारण उपाय: आज ओम नमः शिवाय जप गर्दै सेतो चन्दन वा दुध भगवान शिवलाई अर्पण गर्नु कल्याणकारी हुनेछ।")
                } else {
                    append("• दैनिक उपाय: आज कार्य थालनी गर्नुअघि कुलदेवताको स्मरण गरी सूर्यदेवलाई अर्घ्य चढाउनु उत्तम हुनेछ।")
                }
            }
        }

        return PersonalizedPrediction(
            health = healthText,
            career = careerText,
            finance = financeText,
            luckyColor = luckyColor,
            luckyNumber = luckyNumber,
            dailyAdvice = adviceText
        )
    }
}
