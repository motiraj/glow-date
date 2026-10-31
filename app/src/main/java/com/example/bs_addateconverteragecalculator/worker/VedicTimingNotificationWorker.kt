package com.example.bs_addateconverteragecalculator.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bs_addateconverteragecalculator.MainActivity
import com.example.bs_addateconverteragecalculator.R
import com.example.bs_addateconverteragecalculator.data.PanchangaCalculator
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.util.Locale

class VedicTimingNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val CHANNEL_MUHURTA = "glow_date_muhurta_alerts"
        const val CHANNEL_CAUTION = "glow_date_caution_alerts"
        const val KEY_TIMING_TYPE = "timing_type" // "abhijit" or "rahu"

        fun createNotificationChannels(context: Context) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val muhurtaChannel = NotificationChannel(
                CHANNEL_MUHURTA,
                "अभिजित मुहूर्त सूचना (Auspicious Alerts)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications 10 minutes before Abhijit Muhurta starts"
                enableLights(true)
                lightColor = android.graphics.Color.parseColor("#F59E0B")
                enableVibration(true)
            }
            manager.createNotificationChannel(muhurtaChannel)

            val cautionChannel = NotificationChannel(
                CHANNEL_CAUTION,
                "राहुकाल सतर्कता सूचना (Caution Alerts)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications 10 minutes before Rahu Kalam starts"
                enableLights(true)
                lightColor = android.graphics.Color.parseColor("#EF4444")
                enableVibration(true)
            }
            manager.createNotificationChannel(cautionChannel)
        }
    }

    override suspend fun doWork(): Result {
        try {
            val context = applicationContext
            val timingType = inputData.getString(KEY_TIMING_TYPE) ?: return Result.success()

            val prefs = PreferencesManager(context)
            val isAbhijitEnabled = prefs.abhijitAlertFlow.first()
            val isRahuEnabled = prefs.rahuAlertFlow.first()

            val location = prefs.locationFlow.first()
            val todayAd = LocalDate.now()
            val intervals = PanchangaCalculator.getMuhuratIntervals(todayAd, location.lat, location.lon, location.tzOffset)

            createNotificationChannels(context)

            if (timingType == "abhijit" && isAbhijitEnabled) {
                val abhijit = intervals.find { it.title.contains("अभिजित") }
                if (abhijit != null) {
                    val startStr = formatTime(abhijit.startMinutes)
                    val endStr = formatTime(abhijit.endMinutes)
                    val body = String.format(
                        Locale.getDefault(),
                        "आजको अभिजित मुहूर्त करिब %s देखि %s सम्म रहनेछ। 'अभिजित' को अर्थ सधैँ जित्ने वा विजेता हो। यसलाई शुभ मुहूर्तको राजा मानिन्छ। नयाँ व्यापारको सुरुवात, महत्त्वपूर्ण निर्णय, बैंकिङ कारोबार वा शुभ कार्यको थालनी गर्न यो समय सर्वोत्तम मानिन्छ।",
                        startStr, endStr
                    )
                    showNotification(
                        context,
                        CHANNEL_MUHURTA,
                        3001,
                        "🌟 अभिजित मुहूर्त सुरु हुँदैछ (शुभ समय)",
                        body,
                        android.graphics.Color.parseColor("#F59E0B")
                    )
                }
            } else if (timingType == "rahu" && isRahuEnabled) {
                val rahu = intervals.find { it.title.contains("राहुकाल") }
                if (rahu != null) {
                    val startStr = formatTime(rahu.startMinutes)
                    val endStr = formatTime(rahu.endMinutes)
                    val body = String.format(
                        Locale.getDefault(),
                        "आजको राहुकाल करिब %s देखि %s सम्म रहनेछ। यो समयमा नयाँ कामको थालनी, महत्त्वपूर्ण यात्रा, सम्झौता वा नयाँ सामान खरिद नगर्नुहोला। पहिलेदेखि चलिरहेका कामहरू निरन्तर गर्न सकिनेछ।",
                        startStr, endStr
                    )
                    showNotification(
                        context,
                        CHANNEL_CAUTION,
                        3002,
                        "⚠️ राहुकाल सतर्कता (अशुभ समय)",
                        body,
                        android.graphics.Color.parseColor("#EF4444")
                    )
                }
            }

            // Reschedule for tomorrow
            VedicTimingNotificationScheduler.scheduleDailyTimingAlarms(context)
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }

    private fun formatTime(mins: Int): String {
        var h = (mins / 60) % 24
        val m = mins % 60
        val amPm = if (h < 12) "AM" else "PM"
        if (h == 0) h = 12
        else if (h > 12) h -= 12
        return String.format(Locale.US, "%02d:%02d %s", h, m, amPm)
    }



    private fun showNotification(context: Context, channelId: String, notificationId: Int, title: String, content: String, color: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("route", "panchanga")
            putExtra("EXTRA_DESTINATION", "panchanga")
        }
        val pendingIntent = PendingIntent.getActivity(
            context, notificationId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(color)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, builder.build())
    }
}
