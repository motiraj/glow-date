package com.example.bs_addateconverteragecalculator.utils

import androidx.compose.runtime.compositionLocalOf

val LocalAppLanguage = compositionLocalOf { "ne" }
val LocalIsNepali = compositionLocalOf { true }

object AppStrings {
    fun get(key: String, lang: String): String {
        return when (key) {
            "app_name" -> when (lang) {
                "en" -> "GlowDate"
                "hi" -> "ग्लोडेट"
                else -> "ग्लोडेट"
            }
            "tab_panchanga" -> when (lang) {
                "en" -> "Panchanga"
                "hi" -> "पंचांग"
                else -> "पञ्चाङ्ग"
            }
            "tab_converter" -> when (lang) {
                "en" -> "Converter"
                "hi" -> "कनवर्टर"
                else -> "रूपान्तरण"
            }
            "tab_calendar" -> when (lang) {
                "en" -> "Calendar"
                "hi" -> "कैलेंडर"
                else -> "पात्रो"
            }
            "tab_rashifal" -> when (lang) {
                "en" -> "Rashifal"
                "hi" -> "राशिफल"
                else -> "राशिफल"
            }
            "tab_cheena" -> when (lang) {
                "en" -> "Cheena"
                "hi" -> "चीना पत्रिका"
                else -> "चिना पत्रिका"
            }

            "title_panchanga" -> when (lang) {
                "en" -> "Today's Panchanga"
                "hi" -> "आज का पंचांग"
                else -> "आजको पञ्चाङ्ग"
            }
            "title_converter" -> when (lang) {
                "en" -> "Converter & Age Calculator"
                "hi" -> "तिथि कनवर्टर और आयु कैलकुलेटर"
                else -> "मिति रूपान्तरण तथा उमेर क्याल्कुलेटर"
            }
            "title_calendar" -> when (lang) {
                "en" -> "Nepali Calendar"
                "hi" -> "नेपाली और विक्रमी कैलेंडर"
                else -> "नेपाली पात्रो र बिदाहरू"
            }
            "title_rashifal" -> when (lang) {
                "en" -> "Rashifal & Horoscope"
                "hi" -> "दैनिक, मासिक और वार्षिक राशिफल"
                else -> "दैनिक, मासिक तथा वार्षिक राशिफल"
            }
            "title_settings" -> when (lang) {
                "en" -> "Settings & About"
                "hi" -> "सेटिंग्स और बारे में"
                else -> "सेटिङ तथा बारेमा"
            }

            "btn_convert_ad" -> when (lang) {
                "en" -> "Convert to AD"
                "hi" -> "ईस्वी सन (AD) में बदलें"
                else -> "इ.सं. मा रूपान्तरण गर्नुहोस्"
            }
            "btn_convert_bs" -> when (lang) {
                "en" -> "Convert to BS"
                "hi" -> "विक्रमी संवत (BS) में बदलें"
                else -> "वि.सं. मा रूपान्तरण गर्नुहोस्"
            }
            "btn_calculate" -> when (lang) {
                "en" -> "Calculate Age"
                "hi" -> "आयु की गणना करें"
                else -> "उमेर गणना गर्नुहोस्"
            }
            "btn_copy" -> when (lang) {
                "en" -> "Copy"
                "hi" -> "कॉपी करें"
                else -> "कपी"
            }
            "btn_share" -> when (lang) {
                "en" -> "Share"
                "hi" -> "शेयर करें"
                else -> "सेयर"
            }

            "label_year" -> when (lang) {
                "en" -> "Year"
                "hi" -> "वर्ष"
                else -> "वर्ष"
            }
            "label_month" -> when (lang) {
                "en" -> "Month"
                "hi" -> "महीना"
                else -> "महिना"
            }
            "label_day" -> when (lang) {
                "en" -> "Day"
                "hi" -> "दिन"
                else -> "गते"
            }
            "label_birth_date" -> when (lang) {
                "en" -> "Birth Date"
                "hi" -> "जन्म तिथि"
                else -> "जन्म मिति"
            }
            "label_calc_as_of" -> when (lang) {
                "en" -> "Calculate Age As Of"
                "hi" -> "इस तिथि तक आयु"
                else -> "उमेर गणना गर्ने मिति"
            }

            "result_age" -> when (lang) {
                "en" -> "Your Exact Age"
                "hi" -> "आपकी सटीक आयु"
                else -> "तपाईंको उमेर"
            }
            "total_days" -> when (lang) {
                "en" -> "Total Days"
                "hi" -> "कुल दिन"
                else -> "जम्मा दिनहरू"
            }
            "total_weeks" -> when (lang) {
                "en" -> "Total Weeks"
                "hi" -> "कुल सप्ताह"
                else -> "जम्मा हप्ताहरू"
            }
            "total_months" -> when (lang) {
                "en" -> "Total Months"
                "hi" -> "कुल महीने"
                else -> "जम्मा महिनाहरू"
            }
            "born_on" -> when (lang) {
                "en" -> "Born on"
                "hi" -> "जन्मवार"
                else -> "जन्मिएको बार"
            }
            "zodiac" -> when (lang) {
                "en" -> "Zodiac Sign"
                "hi" -> "राशि"
                else -> "राशि"
            }

            "transit_results" -> when (lang) {
                "en" -> "Transit Results"
                "hi" -> "गोचर फल"
                else -> "गोचर फल"
            }
            "close" -> when (lang) {
                "en" -> "Close"
                "hi" -> "बंद करें"
                else -> "बन्द गर्नुहोस्"
            }
            "karakatva" -> when (lang) {
                "en" -> "Significations:"
                "hi" -> "कारकत्व:"
                else -> "कारकत्व:"
            }
            "auspicious_houses" -> when (lang) {
                "en" -> "Auspicious Houses (from Birth Rashi):"
                "hi" -> "शुभ भाव (जन्म राशि से):"
                else -> "शुभ भाव (जन्म राशिबाट):"
            }
            "inauspicious_houses" -> when (lang) {
                "en" -> "Inauspicious Houses:"
                "hi" -> "अशुभ भाव:"
                else -> "अशुभ भाव:"
            }
            "nakshatra_pada" -> when (lang) {
                "en" -> "Nakshatra & Pada:"
                "hi" -> "नक्षत्र एवं पद:"
                else -> "नक्षत्र तथा पद:"
            }
            "gochar_sthiti" -> when (lang) {
                "en" -> "Transit Position:"
                "hi" -> "गोचर स्थिति:"
                else -> "गोचर स्थिति:"
            }

            "select_bs_date" -> when (lang) {
                "en" -> "Select BS Date"
                "hi" -> "विक्रमी (BS) तिथि चुनें"
                else -> "वि.सं. मिति छान्नुहोस्"
            }
            "select_ad_date" -> when (lang) {
                "en" -> "Select AD Date"
                "hi" -> "ईस्वी (AD) तिथि चुनें"
                else -> "इ.सं. मिति छान्नुहोस्"
            }
            "converted_ad_date" -> when (lang) {
                "en" -> "Converted AD Date"
                "hi" -> "परिवर्तित ईस्वी (AD) तिथि"
                else -> "रूपान्तरित इ.सं. मिति"
            }
            "converted_bs_date" -> when (lang) {
                "en" -> "Converted BS Date"
                "hi" -> "परिवर्तित विक्रमी (BS) तिथि"
                else -> "रूपान्तरित वि.सं. मिति"
            }
            "tab_bs_to_ad" -> when (lang) {
                "en" -> "BS to AD"
                "hi" -> "BS से AD"
                else -> "वि.सं. बाट इ.सं."
            }
            "tab_ad_to_bs" -> when (lang) {
                "en" -> "AD to BS"
                "hi" -> "AD से BS"
                else -> "इ.सं. बाट वि.सं."
            }
            "tab_age_calc" -> when (lang) {
                "en" -> "Age Calc"
                "hi" -> "आयु गणना"
                else -> "उमेर गणना"
            }
            "years" -> when (lang) {
                "en" -> "Years"
                "hi" -> "वर्ष"
                else -> "वर्ष"
            }
            "months" -> when (lang) {
                "en" -> "Months"
                "hi" -> "महीने"
                else -> "महिना"
            }
            "days" -> when (lang) {
                "en" -> "Days"
                "hi" -> "दिन"
                else -> "दिन"
            }

            "settings_title" -> when (lang) {
                "en" -> "Settings"
                "hi" -> "सेटिंग्स"
                else -> "सेटिङ"
            }
            "app_preferences" -> when (lang) {
                "en" -> "App Preferences"
                "hi" -> "ऐप प्राथमिकताएँ"
                else -> "एप प्राथमिकताहरू"
            }
            "dark_mode" -> when (lang) {
                "en" -> "Dark Mode"
                "hi" -> "डार्क मोड"
                else -> "डार्क मोड"
            }
            "dark_mode_desc" -> when (lang) {
                "en" -> "Toggle app theme"
                "hi" -> "थीम बदलें"
                else -> "थिम परिवर्तन गर्नुहोस्"
            }
            "dynamic_colors" -> when (lang) {
                "en" -> "Dynamic Colors"
                "hi" -> "डायनेमिक रंग"
                else -> "डाइनामिक रङहरू"
            }
            "dynamic_colors_desc" -> when (lang) {
                "en" -> "Use system wallpaper colors"
                "hi" -> "सिस्टम वॉलपेपर रंग का उपयोग करें"
                else -> "सिस्टम वालपेपर रङहरू प्रयोग गर्नुहोस्"
            }
            "language_numerals" -> when (lang) {
                "en" -> "Language & Numerals"
                "hi" -> "भाषा एवं अंक"
                else -> "भाषा तथा अंक"
            }
            "select_app_language" -> when (lang) {
                "en" -> "Select Application Language"
                "hi" -> "एप्लिकेशन भाषा चुनें"
                else -> "एप भाषा छान्नुहोस्"
            }
            "language_settings_desc" -> when (lang) {
                "en" -> "Switching language will immediately update app menus, Panchanga readings, and numeral formats across the entire application."
                "hi" -> "भाषा बदलने से पूरे एप्लिकेशन में ऐप मेनू, पंचांग पठन और अंक प्रारूप तुरंत अपडेट हो जाएंगे।"
                else -> "भाषा परिवर्तन गर्दा एपका मेनुहरू, पञ्चाङ्ग तथा अंक ढाँचा तुरुन्तै परिवर्तन हुनेछन्।"
            }
            "current_lang_label" -> when (lang) {
                "en" -> "Current: "
                "hi" -> "वर्तमान: "
                else -> "हालको: "
            }
            "astrology_preferences" -> when (lang) {
                "en" -> "Astrology & Horoscope Preferences"
                "hi" -> "ज्योतिष और राशिफल प्राथमिकताएँ"
                else -> "ज्योतिष तथा राशिफल प्राथमिकताहरू"
            }
            "default_zodiac" -> when (lang) {
                "en" -> "Default Zodiac Sign"
                "hi" -> "डिफ़ॉल्ट राशि"
                else -> "पूर्वनिर्धारित राशि"
            }
            "selected_label" -> when (lang) {
                "en" -> "Selected: "
                "hi" -> "चयनित: "
                else -> "चयन गरिएको: "
            }
            "change" -> when (lang) {
                "en" -> "Change"
                "hi" -> "बदलें"
                else -> "बदल्नुहोस्"
            }
            "location_data" -> when (lang) {
                "en" -> "Location & Astrological Data"
                "hi" -> "स्थान और ज्योतिषीय डेटा"
                else -> "स्थान तथा ज्योतिषीय डाटा"
            }
            "auto_gps" -> when (lang) {
                "en" -> "Auto Detect GPS Location"
                "hi" -> "ऑटो जीपीएस स्थान"
                else -> "अटो जीपीएस स्थान"
            }
            "auto_gps_desc" -> when (lang) {
                "en" -> "Use exact device location for precise Panchanga calculations"
                "hi" -> "सटीक पंचांग गणना के लिए सटीक डिवाइस स्थान का उपयोग करें"
                else -> "सटीक पञ्चाङ्ग गणनाको लागि उपकरणको वास्तविक स्थान प्रयोग गर्नुहोस्"
            }
            "district_selection" -> when (lang) {
                "en" -> "District / City Selection"
                "hi" -> "जिला / शहर चयन"
                else -> "जिल्ला / सहर छनोट"
            }
            "managed_gps" -> when (lang) {
                "en" -> "Managed by device GPS automatically"
                "hi" -> "डिवाइस जीपीएस द्वारा स्वचालित रूप से प्रबंधित"
                else -> "उपकरणको GPS द्वारा स्वचालित रूपमा व्यवस्थापन गरिएको"
            }
            "about" -> when (lang) {
                "en" -> "About"
                "hi" -> "के बारे में"
                else -> "बारेमा"
            }
            "about_app_guide" -> when (lang) {
                "en" -> "About App & Usage Guide"
                "hi" -> "ऐप के बारे में और उपयोग मार्गदर्शिका"
                else -> "एप बारे तथा प्रयोग निर्देशिका"
            }
            "version_info" -> when (lang) {
                "en" -> "Version info, overview & daily life utility"
                "hi" -> "संस्करण जानकारी, अवलोकन और दैनिक जीवन उपयोगिता"
                else -> "संस्करण जानकारी, सिंहावलोकन तथा दैनिक जीवन उपयोगिता"
            }
            "developer_info" -> when (lang) {
                "en" -> "Developer Information"
                "hi" -> "डेवलपर की जानकारी"
                else -> "विकासकर्ताको जानकारी"
            }
            "visit_website" -> when (lang) {
                "en" -> "Visit Developer Website"
                "hi" -> "डेवलपर वेबसाइट पर जाएँ"
                else -> "विकासकर्ताको वेबसाइट हेर्नुहोस्"
            }
            "select_default_rashi" -> when (lang) {
                "en" -> "Select Default Rashi (Rashifal)"
                "hi" -> "डिफ़ॉल्ट राशि चुनें (राशिफल)"
                else -> "पूर्वनिर्धारित राशि छान्नुहोस् (राशिफल)"
            }
            "select_nepal_district" -> when (lang) {
                "en" -> "Select Nepal District / City"
                "hi" -> "नेपाल जिला / शहर चुनें"
                else -> "नेपालको जिल्ला / सहर छान्नुहोस्"
            }
            "search_city" -> when (lang) {
                "en" -> "Search city (e.g., Butwal, Palpa...)"
                "hi" -> "शहर खोजें (जैसे, बुटवल, पाल्पा...)"
                else -> "सहर खोज्नुहोस् (जस्तै: बुटवल, पाल्पा...)"
            }

            else -> key
        }
    }
}
