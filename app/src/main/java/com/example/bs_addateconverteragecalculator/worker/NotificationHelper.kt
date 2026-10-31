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
import java.util.Calendar

object NotificationHelper {
    private const val CHANNEL_ID = "daily_panchanga_channel_v2"
    private const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Panchanga, Rashifal & Muhurat Updates"
            val descriptionText = "Daily updates for Panchanga, Rashifal, Barvela, and Hora Muhurat"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, title: String, content: String, targetRoute: String = "panchanga") {
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

    private const val PERSISTENT_NOTIFICATION_ID = 1002

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

        val cleanTithi = tithi.split(" ").lastOrNull() ?: "प्रतिपदा"
        val paksha = if (tithi.contains("कृष्ण")) "कृष्ण" else "शुक्ल"
        val nsDetails = try {
            com.example.bs_addateconverteragecalculator.data.NepalSambatCalculator.getNepalSambatDetails(todayBs.year, todayBs.month, todayBs.day, paksha, cleanTithi)
        } catch (_: Exception) {
            "नेपाल संवत्"
        }

        val title = "📅 वि.सं. $yearNe $monthNe $dayNe गते, $dayOfWeekNe"
        val content = "$nsDetails | $tithi ($adFormatted)"

        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("route", "panchanga")
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 1002, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(PERSISTENT_NOTIFICATION_ID, builder.build())
    }

    fun cancelPersistentDateNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(PERSISTENT_NOTIFICATION_ID)
    }

    fun scheduleDailyNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyAlarmReceiver::class.java)
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 5)
            set(Calendar.MINUTE, 0)
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
}
