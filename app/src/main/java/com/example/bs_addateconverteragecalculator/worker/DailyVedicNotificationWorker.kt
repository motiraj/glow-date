@file:Suppress("SpellCheckingInspection")
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
import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.HolidayData
import com.example.bs_addateconverteragecalculator.data.NakshatraYogaCalculator
import com.example.bs_addateconverteragecalculator.data.NepaliDays
import com.example.bs_addateconverteragecalculator.data.NepaliMonths
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import com.example.bs_addateconverteragecalculator.data.PanchangaCalculator
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import com.example.bs_addateconverteragecalculator.data.RashifalData
import com.example.bs_addateconverteragecalculator.data.TithiCalculator
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.util.Calendar

class DailyVedicNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val CHANNEL_ID = "glow_date_daily_updates"
        const val NOTIFICATION_ID = 2026
    }

    override suspend fun doWork(): Result {
        try {
            val context = applicationContext
            createNotificationChannel(context)

            val todayAd = LocalDate.now()
            val currentBsYearApprox = todayAd.year + 57
            val todayBs = try { DateConverterEngine.convertAdToBs(todayAd) } catch (_: Exception) { com.example.bs_addateconverteragecalculator.data.NepaliDate(currentBsYearApprox, 1, 1) }
            val monthNe = NepaliMonths.getMonthName(todayBs.month, true)
            val dayNe = NepaliNumerals.toNepaliDigits(todayBs.day)
            val yearNe = NepaliNumerals.toNepaliDigits(todayBs.year)
            val dayOfWeekNe = NepaliDays.getDayName(todayAd.dayOfWeek.value % 7, true)

            val tithi = if (todayBs != null) TithiCalculator.getShortTithi(todayBs.year, todayBs.month, todayBs.day) else "शुक्ल पक्ष"
            val holiday = if (todayBs != null) HolidayData.getHoliday(todayBs.year, todayBs.month, todayBs.day) else null
            
            val lat = 27.7172
            val lon = 85.3240
            val tz = 5.75
            val (sunrise, sunset) = PanchangaCalculator.getSunriseSunset(todayAd, lat, lon, tz)
            val nVal = NakshatraYogaCalculator.getNakshatraValue(todayAd, 6.0, tz)
            val nakshatraName = NakshatraYogaCalculator.nakshatras.getOrNull(nVal.toInt() % 27) ?: "उत्तराषाढा"

            val prefs = PreferencesManager(context)
            val zodiacId = prefs.userZodiacFlow.first()
            val sign = RashifalData.signs.find { it.id == zodiacId } ?: RashifalData.signs[0]
            val prediction = RashifalData.getPrediction(sign.id)

            // Rotate category based on day of year
            val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            val category = dayOfYear % 4

            val (title, body, targetRoute) = when (category) {
                0 -> {
                    // Category A: Today's Nepali Date & Special Parva
                    val parvaText = holiday?.titleNepali ?: "आजको विशिष्ट तिथि: $tithi"
                    Triple(
                        "आजको मिति र विशेष पर्व",
                        "वि.सं. $yearNe $monthNe $dayNe गते, $dayOfWeekNe\n🎉 $parvaText",
                        "panchanga"
                    )
                }
                1 -> {
                    // Category B: Today's Panchanga Essentials
                    Triple(
                        "आजको पञ्चाङ्ग (Glow Date)",
                        "तिथि: $tithi\nनक्षत्र: $nakshatraName\nसूर्योदय: $sunrise | सूर्यास्त: $sunset",
                        "panchanga"
                    )
                }
                2 -> {
                    // Category C: Today's Daily Rashifal & Lucky Insights
                    Triple(
                        "आजको राशिफल र शुभ मुहूर्त (${sign.symbol} ${sign.nameNepali})",
                        "शुभ रंग: ${prediction.luckyColor} | शुभ अंक: ${prediction.luckyNumber}\nक्षेत्र: ${prediction.profession}",
                        "rashifal"
                    )
                }
                else -> {
                    // Category D: Daily Astrological Wisdom & Subha Choghadiya
                    Triple(
                        "ज्योतिषीय सन्देश र अमृत वेला",
                        "आजको दिन शुभ फल प्राप्त गर्न बिहानै सूर्यदेवको दर्शन र जप गर्नुहोस्। आजको पञ्चाङ्ग विवरण हेर्नुहोस्।",
                        "panchanga"
                    )
                }
            }

            showNotification(context, title, body, targetRoute)
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }

    private fun createNotificationChannel(context: Context) {
        val name = "दैनिक पञ्चाङ्ग तथा राशिफल"
        val descriptionText = "Glow Date daily morning Vedic insights and Panchanga notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
            enableLights(true)
            lightColor = android.graphics.Color.parseColor("#F59E0B") // Solar Gold LED
            enableVibration(true)
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun showNotification(context: Context, title: String, content: String, targetRoute: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("EXTRA_DESTINATION", targetRoute)
            putExtra("route", targetRoute)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(android.graphics.Color.parseColor("#F59E0B")) // Solar Amber accent
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}
