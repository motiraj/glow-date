@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.worker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.bs_addateconverteragecalculator.MainActivity
import com.example.bs_addateconverteragecalculator.R
import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.FestivalInfo
import com.example.bs_addateconverteragecalculator.data.NepaliDate
import com.example.bs_addateconverteragecalculator.data.NepaliMonths
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import com.example.bs_addateconverteragecalculator.data.ParvaFestivalData
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import com.example.bs_addateconverteragecalculator.data.TithiCalculator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.concurrent.TimeUnit

object FestivalNotificationHelper {

    const val CHANNEL_ID = "festival_parva_channel_v1"
    const val NOTIFICATION_ID = 3001

    private const val PREFS_NAME = "festival_notification_tracker"
    private const val KEY_LAST_ADVANCE_NOTIFIED = "last_advance_notified_festival"
    private const val KEY_LAST_DAY_NOTIFIED = "last_day_notified_festival"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "चाडपर्व तथा विशेष पर्व सूचना (Festival & Parva Alerts)"
            val descriptionText = "नेपाली चाडपर्व, विशेष उत्सव, एकादशी तथा पावन व्रत प्रारम्भ हुनु अगावै विशेषता, धार्मिक महत्व र विधिको सूचना"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.parseColor("#F59E0B") // Saffron Gold
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Checks if today or tomorrow has a festival, sacred vrata or Ekadashi,
     * and sends a dedicated notification with significance and rituals.
     */
    fun checkAndNotifyFestival(context: Context) {
        val prefs = PreferencesManager(context)
        val isEnabled = runBlocking {
            try {
                prefs.festivalNotificationFlow.first()
            } catch (_: Exception) {
                true
            }
        }
        if (!isEnabled) return

        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val todayAd = LocalDate.now()
        val now = LocalTime.now()

        val todayBs = try {
            DateConverterEngine.convertAdToBs(todayAd)
        } catch (_: Exception) {
            null
        } ?: return

        val tomorrowAd = todayAd.plusDays(1)
        val tomorrowBs = try {
            DateConverterEngine.convertAdToBs(tomorrowAd)
        } catch (_: Exception) {
            null
        }

        val todayTithi = TithiCalculator.getTithi(todayBs.year, todayBs.month, todayBs.day)
        val tomorrowTithi = if (tomorrowBs != null) {
            TithiCalculator.getTithi(tomorrowBs.year, tomorrowBs.month, tomorrowBs.day)
        } else ""

        val todayFestival = ParvaFestivalData.getFestivalForDate(todayBs.year, todayBs.month, todayBs.day, todayTithi)
        val tomorrowFestival = if (tomorrowBs != null) {
            ParvaFestivalData.getFestivalForDate(tomorrowBs.year, tomorrowBs.month, tomorrowBs.day, tomorrowTithi)
        } else null

        // Case 1: Tomorrow has a Parva/Festival/Vrata (Advance Evening Alert e.g. 5 PM to 11 PM or overnight)
        if (tomorrowBs != null && tomorrowFestival != null) {
            val tomorrowKey = "${tomorrowBs.year}-${tomorrowBs.month}-${tomorrowBs.day}-${tomorrowFestival.id}"
            val lastAdvance = sharedPrefs.getString(KEY_LAST_ADVANCE_NOTIFIED, "")

            if (lastAdvance != tomorrowKey && (now.hour >= 17 || now.hour < 4)) {
                sendAdvanceFestivalNotification(context, tomorrowBs, tomorrowFestival)
                sharedPrefs.edit().putString(KEY_LAST_ADVANCE_NOTIFIED, tomorrowKey).apply()
                return
            }
        }

        // Case 2: Today is the Festival Day (Morning Start Alert e.g. early morning before sunrise / before 1 PM)
        if (todayFestival != null) {
            val todayKey = "${todayBs.year}-${todayBs.month}-${todayBs.day}-${todayFestival.id}"
            val lastDay = sharedPrefs.getString(KEY_LAST_DAY_NOTIFIED, "")

            if (lastDay != todayKey && now.hour <= 14) {
                sendMorningFestivalNotification(context, todayBs, todayFestival)
                sharedPrefs.edit().putString(KEY_LAST_DAY_NOTIFIED, todayKey).apply()
            }
        }
    }

    private fun sendAdvanceFestivalNotification(
        context: Context,
        bsDate: NepaliDate,
        festival: FestivalInfo
    ) {
        createNotificationChannel(context)

        val yearNe = NepaliNumerals.toNepaliDigits(bsDate.year)
        val monthNe = NepaliMonths.getMonthName(bsDate.month, true)
        val dayNe = NepaliNumerals.toNepaliDigits(bsDate.day)

        val title = "🔔 भोलि ${festival.name}"

        val body = buildString {
            appendLine("भोलि वि.सं. $yearNe $monthNe $dayNe गते, ${festival.name} पर्दछ।")
            appendLine("📖 विशेषता तथा महत्व: ${festival.significance}")
            appendLine("🙏 तयारी तथा विधि: ${festival.ritualsAndRules}")
            if (festival.customGreeting.isNotEmpty()) {
                appendLine("🌸 ${festival.customGreeting}")
            }
        }.trim()

        postNotification(context, title, body, "भोलिको पावन पर्व अग्रिम सूचना")
    }

    private fun sendMorningFestivalNotification(
        context: Context,
        bsDate: NepaliDate,
        festival: FestivalInfo
    ) {
        createNotificationChannel(context)

        val yearNe = NepaliNumerals.toNepaliDigits(bsDate.year)
        val monthNe = NepaliMonths.getMonthName(bsDate.month, true)
        val dayNe = NepaliNumerals.toNepaliDigits(bsDate.day)

        val title = "🌸 आज ${festival.name} — हार्दिक शुभकामना!"

        val body = buildString {
            appendLine("वि.सं. $yearNe $monthNe $dayNe गते | ${festival.name}")
            appendLine("📖 धार्मिक तथा सांस्कृतिक महत्व: ${festival.significance}")
            appendLine("✨ आजको पूजा तथा उत्सव: ${festival.ritualsAndRules}")
            if (festival.customGreeting.isNotEmpty()) {
                appendLine("🎉 ${festival.customGreeting}")
            }
        }.trim()

        postNotification(context, title, body, "आजको पावन चाडपर्व")
    }

    private fun postNotification(
        context: Context,
        title: String,
        body: String,
        subText: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("EXTRA_DESTINATION", "panchanga")
            putExtra("route", "panchanga")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setSubText(subText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(Color.parseColor("#F59E0B")) // Saffron Gold
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    /**
     * Schedules periodic and exact alarms to check and trigger festival notifications on time.
     */
    fun scheduleFestivalAlarms(context: Context) {
        try {
            // 1. Exact Alarm 1: Evening check at 19:00 (7:00 PM) for tomorrow's festival advance alert
            scheduleSingleAlarm(context, hour = 19, minute = 0, requestCode = 301)

            // 2. Exact Alarm 2: Morning check at 05:15 (5:15 AM) for today's festival start alert (before sunrise)
            scheduleSingleAlarm(context, hour = 5, minute = 15, requestCode = 302)

            // 3. WorkManager Periodic Work as fallback (every 8 hours)
            scheduleWorkManagerBackup(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun scheduleSingleAlarm(context: Context, hour: Int, minute: Int, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, EkadashiAlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    private fun scheduleWorkManagerBackup(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build()

        val periodicWork = PeriodicWorkRequestBuilder<EkadashiNotificationWorker>(
            8, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "glow_date_festival_periodic_check",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWork
        )
    }
}
