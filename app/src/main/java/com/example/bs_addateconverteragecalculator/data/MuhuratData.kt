@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

data class MuhuratEvent(
    val title: String,
    val datesNepali: List<String>
)

object MuhuratData {
    // 2083 BS Auspicious Muhurats (Sample Data aligned to standard Panchanga formats)
    fun getMuhuratsForYear(year: Int): List<MuhuratEvent> {
        if (year == 2083) {
            return listOf(
                MuhuratEvent(
                    title = "💍 विवाह लगन (Marriage)",
                    datesNepali = listOf(
                        "वैशाख: ५, १२, १४, १९, २२, २३",
                        "जेठ: २, ४, ९, ११, १७, २४",
                        "असार: ६, ११, १२, १५",
                        "मंसिर: ५, ८, ११, १४, १८, २२",
                        "माघ: २, ५, ९, १२, १६",
                        "फागुन: ३, ७, १०, १४, १९, २३"
                    )
                ),
                MuhuratEvent(
                    title = "🕉️ व्रतबन्ध (Bratabandha)",
                    datesNepali = listOf(
                        "वैशाख: ७, १४, २१",
                        "जेठ: ३, १०, १७",
                        "असार: ५, १२",
                        "माघ: ८, १५",
                        "फागुन: ५, १२, १९",
                        "चैत: ३, १०"
                    )
                ),
                MuhuratEvent(
                    title = "🍚 अन्न प्राशन (Pasni)",
                    datesNepali = listOf(
                        "वैशाख: ४, ११, १८, २५",
                        "जेठ: २, ९, १६, २३",
                        "असार: ७, १४, २१",
                        "साउन: ४, ११, १८",
                        "भदौ: २, ९, १६",
                        "असोज: ५, १२",
                        "मंसिर: ४, ११, १८",
                        "माघ: ६, १३, २०",
                        "फागुन: ४, ११, १८"
                    )
                ),
                MuhuratEvent(
                    title = "🏠 गृह प्रवेश (Grihapravesh)",
                    datesNepali = listOf(
                        "वैशाख: ६, १३, २०",
                        "जेठ: ५, १२, १९",
                        "मंसिर: ७, १४, २१",
                        "माघ: ९, १६, २३",
                        "फागुन: ७, १४, २१"
                    )
                ),
                MuhuratEvent(
                    title = "🏗️ गृह आरम्भ (House Construction)",
                    datesNepali = listOf(
                        "वैशाख: ३, १०, १७, २४",
                        "जेठ: १, ८, १५, २२",
                        "कात्तिक: ५, १२, १९",
                        "मंसिर: ३, १०, १७",
                        "फागुन: २, ९, १६, २३"
                    )
                ),
                MuhuratEvent(
                    title = "🏪 दोकान खोल्ने / व्यापार (Shop Opening)",
                    datesNepali = listOf(
                        "वैशाख: २, ९, १६, २३",
                        "जेठ: ७, १४, २१",
                        "असोज: ४, ११, १८",
                        "कात्तिक: २, ९, १६",
                        "मंसिर: १, ८, १५, २२",
                        "माघ: ३, १०, १७, २४",
                        "फागुन: ५, १२, १९"
                    )
                ),
                MuhuratEvent(
                    title = "🔥 अग्नि जुर्ने (Agni Jurne)",
                    datesNepali = listOf(
                        "वैशाख: १, ८, १५, २२",
                        "जेठ: ३, १०, १७, २४",
                        "असार: ४, ११, १८, २५",
                        "साउन: ५, १२, १९",
                        "भदौ: ६, १३, २०",
                        "असोज: ७, १४, २१",
                        "कात्तिक: १, ८, १५, २२",
                        "मंसिर: २, ९, १६, २३",
                        "पुस: ३, १०, १७, २४",
                        "माघ: ४, ११, १८, २५",
                        "फागुन: ५, १२, १९",
                        "चैत: ६, १३, २०"
                    )
                ),
                MuhuratEvent(
                    title = "🙏 रुद्री जुर्ने (Rudri Jurne)",
                    datesNepali = listOf(
                        "वैशाख: २, ९, १६, २३",
                        "जेठ: ४, ११, १८, २५",
                        "असार: ५, १२, १९",
                        "साउन: ६, १३, २०",
                        "भदौ: ७, १४, २१",
                        "असोज: १, ८, १५, २२",
                        "कात्तिक: २, ९, १६, २३",
                        "मंसिर: ३, १०, १७, २४",
                        "पुस: ४, ११, १८, २५",
                        "माघ: ५, १२, १९",
                        "फागुन:  ६, १३, २०",
                        "चैत: ७, १४, २१"
                    )
                )
            )
        }
        
        // Return placeholder for other years
        return listOf(
            MuhuratEvent("💍 विवाह", listOf("पात्रो उपलब्ध छैन")),
            MuhuratEvent("🕉️ व्रतबन्ध", listOf("पात्रो उपलब्ध छैन")),
            MuhuratEvent("🍚 अन्न प्राशन", listOf("पात्रो उपलब्ध छैन")),
            MuhuratEvent("🏠 गृह प्रवेश", listOf("पात्रो उपलब्ध छैन")),
            MuhuratEvent("🏗️ गृह आरम्भ", listOf("पात्रो उपलब्ध छैन")),
            MuhuratEvent("🏪 दोकान खोल्ने", listOf("पात्रो उपलब्ध छैन")),
            MuhuratEvent("🔥 अग्नि जुर्ने", listOf("पात्रो उपलब्ध छैन")),
            MuhuratEvent("🙏 रुद्री जुर्ने", listOf("पात्रो उपलब्ध छैन"))
        )
    }
}
