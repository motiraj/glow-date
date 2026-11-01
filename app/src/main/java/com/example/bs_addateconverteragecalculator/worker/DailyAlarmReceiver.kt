@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.HolidayData
import com.example.bs_addateconverteragecalculator.data.NepaliDays
import com.example.bs_addateconverteragecalculator.data.NepaliMonths
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import com.example.bs_addateconverteragecalculator.data.RashifalData
import com.example.bs_addateconverteragecalculator.data.RealTimeRashifalGenerator
import com.example.bs_addateconverteragecalculator.data.TithiCalculator
import com.example.bs_addateconverteragecalculator.widget.NepaliDateClockReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class DailyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        // 1. Reschedule for tomorrow 5:00 AM & guarantee midnight alarm is scheduled
        NotificationHelper.scheduleDailyNotification(context)
        NotificationHelper.scheduleMidnightUpdateAlarm(context)
        FestivalNotificationHelper.scheduleFestivalAlarms(context)

        // 2. Update Home Screen & Keyguard Glance Widgets automatically
        NepaliDateClockReceiver.updateAllWidgets(context)

        // 3. Update persistent lock screen & status bar date notification
        NotificationHelper.updatePersistentDateNotification(context)

        // 4. Check for Festival & Parva notifications
        FestivalNotificationHelper.checkAndNotifyFestival(context)

        // 3. Post random/contextual notification for Panchanga, Rashifal, or Barvela & Hora
        val todayAd = LocalDate.now()
        val todayBs = try {
            DateConverterEngine.convertAdToBs(todayAd)
        } catch (_: Exception) {
            null
        }

        if (todayBs != null) {
            val monthNe = NepaliMonths.getMonthName(todayBs.month, true)
            val dayNe = NepaliNumerals.toNepaliDigits(todayBs.day)
            val yearNe = NepaliNumerals.toNepaliDigits(todayBs.year)
            val dayOfWeekNe = NepaliDays.getDayName(todayAd.dayOfWeek.value % 7, true)
            
            val tithi = TithiCalculator.getShortTithi(todayBs.year, todayBs.month, todayBs.day)
            val holiday = HolidayData.getHoliday(todayBs.year, todayBs.month, todayBs.day)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val prefs = PreferencesManager(context)
                    val zodiacId = prefs.userZodiacFlow.first()
                    val chosenSign = RashifalData.signs.find { it.id == zodiacId } ?: RashifalData.signs[0]
                    val dailyRashifal = RealTimeRashifalGenerator.generateDailyPrediction(chosenSign.id, todayAd)

                    // Randomly select notification topic: 1 = Panchanga, 2 = Rashifal, 3 = Barvela & Hora
                    val topicChoice = (1..3).random()

                    when (topicChoice) {
                        1 -> {
                            // Panchanga & Parva Notification
                            val title = "🌟 आजको पञ्चाङ्ग: $monthNe $dayNe गते ($dayOfWeekNe)"
                            var content = "तिथि: $tithi • $yearNe"
                            if (holiday != null) {
                                content += " | 🔴 बिदा: ${holiday.titleNepali}"
                            }
                            content += "\nआजको पावन पञ्चाङ्ग तथा शुभ अवसर हेर्नुहोस्।"
                            NotificationHelper.showNotification(context, title, content, "panchanga")
                        }
                        2 -> {
                            // Daily Rashifal Notification
                            val title = "⭐ आजको ${chosenSign.nameNepali} राशिफल (${chosenSign.symbol})"
                            val content = "$dailyRashifal\n\nतपाईंको आजको पूर्ण दैनिक, साप्ताहिक र वार्षिक राशिफल हेर्नुहोस्।"
                            NotificationHelper.showNotification(context, title, content, "rashifal")
                        }
                        3 -> {
                            // Barvela & Hora Muhurat Notification
                            val title = "⏳ आजको वारबेला तथा होरा मुहूर्त"
                            val content = "शुभ, लाभ र अमृत बेलामा विशेष कार्यको थालनी गर्नुहोस्। आजको पूर्ण शुभ मुहूर्त समय हेर्नुहोस्।"
                            NotificationHelper.showNotification(context, title, content, "panchanga")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pendingResult.finish()
                }
            }
        } else {
            pendingResult.finish()
        }
    }
}
