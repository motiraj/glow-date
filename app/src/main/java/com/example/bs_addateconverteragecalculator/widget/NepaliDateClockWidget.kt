@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.LocalAppWidgetOptions
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
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
import com.example.bs_addateconverteragecalculator.data.HolidayData
import com.example.bs_addateconverteragecalculator.data.NepalSambatCalculator
import com.example.bs_addateconverteragecalculator.data.NepaliDate
import com.example.bs_addateconverteragecalculator.data.NepaliDays
import com.example.bs_addateconverteragecalculator.data.NepaliMonths
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import com.example.bs_addateconverteragecalculator.data.TithiCalculator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class NepaliDateClockWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            androidx.glance.GlanceTheme {
                NepaliDateClockWidgetContent(context = context)
            }
        }
    }

    @Composable
    fun NepaliDateClockWidgetContent(context: Context) {
        val options = LocalAppWidgetOptions.current
        val hostCategory = options.getInt(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY, AppWidgetProviderInfo.WIDGET_CATEGORY_HOME_SCREEN)
        val isKeyguard = (hostCategory and AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD) != 0

        val widgetSize = LocalSize.current
        val isUltraCompact = widgetSize.height < 70.dp
        val isCompact = widgetSize.height < 95.dp || widgetSize.width < 240.dp

        val todayAd = LocalDate.now()
        val currentBsYearApprox = todayAd.year + 57
        val todayBs = try {
            DateConverterEngine.convertAdToBs(todayAd)
        } catch (_: Exception) {
            NepaliDate(currentBsYearApprox, 1, 1)
        }
        val activeBs = todayBs ?: NepaliDate(currentBsYearApprox, 1, 1)

        val monthNe = try { NepaliMonths.getMonthName(activeBs.month, true) } catch (_: Exception) { "वैशाख" }
        val dayNe = try { NepaliNumerals.toNepaliDigits(activeBs.day) } catch (_: Exception) { "१" }
        val yearNe = try { NepaliNumerals.toNepaliDigits(activeBs.year) } catch (_: Exception) { NepaliNumerals.toNepaliDigits(currentBsYearApprox) }
        val dayOfWeekNe = try { NepaliDays.getDayName(todayAd.dayOfWeek.value % 7, true) } catch (_: Exception) { "आइतबार" }

        val adFormatted = try { todayAd.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)) } catch (_: Exception) { "" }
        val tithi = try { TithiCalculator.getShortTithi(activeBs.year, activeBs.month, activeBs.day) } catch (_: Exception) { "शुक्ल पक्ष" }
        val holiday = try { HolidayData.getHoliday(activeBs.year, activeBs.month, activeBs.day) } catch (_: Exception) { null }

        val nsDetails = try {
            val cleanTithi = tithi.split(" ").lastOrNull() ?: "प्रतिपदा"
            val paksha = if (tithi.contains("कृष्ण")) "कृष्ण" else "शुक्ल"
            NepalSambatCalculator.getNepalSambatDetails(activeBs.year, activeBs.month, activeBs.day, paksha, cleanTithi)
        } catch (_: Exception) {
            val approxNs = activeBs.year - 936
            "नेपाल संवत् ${NepaliNumerals.toNepaliDigits(approxNs)}"
        }

        val routePanchanga = "panchanga"
        val deepLinkAction = KundaliWidgetTheme.getDeepLinkAction(context, routePanchanga)

        if (isKeyguard) {
            // Refined Keyguard / Lock Screen Layout
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(KundaliWidgetTheme.KeyguardScrim)
                    .cornerRadius(14.dp)
                    .padding(8.dp)
                    .clickable(deepLinkAction)
            ) {
                Column(
                    modifier = GlanceModifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "वि.सं. $yearNe $monthNe $dayNe",
                            style = TextStyle(
                                color = KundaliWidgetTheme.SolarAmber,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = tithi,
                            style = TextStyle(
                                color = KundaliWidgetTheme.CrispWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                    Spacer(modifier = GlanceModifier.height(2.dp))
                    Text(
                        text = "$dayOfWeekNe • $nsDetails",
                        style = TextStyle(
                            color = KundaliWidgetTheme.SlateSilver,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        } else if (isUltraCompact) {
            // Ultra-compact 2x1 cell layout without overflow
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(KundaliWidgetTheme.DeepCosmicNavy)
                    .cornerRadius(16.dp)
                    .padding(6.dp)
                    .clickable(deepLinkAction)
            ) {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(KundaliWidgetTheme.MidnightSlate)
                        .cornerRadius(12.dp)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Column(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "वि.सं. $yearNe $monthNe $dayNe गते",
                                style = TextStyle(
                                    color = KundaliWidgetTheme.SolarGold,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = GlanceModifier.defaultWeight())
                            AndroidRemoteViews(
                                remoteViews = RemoteViews(context.packageName, R.layout.widget_text_clock_kundali)
                            )
                        }
                        Spacer(modifier = GlanceModifier.height(2.dp))
                        Text(
                            text = "$dayOfWeekNe • $tithi",
                            style = TextStyle(
                                color = KundaliWidgetTheme.CrispWhite,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        } else if (isCompact) {
            // Compact 3x1 / 4x1 standard layout
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(KundaliWidgetTheme.DeepCosmicNavy)
                    .cornerRadius(18.dp)
                    .padding(8.dp)
                    .clickable(deepLinkAction)
            ) {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(KundaliWidgetTheme.MidnightSlate)
                        .cornerRadius(14.dp)
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = GlanceModifier
                                    .background(KundaliWidgetTheme.AmberBadge)
                                    .cornerRadius(6.dp)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "🌟 Glow Date",
                                    style = TextStyle(
                                        color = KundaliWidgetTheme.SolarAmber,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = GlanceModifier.defaultWeight())
                            AndroidRemoteViews(
                                remoteViews = RemoteViews(context.packageName, R.layout.widget_text_clock_kundali)
                            )
                        }

                        Spacer(modifier = GlanceModifier.height(4.dp))

                        Text(
                            text = "वि.सं. $yearNe $monthNe $dayNe गते, $dayOfWeekNe",
                            style = TextStyle(
                                color = KundaliWidgetTheme.CrispWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = GlanceModifier.height(2.dp))

                        Text(
                            text = "$tithi • $nsDetails",
                            style = TextStyle(
                                color = KundaliWidgetTheme.SlateSilver,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        } else {
            // Full Premium Adaptive Layout (Tablets, Large Phones & Expanded Widgets)
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(KundaliWidgetTheme.DeepCosmicNavy)
                    .cornerRadius(20.dp)
                    .padding(10.dp)
                    .clickable(deepLinkAction)
            ) {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(KundaliWidgetTheme.MidnightSlate)
                        .cornerRadius(16.dp)
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Header Row: App Pill & Native Hardware Live TextClock
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = GlanceModifier
                                    .background(KundaliWidgetTheme.AmberBadge)
                                    .cornerRadius(8.dp)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "🌟 Glow Date",
                                    style = TextStyle(
                                        color = KundaliWidgetTheme.SolarAmber,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = GlanceModifier.defaultWeight())
                            Box(
                                modifier = GlanceModifier
                                    .background(KundaliWidgetTheme.SurfaceCard)
                                    .cornerRadius(8.dp)
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                AndroidRemoteViews(
                                    remoteViews = RemoteViews(context.packageName, R.layout.widget_text_clock_kundali)
                                )
                            }
                        }

                        Spacer(modifier = GlanceModifier.height(6.dp))

                        // Primary Nepali Date Display
                        Text(
                            text = "वि.सं. $yearNe $monthNe $dayNe गते",
                            style = TextStyle(
                                color = KundaliWidgetTheme.SolarGold,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = GlanceModifier.height(3.dp))

                        // Subtitle: Day of week, Tithi & Nepal Sambat
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dayOfWeekNe,
                                style = TextStyle(
                                    color = KundaliWidgetTheme.SolarOrange,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = GlanceModifier.width(6.dp))
                            Text(
                                text = "•  $tithi  •  $nsDetails",
                                style = TextStyle(
                                    color = KundaliWidgetTheme.SlateSilver,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Spacer(modifier = GlanceModifier.height(4.dp))

                        // Bottom Row: AD Date & Holiday tag
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AD: $adFormatted",
                                style = TextStyle(
                                    color = KundaliWidgetTheme.SlateSilver,
                                    fontSize = 10.sp
                                )
                            )
                            if (holiday != null) {
                                Spacer(modifier = GlanceModifier.width(8.dp))
                                Box(
                                    modifier = GlanceModifier
                                        .background(KundaliWidgetTheme.SurfaceCard)
                                        .cornerRadius(6.dp)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "🔴 ${holiday.titleNepali}",
                                        style = TextStyle(
                                            color = KundaliWidgetTheme.MutedRed,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class NepaliDateClockReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NepaliDateClockWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action ?: return
        if (action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_DATE_CHANGED ||
            action == Intent.ACTION_TIME_CHANGED ||
            action == Intent.ACTION_TIME_TICK
        ) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    updateAllWidgetsSuspending(context)
                    com.example.bs_addateconverteragecalculator.worker.NotificationHelper.updatePersistentDateNotification(context)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }

    companion object {
        suspend fun updateAllWidgetsSuspending(context: Context) {
            try {
                NepaliDateClockWidget().updateAll(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                PanchangaWidget().updateAll(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                RashifalWidget().updateAll(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                NepaliCalendarWidget().updateAll(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun updateAllWidgets(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                updateAllWidgetsSuspending(context)
            }
        }
    }
}
