package com.example.bs_addateconverteragecalculator.widget

import android.content.Context
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.bs_addateconverteragecalculator.R
import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.NakshatraYogaCalculator
import com.example.bs_addateconverteragecalculator.data.NepaliDays
import com.example.bs_addateconverteragecalculator.data.NepaliMonths
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import com.example.bs_addateconverteragecalculator.data.PanchangaCalculator
import com.example.bs_addateconverteragecalculator.data.TithiCalculator
import java.time.LocalDate

class PanchangaWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            androidx.glance.GlanceTheme {
                PanchangaWidgetContent(context = context)
            }
        }
    }

    @Composable
    fun PanchangaWidgetContent(context: Context) {
        val todayAd = LocalDate.now()
        val currentBsYearApprox = todayAd.year + 57
        val todayBs = try {
            DateConverterEngine.convertAdToBs(todayAd)
        } catch (_: Exception) {
            com.example.bs_addateconverteragecalculator.data.NepaliDate(currentBsYearApprox, 1, 1)
        }
        val activeBs = todayBs ?: com.example.bs_addateconverteragecalculator.data.NepaliDate(currentBsYearApprox, 1, 1)

        val monthNe = try { NepaliMonths.getMonthName(activeBs.month, true) } catch (_: Exception) { "वैशाख" }
        val dayNe = try { NepaliNumerals.toNepaliDigits(activeBs.day) } catch (_: Exception) { "१" }
        val yearNe = try { NepaliNumerals.toNepaliDigits(activeBs.year) } catch (_: Exception) { NepaliNumerals.toNepaliDigits(currentBsYearApprox) }
        val dayOfWeekNe = try { NepaliDays.getDayName(todayAd.dayOfWeek.value % 7, true) } catch (_: Exception) { "आइतबार" }

        val lat = 27.7172
        val lon = 85.3240
        val tz = 5.75

        // 5 ANGAS OF PANCHANGA
        val tithiShort = try { if (todayBs != null) TithiCalculator.getShortTithi(todayBs.year, todayBs.month, todayBs.day) else "शुक्ल नवमी" } catch (_: Exception) { "शुक्ल नवमी" }
        
        val nakshatraName = try {
            val nVal = NakshatraYogaCalculator.getNakshatraValue(todayAd, 6.0, tz)
            NakshatraYogaCalculator.nakshatras.getOrNull(nVal.toInt() % 27) ?: "उत्तराषाढा"
        } catch (_: Exception) {
            "उत्तराषाढा"
        }

        val yogaName = try {
            val yVal = NakshatraYogaCalculator.getYogaValue(todayAd, 6.0, tz)
            NakshatraYogaCalculator.yogas.getOrNull(yVal.toInt() % 27) ?: "सौभाग्य"
        } catch (_: Exception) {
            "सौभाग्य"
        }

        val karanaName = try {
            TithiCalculator.getKarana(TithiCalculator.getTithiValue(todayAd, 6.0, tz))
        } catch (_: Exception) {
            "कौलव"
        }
        
        val (sunriseStr, sunsetStr) = try {
            PanchangaCalculator.getSunriseSunset(todayAd, lat, lon, tz)
        } catch (_: Exception) {
            "०६:०५ AM" to "०५:५५ PM"
        }

        val deepLinkAction = KundaliWidgetTheme.getDeepLinkAction(context, "panchanga")

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(KundaliWidgetTheme.DeepCosmicNavy)
                .cornerRadius(18.dp)
                .padding(10.dp)
                .clickable(deepLinkAction)
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) {
                // Header Row: Title & Location Pin
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = GlanceModifier.defaultWeight()) {
                        Text(
                            text = "🌞 आजको पञ्चाङ्ग",
                            style = TextStyle(
                                color = KundaliWidgetTheme.SolarOrange,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "📍 काठमाडौँ • $monthNe $dayNe, $yearNe ($dayOfWeekNe)",
                            style = TextStyle(
                                color = KundaliWidgetTheme.SlateSilver,
                                fontSize = 10.sp
                            )
                        )
                    }

                    // Native Live Clock
                    AndroidRemoteViews(
                        remoteViews = RemoteViews(context.packageName, R.layout.widget_text_clock_kundali)
                    )
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                // 2-Column Grid of 5 Angas
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .defaultWeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Column: Tithi & Nakshatra
                    Column(
                        modifier = GlanceModifier
                            .defaultWeight()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PanchangaAngaCard(
                            label = "तिथि (Tithi)",
                            value = tithiShort,
                            modifier = GlanceModifier.fillMaxWidth().defaultWeight()
                        )
                        Spacer(modifier = GlanceModifier.height(6.dp))
                        PanchangaAngaCard(
                            label = "नक्षत्र (Nakshatra)",
                            value = nakshatraName,
                            modifier = GlanceModifier.fillMaxWidth().defaultWeight()
                        )
                    }

                    Spacer(modifier = GlanceModifier.width(6.dp))

                    // Right Column: Yoga, Karana & Sunrise/Sunset
                    Column(
                        modifier = GlanceModifier
                            .defaultWeight()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PanchangaAngaCard(
                                label = "योग (Yoga)",
                                value = yogaName,
                                modifier = GlanceModifier.defaultWeight().fillMaxHeight()
                            )
                            Spacer(modifier = GlanceModifier.width(4.dp))
                            PanchangaAngaCard(
                                label = "करण (Karana)",
                                value = karanaName,
                                modifier = GlanceModifier.defaultWeight().fillMaxHeight()
                            )
                        }
                        Spacer(modifier = GlanceModifier.height(6.dp))
                        PanchangaAngaCard(
                            label = "सूर्योदय / सूर्यास्त",
                            value = "🌅 $sunriseStr / 🌇 $sunsetStr",
                            modifier = GlanceModifier.fillMaxWidth().defaultWeight()
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun PanchangaAngaCard(
        label: String,
        value: String,
        modifier: GlanceModifier = GlanceModifier
    ) {
        Box(
            modifier = modifier
                .background(KundaliWidgetTheme.MidnightSlate)
                .cornerRadius(10.dp)
                .padding(6.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    text = label,
                    style = TextStyle(
                        color = KundaliWidgetTheme.SlateSilver,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = GlanceModifier.height(2.dp))
                Text(
                    text = value,
                    style = TextStyle(
                        color = KundaliWidgetTheme.CrispWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

class PanchangaReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PanchangaWidget()
}
