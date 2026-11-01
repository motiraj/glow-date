@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.worker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.bs_addateconverteragecalculator.MainActivity
import com.example.bs_addateconverteragecalculator.R
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

object NotificationHelper {
    private const val CHANNEL_ID = "daily_panchanga_channel_v2"
    private const val PERSISTENT_CHANNEL_ID = "glow_date_persistent_status_channel_v1"
    private const val NOTIFICATION_ID = 1001
    private const val PERSISTENT_NOTIFICATION_ID = 1002
    private const val REQUEST_CODE_DAILY_ALARM = 0
    private const val REQUEST_CODE_MIDNIGHT_ALARM = 1005

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // 1. High priority channel for interactive daily alerts
            val name = "Panchanga, Rashifal & Muhurat Updates"
            val descriptionText = "Daily updates for Panchanga, Rashifal, Barvela, and Hora Muhurat"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)

            // 2. Silent, ongoing low-priority channel for lock screen & status bar date display
            val persistentName = "नेपाली मिति बार (Status Bar Date)"
            val persistentDesc = "मोबाइलको नोटिफिकेशन बार तथा लक स्क्रिनमा निरन्तर दैनिक मिति र नेपाल संवत्"
            val persistentChannel = NotificationChannel(
                PERSISTENT_CHANNEL_ID,
                persistentName,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = persistentDesc
                enableVibration(false)
                setShowBadge(false)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(persistentChannel)
        }
    }

    fun showNotification(context: Context, title: String, content: String, targetRoute: String = "panchanga") {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("route", targetRoute) 
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun showPersistentDateNotification(context: Context) {
        val todayAd = java.time.LocalDate.now()
        val currentBsYearApprox = todayAd.year + 57
        val todayBs = try {
            com.example.bs_addateconverteragecalculator.converter.DateConverterEngine.convertAdToBs(todayAd)
        } catch (_: Exception) {
            com.example.bs_addateconverteragecalculator.data.NepaliDate(currentBsYearApprox, 1, 1)
        }

        val monthNe = try { com.example.bs_addateconverteragecalculator.data.NepaliMonths.getMonthName(todayBs.month, true) } catch (_: Exception) { "वैशाख" }
        val dayNe = try { com.example.bs_addateconverteragecalculator.data.NepaliNumerals.toNepaliDigits(todayBs.day) } catch (_: Exception) { "१" }
        val yearNe = try { com.example.bs_addateconverteragecalculator.data.NepaliNumerals.toNepaliDigits(todayBs.year) } catch (_: Exception) { com.example.bs_addateconverteragecalculator.data.NepaliNumerals.toNepaliDigits(currentBsYearApprox) }
        val dayOfWeekNe = try { com.example.bs_addateconverteragecalculator.data.NepaliDays.getDayName(todayAd.dayOfWeek.value % 7, true) } catch (_: Exception) { "आइतबार" }

        val adFormatted = try { todayAd.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy (EEEE)", java.util.Locale.ENGLISH)) } catch (_: Exception) { "" }
        val tithi = try { com.example.bs_addateconverteragecalculator.data.TithiCalculator.getShortTithi(todayBs.year, todayBs.month, todayBs.day) } catch (_: Exception) { "शुक्ल पक्ष" }
        val holiday = try { com.example.bs_addateconverteragecalculator.data.HolidayData.getHoliday(todayBs.year, todayBs.month, todayBs.day) } catch (_: Exception) { null }

        val cleanTithi = tithi.split(" ").lastOrNull() ?: "प्रतिपदा"
        val paksha = if (tithi.contains("कृष्ण")) "कृष्ण" else "शुक्ल"
        val nsDetails = try {
            com.example.bs_addateconverteragecalculator.data.NepalSambatCalculator.getNepalSambatDetails(todayBs.year, todayBs.month, todayBs.day, paksha, cleanTithi)
        } catch (_: Exception) {
            "नेपाल संवत्"
        }

        val title = "📅 वि.सं. $yearNe $monthNe $dayNe गते, $dayOfWeekNe"
        val content = if (holiday != null) {
            "$nsDetails | $tithi ($adFormatted) • 🔴 ${holiday.titleNepali}"
        } else {
            "$nsDetails | $tithi ($adFormatted)"
        }

        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("route", "panchanga")
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 1002, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, PERSISTENT_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(PERSISTENT_NOTIFICATION_ID, builder.build())
    }

    fun updatePersistentDateNotification(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val prefs = PreferencesManager(context)
                val isEnabled = try {
                    prefs.persistentNotificationFlow.first()
                } catch (_: Exception) {
                    true
                }
                if (isEnabled) {
                    showPersistentDateNotification(context)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cancelPersistentDateNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(PERSISTENT_NOTIFICATION_ID)
    }

    fun scheduleMidnightUpdateAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, MidnightUpdateReceiver::class.java).apply {
            action = MidnightUpdateReceiver.ACTION_MIDNIGHT_UPDATE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_MIDNIGHT_ALARM,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            // Target: exactly 2 seconds past 12:00:00 midnight (00:00:02) to guarantee date has ticked
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 2)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val triggerAtMillis = calendar.timeInMillis

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun scheduleDailyNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, DailyAlarmReceiver::class.java)
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_DAILY_ALARM,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 5)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val triggerAtMillis = calendar.timeInMillis

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
