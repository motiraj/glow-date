package com.example.bs_addateconverteragecalculator.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
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
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import com.example.bs_addateconverteragecalculator.data.RashifalData
import com.example.bs_addateconverteragecalculator.data.RealTimeRashifalGenerator
import java.time.LocalDate

class RashifalWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            androidx.glance.GlanceTheme {
                val prefs = currentState<Preferences>()
                val zodiacId = prefs[PreferencesManager.USER_ZODIAC_KEY] ?: "aries"
                RashifalWidgetContent(context = context, zodiacId = zodiacId)
            }
        }
    }

    @Composable
    fun RashifalWidgetContent(context: Context, zodiacId: String) {
        val todayAd = LocalDate.now()
        val sign = try {
            RashifalData.signs.find { it.id == zodiacId } ?: RashifalData.signs[0]
        } catch (_: Exception) {
            RashifalData.signs[0]
        }
        val basePrediction = try { RashifalData.getPrediction(sign.id) } catch (_: Exception) { RashifalData.getPrediction("aries") }
        val dailyPredictionText = try { RealTimeRashifalGenerator.generateDailyPrediction(sign.id, todayAd) } catch (_: Exception) { "आज दिन राम्रो रहनेछ।" }

        val deepLinkAction = KundaliWidgetTheme.getDeepLinkAction(context, "rashifal")

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(KundaliWidgetTheme.DeepCosmicNavy)
                .cornerRadius(18.dp)
                .padding(10.dp)
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) {
                // Header Row: Title & Interactive Cycle Sign Button
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⭐ दैनिक राशिफल",
                        style = TextStyle(
                            color = KundaliWidgetTheme.SolarOrange,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    
                    // Interactive Button to Cycle Zodiac Sign
                    Box(
                        modifier = GlanceModifier
                            .background(KundaliWidgetTheme.MidnightSlate)
                            .cornerRadius(12.dp)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable(actionRunCallback<CycleZodiacActionCallback>()),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🔄 अर्को राशि",
                            style = TextStyle(
                                color = KundaliWidgetTheme.SolarAmber,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                // Main Content Card (Clickable to deep link into Rashifal tab)
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .defaultWeight()
                        .clickable(deepLinkAction),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Box: Zodiac Symbol & Solar Aura Badge
                    Column(
                        modifier = GlanceModifier
                            .width(85.dp)
                            .fillMaxHeight()
                            .background(KundaliWidgetTheme.MidnightSlate)
                            .cornerRadius(12.dp)
                            .padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sign.symbol,
                            style = TextStyle(
                                color = KundaliWidgetTheme.SolarAmber,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = GlanceModifier.height(2.dp))
                        Text(
                            text = sign.nameNepali,
                            style = TextStyle(
                                color = KundaliWidgetTheme.CrispWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "(${sign.nameEnglish})",
                            style = TextStyle(
                                color = KundaliWidgetTheme.SlateSilver,
                                fontSize = 9.sp
                            )
                        )
                    }

                    Spacer(modifier = GlanceModifier.width(8.dp))

                    // Right Box: Short Prediction Text
                    Box(
                        modifier = GlanceModifier
                            .defaultWeight()
                            .fillMaxHeight()
                            .background(KundaliWidgetTheme.MidnightSlate)
                            .cornerRadius(12.dp)
                            .padding(10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column {
                            Text(
                                text = "${sign.nameNepali} — आजको भविष्यवाणी",
                                style = TextStyle(
                                    color = KundaliWidgetTheme.SolarAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = GlanceModifier.height(4.dp))
                            Text(
                                text = dailyPredictionText,
                                style = TextStyle(
                                    color = KundaliWidgetTheme.CrispWhite,
                                    fontSize = 11.sp
                                ),
                                maxLines = 3
                            )
                        }
                    }
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                // Footer: Astrological Highlights (Lucky Color & Lucky Number)
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .clickable(deepLinkAction),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎨 शुभ रंग: ${basePrediction.luckyColor} | 🔢 शुभ अंक: ${basePrediction.luckyNumber}",
                        style = TextStyle(
                            color = KundaliWidgetTheme.SlateSilver,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text(
                        text = "विस्तृत ➔",
                        style = TextStyle(
                            color = KundaliWidgetTheme.SolarOrange,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

class CycleZodiacActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val current = prefs[PreferencesManager.USER_ZODIAC_KEY] ?: "aries"
            val currentIndex = RashifalData.signs.indexOfFirst { it.id == current }
            val nextIndex = if (currentIndex >= 0) (currentIndex + 1) % RashifalData.signs.size else 0
            prefs[PreferencesManager.USER_ZODIAC_KEY] = RashifalData.signs[nextIndex].id
        }
        RashifalWidget().update(context, glanceId)
    }
}

class RashifalReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RashifalWidget()
}
