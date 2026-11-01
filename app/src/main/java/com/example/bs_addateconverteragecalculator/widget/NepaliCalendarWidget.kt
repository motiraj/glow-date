package com.example.bs_addateconverteragecalculator.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
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
import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.BsCalendarData
import com.example.bs_addateconverteragecalculator.data.HolidayData
import com.example.bs_addateconverteragecalculator.data.NepaliDate
import com.example.bs_addateconverteragecalculator.data.NepaliMonths
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import java.time.LocalDate

class NepaliCalendarWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition
    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            androidx.glance.GlanceTheme {
                val prefs = currentState<Preferences>()
                val monthOffset = prefs[PreferencesManager.CALENDAR_MONTH_OFFSET_KEY] ?: 0
                NepaliCalendarWidgetContent(context = context, monthOffset = monthOffset)
            }
        }
    }

    @Composable
    fun NepaliCalendarWidgetContent(context: Context, monthOffset: Int) {
        val widgetSize = LocalSize.current
        val isCompactHeight = widgetSize.height < 170.dp
        val isNarrowWidth = widgetSize.width < 240.dp

        val todayAd = LocalDate.now()
        val todayBs = try {
            DateConverterEngine.convertAdToBs(todayAd)
        } catch (_: Exception) {
            null
        }

        val baseYear = todayBs?.year ?: 2083
        val baseMonth = todayBs?.month ?: 1
        val todayBsDay = todayBs?.day ?: 1

        // Compute displayed year and month based on interactive monthOffset
        var targetMonth = baseMonth + monthOffset
        var targetYear = baseYear
        while (targetMonth > 12) {
            targetMonth -= 12
            targetYear += 1
        }
        while (targetMonth < 1) {
            targetMonth += 12
            targetYear -= 1
        }

        val monthNe = NepaliMonths.getMonthName(targetMonth, true)
        val yearNe = NepaliNumerals.toNepaliDigits(targetYear)
        val daysInMonth = BsCalendarData.getDaysInMonth(targetYear, targetMonth)
        
        // Find starting day of week for 1st of target BS month
        val firstAdDate = try {
            DateConverterEngine.convertBsToAd(NepaliDate(targetYear, targetMonth, 1))
        } catch (_: Exception) {
            todayAd
        }
        val startDayOfWeek = (firstAdDate.dayOfWeek.value % 7) // 0=Sunday, 6=Saturday

        val deepLinkAction = KundaliWidgetTheme.getDeepLinkAction(context, "calendar")

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(KundaliWidgetTheme.DeepCosmicNavy)
                .cornerRadius(18.dp)
                .padding(if (isCompactHeight) 6.dp else 10.dp)
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) {
                // Header Row: Month Title & Interactive Mini-Navigation Controls
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📅 $monthNe $yearNe",
                        style = TextStyle(
                            color = KundaliWidgetTheme.SolarGold,
                            fontSize = if (isNarrowWidth) 12.sp else 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = GlanceModifier.defaultWeight())

                    // Mini-Navigation Controls (Backward / Today Reset / Forward)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = GlanceModifier
                                .background(KundaliWidgetTheme.SurfaceCard)
                                .cornerRadius(8.dp)
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                                .clickable(
                                    actionRunCallback<ChangeCalendarMonthActionCallback>(
                                        actionParametersOf(ChangeCalendarMonthActionCallback.MONTH_DELTA_KEY to -1)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "◀",
                                style = TextStyle(
                                    color = KundaliWidgetTheme.SolarAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = GlanceModifier.width(4.dp))

                        if (monthOffset != 0) {
                            Box(
                                modifier = GlanceModifier
                                    .background(KundaliWidgetTheme.AmberBadge)
                                    .cornerRadius(8.dp)
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                                    .clickable(
                                        actionRunCallback<ChangeCalendarMonthActionCallback>(
                                            actionParametersOf(ChangeCalendarMonthActionCallback.MONTH_DELTA_KEY to 0)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "आज",
                                    style = TextStyle(
                                        color = KundaliWidgetTheme.SolarAmber,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = GlanceModifier.width(4.dp))
                        }

                        Box(
                            modifier = GlanceModifier
                                .background(KundaliWidgetTheme.SurfaceCard)
                                .cornerRadius(8.dp)
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                                .clickable(
                                    actionRunCallback<ChangeCalendarMonthActionCallback>(
                                        actionParametersOf(ChangeCalendarMonthActionCallback.MONTH_DELTA_KEY to 1)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "▶",
                                style = TextStyle(
                                    color = KundaliWidgetTheme.SolarAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = GlanceModifier.height(if (isCompactHeight) 4.dp else 6.dp))

                // Days of Week Header (7 columns: Sun..Sat, Saturday in Muted Red)
                val headers = listOf("आइ", "सो", "मं", "बु", "बि", "शु", "शनि")
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    headers.forEach { h ->
                        val isSaturday = h == "शनि"
                        Text(
                            text = h,
                            style = TextStyle(
                                color = if (isSaturday) KundaliWidgetTheme.MutedRed else KundaliWidgetTheme.SlateSilver,
                                fontSize = if (isCompactHeight) 9.sp else 10.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = GlanceModifier.defaultWeight()
                        )
                    }
                }

                Spacer(modifier = GlanceModifier.height(if (isCompactHeight) 2.dp else 4.dp))

                // 7x6 Calendar Month Day Grid
                val gridCells = mutableListOf<Int?>()
                repeat(startDayOfWeek) { gridCells.add(null) }
                for (d in 1..daysInMonth) { gridCells.add(d) }
                while (gridCells.size % 7 != 0) { gridCells.add(null) }

                val weeks = gridCells.chunked(7)
                val dayFontSize = if (isCompactHeight) 10.sp else 11.sp
                val cellCornerRad = if (isCompactHeight) 4.dp else 6.dp
                val cellPad = if (isCompactHeight) 0.5.dp else 1.dp

                Column(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .defaultWeight()
                        .clickable(deepLinkAction),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    weeks.forEach { week ->
                        Row(
                            modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            week.forEachIndexed { colIndex, dayNum ->
                                if (dayNum != null) {
                                    val isToday = (targetYear == baseYear && targetMonth == baseMonth && dayNum == todayBsDay)
                                    val isSaturday = (colIndex == 6)
                                    val holiday = HolidayData.getHoliday(targetYear, targetMonth, dayNum)
                                    val isHoliday = (holiday != null || isSaturday)

                                    Box(
                                        modifier = GlanceModifier
                                            .defaultWeight()
                                            .fillMaxHeight()
                                            .padding(cellPad)
                                            .background(
                                                if (isToday) 
                                                    KundaliWidgetTheme.SolarGold
                                                else 
                                                    KundaliWidgetTheme.MidnightSlate
                                            )
                                            .cornerRadius(cellCornerRad),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = NepaliNumerals.toNepaliDigits(dayNum),
                                            style = TextStyle(
                                                color = when {
                                                    isToday -> KundaliWidgetTheme.DeepCosmicNavy
                                                    isHoliday -> KundaliWidgetTheme.MutedRed
                                                    else -> KundaliWidgetTheme.CrispWhite
                                                },
                                                fontSize = dayFontSize,
                                                fontWeight = if (isToday || isHoliday) FontWeight.Bold else FontWeight.Medium
                                            )
                                        )
                                    }
                                } else {
                                    Spacer(modifier = GlanceModifier.defaultWeight())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class ChangeCalendarMonthActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val delta = parameters[ChangeCalendarMonthActionCallback.MONTH_DELTA_KEY] ?: 0
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentOffset = prefs[PreferencesManager.CALENDAR_MONTH_OFFSET_KEY] ?: 0
            val newOffset = if (delta == 0) 0 else (currentOffset + delta).coerceIn(-24, 24)
            prefs[PreferencesManager.CALENDAR_MONTH_OFFSET_KEY] = newOffset
        }
        NepaliCalendarWidget().update(context, glanceId)
    }

    companion object {
        val MONTH_DELTA_KEY = ActionParameters.Key<Int>("month_delta")
    }
}

class NepaliCalendarReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NepaliCalendarWidget()
}
